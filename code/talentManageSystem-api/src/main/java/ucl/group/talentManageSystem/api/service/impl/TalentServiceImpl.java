package ucl.group.talentManageSystem.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucl.group.talentManageSystem.api.common.PageUtils;
import ucl.group.talentManageSystem.api.common.constants.CacheConstants;
import ucl.group.talentManageSystem.api.common.constants.DictTypeConstants;
import ucl.group.talentManageSystem.api.common.utils.DateUtils;
import ucl.group.talentManageSystem.api.common.utils.SecurityUtils;
import ucl.group.talentManageSystem.api.controller.form.talentForm.BusinessYearForm;
import ucl.group.talentManageSystem.api.controller.form.talentForm.LabelYearForm;
import ucl.group.talentManageSystem.api.controller.form.talentForm.TalentAddForm;
import ucl.group.talentManageSystem.api.controller.form.talentForm.TalentInfoByPageForm;
import ucl.group.talentManageSystem.api.db.dao.*;
import ucl.group.talentManageSystem.api.db.pojo.*;
import ucl.group.talentManageSystem.api.db.pojo.vo.BusinessYear;
import ucl.group.talentManageSystem.api.db.pojo.vo.InterAndProInfoVO;
import ucl.group.talentManageSystem.api.db.pojo.vo.LabelYear;
import ucl.group.talentManageSystem.api.exception.ServiceException;
import ucl.group.talentManageSystem.api.service.*;

import java.util.*;

@Service
@Slf4j
public class TalentServiceImpl implements TalentService {

    @Autowired
    private TalentDao talentDao;
    @Autowired
    private BusinessDao businessDao;
    @Autowired
    private InterviewInfoDao interviewInfoDao;
    @Autowired
    private ProjectInfoDao projectInfoDao;

    @Autowired
    private EngineerDao engineerDao;
    @Autowired
    private RedisService redisService;
    @Autowired
    private EngineerService engineerService;
    @Autowired
    private DictService dictService;
    @Autowired
    private InterviewInfoService interviewInfoService;

    @Override
    public PageUtils searchByPage(TalentInfoByPageForm form) {
        int total= talentDao.searchCount(form);

        form.setEngineerSize(form.getEngineerIds()==null?0:form.getEngineerIds().size());
        if(form.getLabelYears()!=null){
            List<Integer> integers = talentDao.selectTalentIdByLabelYear(form.getLabelYears());
           if(integers.isEmpty()){
               PageUtils pageUtils = new PageUtils(new ArrayList(),total, form.getPage(), form.getLength());
               return pageUtils;
           }else{
               form.setTalentIds(integers);
           }
        }

        //这里获取到全部的数据
        List<BasicTalentEntity> talentEntities = talentDao.searchByPage(form);

        //这里是排好序的而且截取好的数据
        if (talentEntities.isEmpty()) {
            talentEntities = Collections.emptyList();
        } else {
            //修改中日文：性别 国籍 日语等级 所属公司 任用状态
            //String userId = StpUtil.getLoginIdAsString();
            String userId = SecurityUtils.getUserIdAsString();
            String langName = redisService.getCacheObject(redisService.getLangCacheKey(userId));
            for (BasicTalentEntity basicTalentEntity : talentEntities) {
                updateTalentJap(basicTalentEntity, langName);

                int talentId = basicTalentEntity.getTalentId();
                List<BasicInterviewInfoEntity> interviewInfoList = interviewInfoService.searchByTalentId(talentId);
                basicTalentEntity.setInterviewInfoList(!interviewInfoList.isEmpty() ? interviewInfoList : Collections.emptyList());

                List<BasicProjectInfoEntity> projectInfoList = projectInfoDao.searchByTalentId(talentId);
                basicTalentEntity.setProjectInfoList(!projectInfoList.isEmpty() ? projectInfoList : Collections.emptyList());

                List<BasicEngineerEntity> engineerList = engineerService.searchByTalentId(talentId);
                basicTalentEntity.setEngineerNameList(!engineerList.isEmpty() ? engineerList : Collections.emptyList());

                List<LabelYear> talentLabelYearList = searchLabelByTalentId(talentId);
                basicTalentEntity.setTalentLabelList(!talentLabelYearList.isEmpty() ? talentLabelYearList : Collections.emptyList());

                //查询行业和年份
                List<BusinessYear> talentBusinessYearList = businessDao.searchBusinessByTalentId(talentId);
                basicTalentEntity.setTalentBusinessList(!talentBusinessYearList.isEmpty() ? talentBusinessYearList : Collections.emptyList());
            }
        }

        //这里筛选出latestDate
        for (BasicTalentEntity talent : talentEntities){
            if (talent.getInterviewInfoList() != null && talent.getInterviewInfoList().size() != 0){
                // 如果有面试信息的处理
                List<Date> datelist = new ArrayList<>();
                for (BasicInterviewInfoEntity interview : talent.getInterviewInfoList()){
                    if (interview.getBusDate() != null){
                        datelist.add(interview.getBusDate());
                    }
                    if (interview.getTechDate() != null){
                        datelist.add(interview.getTechDate());
                    }
                }
                // datelist中是全部的面试日期，没有null和空
                if (datelist.isEmpty()){
                    talent.setLatestDate(null);
                } else {
                    talent.setLatestDate(Collections.max(datelist));
                }
            } else {
                //如果没有面试信息，设置为空
                talent.setLatestDate(null);
            }
        }

// 这里进行排序
        if (form.getOrderColumn() != null && form.getOrderColumn().equals("interview_info")
                && form.getOrderSeq() != null && !form.getOrderSeq().isEmpty()) {
            String orderSeq = form.getOrderSeq();

            talentEntities.sort(new Comparator<BasicTalentEntity>() {
                @Override
                public int compare(BasicTalentEntity o1, BasicTalentEntity o2) {
                    // 处理 null 的情况
                    if (o1.getLatestDate() == null && o2.getLatestDate() == null) {
                        return 0; // 两者都为 null，认为相等
                    }
                    if (o1.getLatestDate() == null) {
                        return 1; // o1 为 null，排在后面
                    }
                    if (o2.getLatestDate() == null) {
                        return -1; // o2 为 null，排在后面
                    }
                    // 比较非 null 的日期
                    return orderSeq.equalsIgnoreCase("ASC")
                            ? o1.getLatestDate().compareTo(o2.getLatestDate())
                            : o2.getLatestDate().compareTo(o1.getLatestDate());
                }
            });
        }

        //截取逻辑
        int start = form.getStart(); // 获取起始索引
        int length = form.getLength(); // 获取要提取的元素数量
        int end = Math.min(start + length, talentEntities.size());
        talentEntities = talentEntities.subList(start,end);

        PageUtils pageUtils = new PageUtils(talentEntities,total, form.getPage(), form.getLength());
        return pageUtils;
    }

    @Override
    public PageUtils searchByPage2(TalentInfoByPageForm form) {
        int total= talentDao.searchCount(form);

        form.setEngineerSize(form.getEngineerIds()==null?0:form.getEngineerIds().size());
        if(form.getLabelYears()!=null){
            List<Integer> integers = talentDao.selectTalentIdByLabelYear(form.getLabelYears());
            if(integers.isEmpty()){
                PageUtils pageUtils = new PageUtils(new ArrayList(),total, form.getPage(), form.getLength());
                return pageUtils;
            }else{
                form.setTalentIds(integers);
            }
        }
        List<BasicTalentEntity> talentEntities = talentDao.searchByPage(form);
        if (talentEntities.isEmpty()) {
            talentEntities = Collections.emptyList();
        } else {
            //修改中日文：性别 国籍 日语等级 所属公司 任用状态
            //String userId = StpUtil.getLoginIdAsString();
            String userId = SecurityUtils.getUserIdAsString();
            String langName = redisService.getCacheObject(redisService.getLangCacheKey(userId));
            for (BasicTalentEntity basicTalentEntity : talentEntities) {
                updateTalentJap(basicTalentEntity, langName);
                int talentId = basicTalentEntity.getTalentId();
                //下面这些不能看
                basicTalentEntity.setEmail(null);
                basicTalentEntity.setLine(null);
                basicTalentEntity.setPhone(null);
                basicTalentEntity.setWechat(null);
                List<BasicInterviewInfoEntity> interviewInfoList = interviewInfoService.searchByTalentId(talentId);
                basicTalentEntity.setInterviewInfoList(!interviewInfoList.isEmpty() ? interviewInfoList : Collections.emptyList());

                List<BasicProjectInfoEntity> projectInfoList = projectInfoDao.searchByTalentId(talentId);
                basicTalentEntity.setProjectInfoList(!projectInfoList.isEmpty() ? projectInfoList : Collections.emptyList());

                List<BasicEngineerEntity> engineerList = engineerService.searchByTalentId(talentId);
                basicTalentEntity.setEngineerNameList(!engineerList.isEmpty() ? engineerList : Collections.emptyList());
                List<LabelYear> talentLabelYearList = searchLabelByTalentId(talentId);
                basicTalentEntity.setTalentLabelList(!talentLabelYearList.isEmpty() ? talentLabelYearList : Collections.emptyList());

                //查询行业和年份
                List<BusinessYear> talentBusinessYearList = businessDao.searchBusinessByTalentId(talentId);
                basicTalentEntity.setTalentBusinessList(!talentBusinessYearList.isEmpty() ? talentBusinessYearList : Collections.emptyList());
            }
        }
        PageUtils pageUtils = new PageUtils(talentEntities,total, form.getPage(), form.getLength());
        return pageUtils;
    }

    @Override
    public PageUtils searchByPage3(TalentInfoByPageForm form) {
        int total= talentDao.searchCount(form);
        form.setEngineerSize(form.getEngineerIds()==null?0:form.getEngineerIds().size());
//        if(form.getLabelYears()!=null){
//            List<Integer> integers = talentDao.selectTalentIdByLabelYear(form.getLabelYears());
//            form.setTalentIds(integers);
//        }
        if(form.getLabelYears()!=null){
            List<Integer> integers = talentDao.selectTalentIdByLabelYear(form.getLabelYears());
            if(integers.isEmpty()){
                PageUtils pageUtils = new PageUtils(new ArrayList(),total, form.getPage(), form.getLength());
                return pageUtils;
            }else{
                form.setTalentIds(integers);
            }
        }
        List<BasicTalentEntity> talentEntities = talentDao.searchByPage(form);
        if (talentEntities.isEmpty()) {
            talentEntities = Collections.emptyList();
        } else {
            //修改中日文：性别 国籍 日语等级 所属公司 任用状态
            //String userId = StpUtil.getLoginIdAsString();
            String userId = SecurityUtils.getUserIdAsString();
            String langName = redisService.getCacheObject(redisService.getLangCacheKey(userId));
            for (BasicTalentEntity basicTalentEntity : talentEntities) {
                updateTalentJap(basicTalentEntity, langName);
                int talentId = basicTalentEntity.getTalentId();
                basicTalentEntity.setEmail(null);
                basicTalentEntity.setLine(null);
                basicTalentEntity.setPhone(null);
                basicTalentEntity.setWechat(null);
                //价格不能看
                basicTalentEntity.setPrice(-1);
                List<BasicInterviewInfoEntity> interviewInfoList = interviewInfoService.searchByTalentId(talentId);
                basicTalentEntity.setInterviewInfoList(!interviewInfoList.isEmpty() ? interviewInfoList : Collections.emptyList());

                List<BasicProjectInfoEntity> projectInfoList = projectInfoDao.searchByTalentId(talentId);
                basicTalentEntity.setProjectInfoList(!projectInfoList.isEmpty() ? projectInfoList : Collections.emptyList());

                List<BasicEngineerEntity> engineerList = engineerService.searchByTalentId(talentId);
                basicTalentEntity.setEngineerNameList(!engineerList.isEmpty() ? engineerList : Collections.emptyList());

                List<LabelYear> talentLabelYearList = searchLabelByTalentId(talentId);
                basicTalentEntity.setTalentLabelList(!talentLabelYearList.isEmpty() ? talentLabelYearList : Collections.emptyList());

                //查询行业和年份
                List<BusinessYear> talentBusinessYearList = businessDao.searchBusinessByTalentId(talentId);
                basicTalentEntity.setTalentBusinessList(!talentBusinessYearList.isEmpty() ? talentBusinessYearList : Collections.emptyList());
            }
        }
        PageUtils pageUtils = new PageUtils(talentEntities, total, form.getPage(), form.getLength());
        return pageUtils;
    }

    /**
     * 修改日文
     * 性别 国籍 日语等级 所属公司 任用状态
     *
     * @param basicTalentEntity
     * @return
     */
    public void updateTalentJap(BasicTalentEntity basicTalentEntity, String lang) {
        basicTalentEntity.setSex(dictService.getDictName(DictTypeConstants.SEX, lang, basicTalentEntity.getSex()));
        basicTalentEntity.setNation(dictService.getDictName(DictTypeConstants.NATION, lang, basicTalentEntity.getNation()));
        basicTalentEntity.setJapanLevel(dictService.getDictName(DictTypeConstants.JAPAN_LEVEL, lang, basicTalentEntity.getJapanLevel()));
        basicTalentEntity.setBelongCompany(dictService.getDictName(DictTypeConstants.COMPANY, lang, basicTalentEntity.getBelongCompany()));
        basicTalentEntity.setAppointStatus(dictService.getDictName(DictTypeConstants.APPOINT_STATUS, lang, basicTalentEntity.getAppointStatus()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int  add(TalentAddForm talentAddForm) {

        BasicTalentEntity basicTalentEntity = new BasicTalentEntity(
                talentAddForm.getStatus(),
                talentAddForm.getRemark(),
                null,
                null,
                null,
                talentAddForm.getName(),
                talentAddForm.getPseudonym(),
                talentAddForm.getEnglishName(),
                talentAddForm.getSex(),
                talentAddForm.getBirth(),
                talentAddForm.getExprYear(),
                talentAddForm.getNation(),
                talentAddForm.getInJapanYear(),
                talentAddForm.getJapanLevel(),
                talentAddForm.getStation(),
                talentAddForm.getSchool(),
                talentAddForm.getMajor(),
                talentAddForm.getPicture(),
                talentAddForm.getSkillSheet(),
                talentAddForm.getTalentDescription(),
                talentAddForm.getBelongCompany(),
                talentAddForm.getAppointStatus(),
                talentAddForm.getBlacklistReason(),
                talentAddForm.getBlacklistTime(),
                talentAddForm.getBlacklistBy(),
                talentAddForm.getEmail(),
                talentAddForm.getPhone(),
                talentAddForm.getWechat(),
                talentAddForm.getLine(),
                talentAddForm.getPrice(),
                talentAddForm.getCompanyName(),
                talentAddForm.getPredictMonth()
        );
        basicTalentEntity.setCreateBy(SecurityUtils.getUsername());
        int row = talentDao.add(basicTalentEntity);
        //插入标签年份
        List<LabelYearForm> labelYears = talentAddForm.getLabelYears();
        if (!labelYears.isEmpty()) {
            for (LabelYearForm temp : labelYears) {
                talentDao.addTalentLabel(new RelatedTalentLabel(basicTalentEntity.getTalentId(), temp.getLabelId(), temp.getLabelYear()));
            }
        }
        //插入行业年份
        List<BusinessYearForm> businessYears = talentAddForm.getBusinessYears();
        if (!businessYears.isEmpty()){
            for (BusinessYearForm temp : businessYears){
                talentDao.addTalentBusiness(new RelatedTalentBusiness(basicTalentEntity.getTalentId(), temp.getBusinessId(), temp.getBusinessYear()));
            }
        }
        //插入工程表
        List<Integer> engineerIds = talentAddForm.getEngineerIds();
        if (!engineerIds.isEmpty()) {
            for (Integer temp : engineerIds) {
                engineerDao.add(new RelatedTalentEngineer(basicTalentEntity.getTalentId(), temp));
            }
        }
        return row;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int edit(TalentAddForm talentAddForm) {

        BasicTalentEntity basicTalentEntity = new BasicTalentEntity(
                talentAddForm.getStatus()==null ? null: talentAddForm.getStatus(),
                talentAddForm.getRemark()==null ? "": talentAddForm.getRemark(),
                null,
                null,
                talentAddForm.getTalentId(),
                talentAddForm.getName()==null ? "": talentAddForm.getName(),
                talentAddForm.getPseudonym()==null ? "":talentAddForm.getPseudonym() ,
                talentAddForm.getEnglishName()==null ? "":talentAddForm.getEnglishName() ,
                talentAddForm.getSex()==null ? "":talentAddForm.getSex() ,
                talentAddForm.getBirth()==null ? "":talentAddForm.getBirth() ,
                talentAddForm.getExprYear(),
                talentAddForm.getNation()==null ? "": talentAddForm.getNation(),
                talentAddForm.getInJapanYear(),
                talentAddForm.getJapanLevel()==null ? "": talentAddForm.getJapanLevel(),
                talentAddForm.getStation()==null ? "": talentAddForm.getStation(),
                talentAddForm.getSchool()==null ? "":talentAddForm.getSchool() ,
                talentAddForm.getMajor()==null ? "": talentAddForm.getMajor(),
                talentAddForm.getPicture()==null ? "": talentAddForm.getPicture(),
                talentAddForm.getSkillSheet()==null ? "":talentAddForm.getSkillSheet() ,
                talentAddForm.getTalentDescription()==null ? "": talentAddForm.getTalentDescription(),
                talentAddForm.getBelongCompany()==null ? "":talentAddForm.getBelongCompany() ,
                talentAddForm.getAppointStatus()==null ? "":talentAddForm.getAppointStatus() ,
                talentAddForm.getBlacklistReason()==null ? "": talentAddForm.getBlacklistReason(),
                talentAddForm.getBlacklistTime(),
                talentAddForm.getBlacklistBy()==null ? "": talentAddForm.getBlacklistBy(),
                talentAddForm.getEmail()==null ? "": talentAddForm.getEmail(),
                talentAddForm.getPhone()==null ? "": talentAddForm.getPhone(),
                talentAddForm.getWechat()==null ? "": talentAddForm.getWechat(),
                talentAddForm.getLine()==null ? "": talentAddForm.getLine(),
                talentAddForm.getPrice(),
                talentAddForm.getCompanyName()==null ? "": talentAddForm.getCompanyName(),
                talentAddForm.getPredictMonth()==null ? null: talentAddForm.getPredictMonth()
        );
        basicTalentEntity.setUpdateBy(SecurityUtils.getUsername());
        int row = talentDao.edit(basicTalentEntity);
        //更新成功再删除
        if (row != 0) {
            //先删除
            talentDao.removeTalentLabelByTalentId(basicTalentEntity.getTalentId());

            //再插入标签年份
            List<LabelYearForm> labelYears = talentAddForm.getLabelYears();
            if (!labelYears.isEmpty()) {
                for (LabelYearForm temp : labelYears) {
                    talentDao.addTalentLabel(new RelatedTalentLabel(basicTalentEntity.getTalentId(), temp.getLabelId(), temp.getLabelYear()));
                }
            }

            //先删除
            talentDao.removeTalentBusinessByTalentId(basicTalentEntity.getTalentId());
            //插入行业年份
            List<BusinessYearForm> businessYears = talentAddForm.getBusinessYears();
            if (!businessYears.isEmpty()){
                for (BusinessYearForm temp : businessYears){
                    talentDao.addTalentBusiness(new RelatedTalentBusiness(basicTalentEntity.getTalentId(), temp.getBusinessId(), temp.getBusinessYear()));
                }
            }

            //先删除
            engineerDao.removeRelated(basicTalentEntity.getTalentId());
            //再插入工程表
            List<Integer> engineerIds = talentAddForm.getEngineerIds();
            if (!engineerIds.isEmpty()) {
                for (Integer temp : engineerIds) {
                    engineerDao.add(new RelatedTalentEngineer(basicTalentEntity.getTalentId(), temp));
                }
            }
        } else {
            throw new ServiceException("修正に失敗しました");
        }
        return row;
    }
    @Override
    public int editBlacklist(TalentAddForm talentAddForm){
        BasicTalentEntity basicTalentEntity = new BasicTalentEntity();
        basicTalentEntity.setStatus(talentAddForm.getStatus()==null?null:talentAddForm.getStatus());
        basicTalentEntity.setTalentId(talentAddForm.getTalentId());
        basicTalentEntity.setBlacklistBy(SecurityUtils.getUsername());
        basicTalentEntity.setBlacklistReason(talentAddForm.getBlacklistReason()==null?"":talentAddForm.getBlacklistReason());
        basicTalentEntity.setBlacklistTime(DateUtils.getTime());
        int row = talentDao.edit(basicTalentEntity);
        return  row;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int remove(int[] talentIds) {
        int row=talentDao.remove(talentIds);
        //更新时间和姓名
        for(int temp:talentIds){
            BasicTalentEntity talentEntity=new BasicTalentEntity();
            talentEntity.setUpdateBy(SecurityUtils.getUsername());
            talentEntity.setTalentId(temp);
            talentEntity.setDeletedBy(SecurityUtils.getUsername());
            talentEntity.setDeletedTime(DateUtils.getTime());
            talentDao.edit(talentEntity);
        }
        return row;

    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int undeletion(int talentId) {
        int row=talentDao.undeletion(talentId);
        //更新时间和姓名
        return row;

    }
    @Override
    public InterAndProInfoVO searchInfoByTalentId(int talentId){
        List<BasicInterviewInfoEntity> basicInterviewInfoEntities = interviewInfoService.searchByTalentId(talentId);
        List<BasicProjectInfoEntity> projectInfoEntityList = projectInfoDao.searchByTalentId(talentId);
        InterAndProInfoVO interAndProInfoVO=new InterAndProInfoVO(basicInterviewInfoEntities,projectInfoEntityList);
return interAndProInfoVO;
    }

    @Override
    public List<LabelYear> searchLabelByTalentId(int talentId) {
        List<LabelYear> labelYears = talentDao.searchLabelByTalentId(talentId);
        String userId = SecurityUtils.getUserIdAsString();
        String langName = redisService.getCacheObject(redisService.getLangCacheKey(userId));
        labelYears.forEach(labelYear -> {
            //中文则不变
            if (langName.equals(CacheConstants.Language_JA)) {
                // 更新标签名为日文
                labelYear.setLabelName(labelYear.getLabelNameJap());
            }
        });
        return labelYears;
    }
    /**
     * 以下是保留返回标签类型中日文的代码
     * 防止后期修改需求
     *  String finalTypeName = typeName;
     *             //只有满足条件的元素才会被包含在当前流的下一阶段处理中。
     *             //findFirst它会返回流中的第一个元素，如果流为空，则返回一个空的Optional
     *             Optional<TalentLabelYear> existingEntry = talentLabelList.stream()
     *                     .filter(t -> t.getTypeName().equals(finalTypeName))
     *                     .findFirst();
     *             if (existingEntry.isPresent()) {
     *                 // 如果存在，只需要添加新的标签到已存在的记录
     *                 existingEntry.get().getLabelYears().add(labelYear);
     *             } else {
     *                 // 如果不存在，创建新的记录并添加到结果列表
     *                 TalentLabelYear newEntry = new TalentLabelYear();
     *                 newEntry.setTypeName(typeName);
     *                 List<LabelYear> temp = new ArrayList<>();
     *                 temp.add(labelYear);
     *                 newEntry.setLabelYears(temp);
     *                 talentLabelList.add(newEntry);
     *             }
     */
}
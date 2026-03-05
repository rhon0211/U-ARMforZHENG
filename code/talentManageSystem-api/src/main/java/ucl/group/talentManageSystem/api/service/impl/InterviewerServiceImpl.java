package ucl.group.talentManageSystem.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucl.group.talentManageSystem.api.common.PageUtils;
import ucl.group.talentManageSystem.api.common.constants.DictTypeConstants;
import ucl.group.talentManageSystem.api.common.utils.SecurityUtils;
import ucl.group.talentManageSystem.api.controller.form.talentForm.InterviewerByPageForm;
import ucl.group.talentManageSystem.api.db.dao.InterviewInfoDao;
import ucl.group.talentManageSystem.api.db.dao.InterviewerDao;
import ucl.group.talentManageSystem.api.db.pojo.BasicInterviewerEntity;
import ucl.group.talentManageSystem.api.db.pojo.vo.InterviewerCountVO;
import ucl.group.talentManageSystem.api.service.DictService;
import ucl.group.talentManageSystem.api.service.InterviewerService;
import ucl.group.talentManageSystem.api.service.RedisService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InterviewerServiceImpl implements InterviewerService {
    @Autowired
    private InterviewerDao interviewerDao;
    @Autowired
    private InterviewInfoDao interviewInfoDao;
    @Autowired
    private RedisService redisService;
    @Autowired
    private DictService dictService;

    @Override
    public PageUtils searchByPage(InterviewerByPageForm interviewerByPageForm) {
        String tempColumnName = interviewerByPageForm.getOrderColumn();
        if (tempColumnName != null && (tempColumnName.equals("interviewedNum") || tempColumnName.equals("hiredNum"))) {
            interviewerByPageForm.setOrderColumn(null);
        }
        int total= interviewerDao.searchCount(interviewerByPageForm);
        List<BasicInterviewerEntity> basicInterviewerEntities = interviewerDao.searchByPage(interviewerByPageForm);
        List<InterviewerCountVO> interviewerCountVOS = new ArrayList<>();
        //修改中日文：面试官类别
        //String userId = StpUtil.getLoginIdAsString();
        String userId = SecurityUtils.getUserIdAsString();
        String langName = redisService.getCacheObject(redisService.getLangCacheKey(userId));
        for (BasicInterviewerEntity temp : basicInterviewerEntities) {
            InterviewerCountVO interviewerCountVO = new InterviewerCountVO(
                    temp.getInterviewerId(),
                    temp.getName(),
                    temp.getPseudonym(),
                    temp.getType(),
                    temp.getStatus(),
                    temp.getDelFlag(),
                    temp.getCreateBy(),
                    temp.getCreateTime(),
                    interviewInfoDao.countInterviewedNum(temp.getInterviewerId(),temp.getType(), interviewerByPageForm.getStartDate(), interviewerByPageForm.getEndDate()),
                    interviewInfoDao.countHiredNum(temp.getInterviewerId(), temp.getType(),interviewerByPageForm.getStartDate(), interviewerByPageForm.getEndDate())
            );
            interviewerCountVO.setType(dictService.getDictName(DictTypeConstants.INTERVIEWER, langName, temp.getType()));
            interviewerCountVOS.add(interviewerCountVO);
        }
        Comparator<InterviewerCountVO> comparator = null;
        //根据面试人数和采用人数进行排序
        if (tempColumnName != null) {
            switch (tempColumnName) {
                case "interviewedNum":
                    comparator = Comparator.comparingInt(InterviewerCountVO::getInterviewedNum);
                    break;
                case "hiredNum":
                    comparator = Comparator.comparingInt(InterviewerCountVO::getHiredNum);
                    break;
                default:
                    break;
            }
        }
        if (comparator != null) {
            List<InterviewerCountVO> sortedInterviewerCountVOS = interviewerCountVOS.stream().sorted(comparator)
                    .collect(Collectors.toList());
            if (interviewerByPageForm.getOrderSeq().equals("DESC")) {
                //默认是asc，desc则进行反转
                comparator = comparator.reversed();
                sortedInterviewerCountVOS = interviewerCountVOS.stream()
                        .sorted(comparator)
                        .collect(Collectors.toList());
            }
            PageUtils pageUtils = new PageUtils(sortedInterviewerCountVOS, total, interviewerByPageForm.getPage(), interviewerByPageForm.getLength());
            return pageUtils;
        } else {
            PageUtils pageUtils = new PageUtils(interviewerCountVOS, total, interviewerByPageForm.getPage(), interviewerByPageForm.getLength());
            return pageUtils;
        }


    }

    @Override
    public BasicInterviewerEntity searchById(int interviewerId) {
        return interviewerDao.searchById(interviewerId);
    }
    @Override
    public List<BasicInterviewerEntity> searchByType(String type) {
        return interviewerDao.searchByType(type);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int add(BasicInterviewerEntity basicInterviewerEntity) {
        basicInterviewerEntity.setCreateBy(SecurityUtils.getUsername());
        return interviewerDao.add(basicInterviewerEntity);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int edit(BasicInterviewerEntity basicInterviewerEntity) {
        basicInterviewerEntity.setUpdateBy(SecurityUtils.getUsername());
        return interviewerDao.edit(basicInterviewerEntity);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int remove(int[] interviewerIds){
        return interviewerDao.remove(interviewerIds);
    }


}
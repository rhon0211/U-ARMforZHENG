package ucl.group.talentManageSystem.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucl.group.talentManageSystem.api.common.utils.SecurityUtils;
import ucl.group.talentManageSystem.api.controller.form.talentForm.TalentInterviewInfoAddForm;
import ucl.group.talentManageSystem.api.db.dao.InterviewInfoDao;
import ucl.group.talentManageSystem.api.db.dao.InterviewerDao;
import ucl.group.talentManageSystem.api.db.pojo.BasicInterviewInfoEntity;
import ucl.group.talentManageSystem.api.db.pojo.BasicInterviewerEntity;
import ucl.group.talentManageSystem.api.db.pojo.RelatedTalentInterview;
import ucl.group.talentManageSystem.api.service.InterviewInfoService;
import ucl.group.talentManageSystem.api.service.RedisService;

import java.util.List;

@Service
@Slf4j
public class InterviewInfoServiceImpl implements InterviewInfoService {

    @Autowired
    private InterviewInfoDao interviewInfoDao;
    @Autowired
    private InterviewerDao interviewerDao;
    @Autowired
    private RedisService redisService;

    @Override
    public List<BasicInterviewInfoEntity> searchByTalentId(Integer talentId) {
        List<BasicInterviewInfoEntity> basicInterviewInfoEntities = interviewInfoDao.searchByTalentId(talentId);
        for (BasicInterviewInfoEntity temp : basicInterviewInfoEntities) {
            if (temp.getBusInterviewer() != null) {
                BasicInterviewerEntity basicInterviewerEntity = interviewerDao.searchById(temp.getBusInterviewer());
                if (basicInterviewerEntity != null) {
                    temp.setBusName(basicInterviewerEntity.getName());
                    temp.setBusNamePse(basicInterviewerEntity.getPseudonym());
                }
            }
            if(temp.getTechInterviewer()!=null) {
                BasicInterviewerEntity basicInterviewerEntity2 = interviewerDao.searchById(temp.getTechInterviewer());
                if (basicInterviewerEntity2 != null) {
                    temp.setTechName(basicInterviewerEntity2.getName());
                    temp.setTechNamePse(basicInterviewerEntity2.getPseudonym());
                }
            }
        }
        return basicInterviewInfoEntities;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int add(TalentInterviewInfoAddForm talentInterviewInfoAddForm) {
        BasicInterviewInfoEntity basicInterviewInfoEntity = new BasicInterviewInfoEntity(
                talentInterviewInfoAddForm.getStatus(),
                talentInterviewInfoAddForm.getRemark(),
                null,
                talentInterviewInfoAddForm.getBusDate(),
                talentInterviewInfoAddForm.getBusInterviewerId(),
                talentInterviewInfoAddForm.getBusEvaluation(),
                talentInterviewInfoAddForm.getTechDate(),
                talentInterviewInfoAddForm.getTechInterviewerId(),
                talentInterviewInfoAddForm.getTechEvaluation(),
                talentInterviewInfoAddForm.getPicture()
        );
        basicInterviewInfoEntity.setCreateBy(SecurityUtils.getUsername());
        int row = interviewInfoDao.add(basicInterviewInfoEntity);
        RelatedTalentInterview relatedTalentInterview = new RelatedTalentInterview(talentInterviewInfoAddForm.getTalentId(), basicInterviewInfoEntity.getInterId());
        interviewInfoDao.addRelated(relatedTalentInterview);
        return row;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int edit(TalentInterviewInfoAddForm form) {
        BasicInterviewInfoEntity basicInterviewInfoEntity = new BasicInterviewInfoEntity(
                form.getStatus()==null ?null: form.getStatus(),
                form.getRemark()==null ?"": form.getRemark(),
                form.getInterId(),
                form.getBusDate(),
                form.getBusInterviewerId(),
                form.getBusEvaluation()==null ?"": form.getBusEvaluation(),
                form.getTechDate(),
                form.getTechInterviewerId(),
                form.getTechEvaluation()==null ?"": form.getTechEvaluation(),
                form.getPicture()
        );
        basicInterviewInfoEntity.setUpdateBy(SecurityUtils.getUsername());
        int row = interviewInfoDao.edit(basicInterviewInfoEntity);
        return row;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int remove(int[] interIds) {
        int row = interviewInfoDao.remove(interIds);
        //删除成功则还需要删除关联表
        if (row > 0) {
            for (int i : interIds) {
                interviewInfoDao.removeRelated(i);
            }
        }
        return row;
    }

    @Override
    public int addRelated(RelatedTalentInterview relatedTalentInterview) {
        return interviewInfoDao.addRelated(relatedTalentInterview);
    }
    @Override
    public int countInterviewedNumByDate( String startDate, String endDate){
        return interviewInfoDao.countInterviewedNumByDate(startDate,endDate);
    }
    @Override
    //统计时间内的采用回数
    public int countHiredNumByDate(String startDate, String endDate){
        return interviewInfoDao.countHiredNumByDate(startDate,endDate);
    }


}
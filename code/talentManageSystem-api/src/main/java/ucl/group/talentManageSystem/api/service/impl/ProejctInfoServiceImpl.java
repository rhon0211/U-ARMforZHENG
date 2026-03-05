package ucl.group.talentManageSystem.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucl.group.talentManageSystem.api.common.utils.SecurityUtils;
import ucl.group.talentManageSystem.api.controller.form.talentForm.TalentPorjectAddForm;
import ucl.group.talentManageSystem.api.db.dao.ProjectInfoDao;
import ucl.group.talentManageSystem.api.db.pojo.BasicProjectInfoEntity;
import ucl.group.talentManageSystem.api.db.pojo.RelatedTalentProject;
import ucl.group.talentManageSystem.api.service.ProjectInfoService;
import ucl.group.talentManageSystem.api.service.RedisService;

@Service
@Slf4j
public class ProejctInfoServiceImpl implements ProjectInfoService {

    @Autowired
    private ProjectInfoDao projectInfoDao;
    @Autowired
    private RedisService redisService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int add(TalentPorjectAddForm talentPorjectAddForm) {
        BasicProjectInfoEntity basicProjectInfoEntity = new BasicProjectInfoEntity(
                talentPorjectAddForm.getRemark(),
                 null,
                talentPorjectAddForm.getName(),
                talentPorjectAddForm.getAppointStartTime(),
                talentPorjectAddForm.getAppointEndTime(),
                talentPorjectAddForm.getAppointEvalution()
        );
        basicProjectInfoEntity.setCreateBy(SecurityUtils.getUsername());
        int row = projectInfoDao.add(basicProjectInfoEntity);
        RelatedTalentProject relatedTalentProject = new RelatedTalentProject(talentPorjectAddForm.getTalentId(), basicProjectInfoEntity.getProjectId());
        projectInfoDao.addRelated(relatedTalentProject);
        return row;
    }

    @Override
    public int edit(TalentPorjectAddForm form){
        BasicProjectInfoEntity basicProjectInfoEntity = new BasicProjectInfoEntity(
                form.getProjectId(),
                form.getName()==null ?"": form.getName(),
                form.getAppointStartTime(),
                form.getAppointEndTime(),
                form.getAppointEvalution()==null ?"": form.getAppointEvalution()
        );
        basicProjectInfoEntity.setUpdateBy(SecurityUtils.getUsername());
        int row = projectInfoDao.edit(basicProjectInfoEntity);
        return row;
    }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int remove(int[] projectIds){
            int row=projectInfoDao.remove(projectIds);
            //删除成功则还需要删除关联表
            if(row>0){
                for(int i:projectIds){
                    projectInfoDao.removeRelated(i);
                }
            }
            return row;
        }

    @Override
    public int addRelated(RelatedTalentProject relatedTalentProject) {
        return projectInfoDao.addRelated(relatedTalentProject);
    }

}
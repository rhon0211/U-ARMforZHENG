package ucl.group.talentManageSystem.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ucl.group.talentManageSystem.api.common.utils.SecurityUtils;
import ucl.group.talentManageSystem.api.db.dao.BusinessDao;
import ucl.group.talentManageSystem.api.db.pojo.BusinessEntity;
import ucl.group.talentManageSystem.api.service.BusinessService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

@Service
@Slf4j
public class BusinessServiceImpl implements BusinessService {
    @Resource
    BusinessDao businessDao;

    @Override
    @SuppressWarnings({"rawtypes"})
    public ArrayList<HashMap> BusinessSelect() {
        ArrayList<HashMap> list = businessDao.BusinessSelect();
        return list;
    }

    @Override
    public void BusinessInsert(BusinessEntity entity) {
        entity.setCreateBy(SecurityUtils.getUsername());
        businessDao.BusinessInsert(entity);
    }

    @Override
    public void BusinessUpdate(BusinessEntity entity) {
        entity.setUpdateBy(SecurityUtils.getUsername());
        businessDao.BusinessUpdate(entity);
    }
}

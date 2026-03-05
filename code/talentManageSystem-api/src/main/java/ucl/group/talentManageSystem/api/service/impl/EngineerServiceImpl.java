package ucl.group.talentManageSystem.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucl.group.talentManageSystem.api.common.constants.CacheConstants;
import ucl.group.talentManageSystem.api.common.utils.SecurityUtils;
import ucl.group.talentManageSystem.api.db.dao.EngineerDao;
import ucl.group.talentManageSystem.api.db.pojo.BasicEngineerEntity;
import ucl.group.talentManageSystem.api.service.EngineerService;
import ucl.group.talentManageSystem.api.service.RedisService;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EngineerServiceImpl implements EngineerService {

    @Autowired
    private EngineerDao engineerDao;
    @Autowired
    private RedisService redisService;

    @Override
    public List<BasicEngineerEntity> searchByTalentId(int talentId) {
        List<BasicEngineerEntity> engineerEntities = engineerDao.searchByTalentId(talentId);
        String userId = SecurityUtils.getUserIdAsString();
        String langName = redisService.getCacheObject(redisService.getLangCacheKey(userId));

        engineerEntities.forEach(
                temp -> {
                    //中文则不变
                    if (langName.equals(CacheConstants.Language_JA)) {
                        // 更新标签名为日文
                        temp.setEngineerName(temp.getEngineerNameJap());
                    }
                });
        return engineerEntities;
    }

    @Override
    public List<BasicEngineerEntity> searchAllEngineer() {
       return  engineerDao.searchAllEngineer();
    }
    @Override
    public List<String> searchNameByTalentId(int talentId){
        List<BasicEngineerEntity> engineerEntities = engineerDao.searchByTalentId(talentId);
        //String userId = StpUtil.getLoginIdAsString();
        String userId = SecurityUtils.getUserIdAsString();
        String langName = redisService.getCacheObject(redisService.getLangCacheKey(userId));
        List<String> nameList = new ArrayList<>();
        engineerEntities.forEach(temp -> {
            //中文则不变
            if (langName.equals(CacheConstants.Language_JA)) {
                // 更新标签名为日文
                nameList.add(temp.getEngineerNameJap());
            }else{
                nameList.add(temp.getEngineerName());
            }
        });
        return nameList;
    }

}
package ucl.group.talentManageSystem.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucl.group.talentManageSystem.api.common.constants.CacheConstants;
import ucl.group.talentManageSystem.api.common.utils.SecurityUtils;
import ucl.group.talentManageSystem.api.db.dao.DictDao;
import ucl.group.talentManageSystem.api.db.pojo.DictDataEntity;
import ucl.group.talentManageSystem.api.exception.ServiceException;
import ucl.group.talentManageSystem.api.service.DictService;
import ucl.group.talentManageSystem.api.service.RedisService;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class DictServiceImpl implements DictService {

    @Autowired
    private DictDao dictDao;
    @Autowired
    private RedisService redisService;
    @Override
    public List<DictDataEntity> searchByType(String type){
        List<DictDataEntity> dictDataEntities = dictDao.searchByType(type);
        if (dictDataEntities == null || dictDataEntities.size()==0) {
            // 如果为空，抛出ServiceException异常
            //未找到字典类型记录：
            throw new ServiceException("辞書タイプが見つかりませんでした: '" + type + "' ");
        }
        String userId = SecurityUtils.getUserIdAsString();
        String langName = redisService.getCacheObject(redisService.getLangCacheKey(userId));
        dictDataEntities.forEach(temp -> {
            //中文则不变
            if (langName.equals(CacheConstants.Language_JA)) {
                // 更新标签名为日文
                temp.setDictLabel(temp.getDictLabelJap());
            }
        });
        return dictDataEntities;
    }
    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init()
    {
        loadingDictCache();
    }

    /**
     * 加载参数缓存数据
     */
    @Override
    public void loadingDictCache()
    {
        List<String>  typeList=dictDao.searchAllDictType();
        for(String temp:typeList){
            List<DictDataEntity> dictDataEntityList = dictDao.searchByType(temp);
            redisService.setCacheObject(getCacheKey(temp), dictDataEntityList);
        }

    }
    private String getCacheKey(String configKey)
    {
        return CacheConstants.DICT_DATA_KEY + configKey;
    }
    /**
     * 清空参数缓存数据
     */
    @Override
    public void clearDictCache()
    {
        Collection<String> keys = redisService.keys(CacheConstants.DICT_DATA_KEY + "*");
        redisService.deleteObject(keys);
    }
    /**
     * 重置
     */
    @Override
    public void resetDictCache()
    {
        clearDictCache();
        loadingDictCache();
    }
    @Override
    public String   getDictName(String type, String lang,String value){
        List<DictDataEntity> dataEntityList = redisService.getCacheObject(getCacheKey(type));
        DictDataEntity matchedEntity = dataEntityList.stream()
                .filter(r -> r.getDictValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new ServiceException("字典中没有该元素"));

        if(lang.equals(CacheConstants.Language_JA)){
           return matchedEntity.getDictLabelJap();
        }else{
            return matchedEntity.getDictLabel();
        }
    }

}
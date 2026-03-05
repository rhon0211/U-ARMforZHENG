package ucl.group.talentManageSystem.api.service.impl;

import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ucl.group.talentManageSystem.api.common.PageUtils;
import ucl.group.talentManageSystem.api.common.utils.SecurityUtils;
import ucl.group.talentManageSystem.api.db.dao.LabelDao;
import ucl.group.talentManageSystem.api.db.dao.LabelTypeDao;
import ucl.group.talentManageSystem.api.db.pojo.LabelTypeEntity;
import ucl.group.talentManageSystem.api.service.LabelTypeService;
import ucl.group.talentManageSystem.api.service.RedisService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class LabelTypeServiceImpl implements LabelTypeService {

    @Resource
    LabelTypeDao labelTypeDao;

    /**
     * 为了插入关联表数据
     */
    @Resource
    LabelDao labelDao;

    @Resource
    private RedisService redisService;

    @Override
    public ArrayList<HashMap> LabelTypeSelect() {
        ArrayList<HashMap> list = labelTypeDao.LabelTypeSelect();
        if (!isZh()) {
            for (HashMap<String, Object> map : list) {
                Object name = map.get("name");
                if (name != null) {
                    // 将 name 的值设置为 nameJap 的值
                    Object nameJap = map.get("nameJap");
                    map.put("name", nameJap);
                }
            }
        }
        return list;
    }

    @Override
    public void LabelTypeInsert(LabelTypeEntity entity) {
        entity.setCreateBy(SecurityUtils.getUsername());
        labelTypeDao.LabelTypeInsert(entity);
    }

    @Override
    public void LabelTypeUpdate(LabelTypeEntity entity) {
        entity.setUpdateBy(SecurityUtils.getUsername());
        labelTypeDao.LabelTypeUpdate(entity);
    }

    @Override
    public String LabelTypeDeletes(Integer[] ids) {
        Integer labelIdCounts = labelTypeDao.LabelTypeCheckDelete(ids);
        if (labelIdCounts > 0) {
            return "不能被删除，因为关联多于一个的label_id。";
        }
        labelTypeDao.LabelTypeDeletes(ids);
        return null;
    }

    @Override
    public PageUtils LabelTypeSearchByPage(Map param) {
        ArrayList<HashMap> list = null;
        long count = labelTypeDao.LabelTypeSearchCount(param);
        if (count > 0) {
            list = labelTypeDao.LabelTypeSearchByPage(param);
        } else {
            list = new ArrayList<>();
        }
        if (!isZh()) {
            for (HashMap<String, Object> map : list) {
                Object typeName = map.get("typeName");
                if (typeName != null) {
                    // 将 typeName 的值设置为 typeNameJap 的值
                    Object typeNameJap = map.get("typeNameJap");
                    map.put("typeName", typeNameJap);
                }
            }
        }
        int page = MapUtil.getInt(param, "page");
        int length = MapUtil.getInt(param, "length");
        PageUtils pageUtils = new PageUtils(list, count, page, length);
        return pageUtils;
    }

    @Override
    public HashMap searchById(Integer typeId) {
        HashMap map = labelTypeDao.searchById(typeId);
        return map;
    }

    private Boolean isZh() {
        String userId = SecurityUtils.getUserIdAsString();
        String langName = redisService.getCacheObject(redisService.getLangCacheKey(userId));
        if (langName.equals("zh-CN")) return true;
        else return false;
    }

}

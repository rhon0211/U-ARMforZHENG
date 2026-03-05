package ucl.group.talentManageSystem.api.service.impl;

import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ucl.group.talentManageSystem.api.common.PageUtils;
import ucl.group.talentManageSystem.api.common.utils.SecurityUtils;
import ucl.group.talentManageSystem.api.db.dao.LabelDao;
import ucl.group.talentManageSystem.api.db.pojo.LabelEntity;
import ucl.group.talentManageSystem.api.db.pojo.RelatedLabelTypeEntity;
import ucl.group.talentManageSystem.api.service.LabelService;
import ucl.group.talentManageSystem.api.service.RedisService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Slf4j
public class LabelServiceImpl implements LabelService {

    @Resource
    private LabelDao labelDao;

    @Resource
    private RedisService redisService;

    @Override
    public HashMap labelSelect() {
        ArrayList<HashMap> list = labelDao.labelSelect();
        // 使用LinkedHashMap是为了保持插入顺序。
        LinkedHashMap map = new LinkedHashMap();
        for (HashMap one : list) {
            Integer typeId = MapUtil.getInt(one, "typeId");
            Integer labelId = MapUtil.getInt(one, "labelId");
            String typeName = MapUtil.getStr(one, "typeName");
            String labelName = MapUtil.getStr(one, "labelName");
            if (map.containsKey(typeName)) {
                ArrayList<HashMap> subList = (ArrayList<HashMap>) map.get(typeName);
                subList.add(new HashMap() {{
                    put("labelId", labelId);
                    put("labelName", labelName);
                }});
            } else {
                map.put(typeName, new ArrayList() {{
                    add(new HashMap() {{
                        put("labelId", labelId);
                        put("labelName", labelName);
                    }});
                }});
            }
        }

        return map;
    }

    @Override
    public HashMap searchById(Integer labelId) {
        HashMap map = labelDao.searchById(labelId);
        return map;
    }

    @Override
    public void LabelInsert(LabelEntity entity) {
        entity.setCreateBy(SecurityUtils.getUsername());
        labelDao.LabelInsert(entity);
        RelatedLabelTypeEntity entity1 = new RelatedLabelTypeEntity();
        entity1.setLabelId(entity.getLabelId());
        entity1.setTypeId(entity.getTypeId());
        labelDao.RelatedLabelInsert(entity1);
    }

    @Override
    public void LabelUpdate(LabelEntity entity) {
        entity.setUpdateBy(SecurityUtils.getUsername());
        labelDao.LabelUpdate(entity);
        if (entity.getTypeId() > 0) {
            RelatedLabelTypeEntity entity1 = new RelatedLabelTypeEntity();
            entity1.setLabelId(entity.getLabelId());
            System.out.println(entity.getLabelId());
            entity1.setTypeId(entity.getTypeId());
            labelDao.RelatedLabelUpdate(entity1);
        }
    }

    @Override
    public void LabelDeletes(Integer[] ids) {
        labelDao.LabelDeletes(ids);
    }

    @Override
    public PageUtils LabelSearchByPage(Map param) {
        ArrayList<HashMap> list = null;
        long count = labelDao.LabelSearchCount(param);
        if (count > 0) {
            list = labelDao.LabelSearchByPage(param);
        } else {
            list = new ArrayList<>();
        }
        if (!isZh()) {
            for (HashMap<String, Object> map : list) {
                Object typeName = map.get("typeName");
                Object labelName = map.get("labelName");
                if (typeName != null) {
                    // 将 typeName 的值设置为 typeNameJap 的值
                    Object typeNameJap = map.get("typeNameJap");
                    map.put("typeName", typeNameJap);
                    Object labelNameJap = map.get("labelNameJap");
                    map.put("labelName", labelNameJap);
                }
            }
        }
        int page = MapUtil.getInt(param, "page");
        int length = MapUtil.getInt(param, "length");
        PageUtils pageUtils = new PageUtils(list, count, page, length);
        return pageUtils;
    }

    private Boolean isZh() {
        String userId = SecurityUtils.getUserIdAsString();
        String langName = redisService.getCacheObject(redisService.getLangCacheKey(userId));
        if (langName.equals("zh-CN")) return true;
        else return false;
    }

}
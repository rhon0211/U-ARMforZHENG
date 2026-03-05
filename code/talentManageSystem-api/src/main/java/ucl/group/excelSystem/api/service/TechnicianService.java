package ucl.group.excelSystem.api.service;

import ucl.group.excelSystem.api.db.pojo.BasicTechnicianEntity;
import ucl.group.talentManageSystem.api.common.PageUtils;

import java.util.Map;
@SuppressWarnings({"rawtypes",})
public interface TechnicianService {

	void insertTech(BasicTechnicianEntity basicTechnicianEntity);

	PageUtils selectTechByPage(Map param);

	void updateTech(BasicTechnicianEntity basicTechnicianEntity);

	/**
	 * param ids:
	 * return void
	 * author he_jiale
	 * description 删除技术者，要删除任用里的表信息
	 * date 2024/8/9 14:28
	 */
	void deleteTech(Long[] ids);
}

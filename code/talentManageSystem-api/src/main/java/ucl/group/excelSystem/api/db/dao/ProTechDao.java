package ucl.group.excelSystem.api.db.dao;

import ucl.group.excelSystem.api.db.pojo.RelatedProjectTechnician;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface ProTechDao {

	void insertProTech(RelatedProjectTechnician relatedProjectTechnician);

	ArrayList<HashMap> selectProTechByPage(Map param);

	long selectProTechByPageCount(Map param);

	HashMap selectProTechForUpdateOrChange(long proTechId);

	RelatedProjectTechnician selectProTechForStopMonth(long proTechId);

	void updateProTech(RelatedProjectTechnician relatedProjectTechnician);

	void deleteProTech(Long[] ids);

	ArrayList<HashMap> selectTechnician();

	ArrayList<HashMap> selectProject(Long customerId);

	ArrayList<HashMap> selectCustomer();
}

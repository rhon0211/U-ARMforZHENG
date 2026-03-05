package ucl.group.excelSystem.api.db.dao;

import org.apache.ibatis.annotations.Param;
import ucl.group.excelSystem.api.controller.form.SaveTechnicianListForm;
import ucl.group.excelSystem.api.db.pojo.RelatedProjectTechnician;

import java.util.List;

public interface RelatedProjectTechnicianDao {
     List<RelatedProjectTechnician> searchBetweenStartAndEnd(@Param("dateStart") String dateStart,@Param("dateEnd") String dateEnd);

    int saveRemark(SaveTechnicianListForm form);

     RelatedProjectTechnician searchByProjectTechId(Long projectTechnicianId);

     List<RelatedProjectTechnician> searchByProjectId(Long projectId);

     List<RelatedProjectTechnician> searchByTechnicianId(Long technicianId);

    List<RelatedProjectTechnician> findAllById(@Param("projectTechnicianIds") List<Long> projectTechnicianIds);

}

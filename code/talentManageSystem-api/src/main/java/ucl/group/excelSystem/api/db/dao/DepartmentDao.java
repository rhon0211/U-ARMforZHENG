package ucl.group.excelSystem.api.db.dao;

import ucl.group.excelSystem.api.db.pojo.BasicDepartmentEntity;
import ucl.group.excelSystem.api.db.pojo.vo.DepartmentVo;

import java.util.HashMap;
import java.util.List;

public interface DepartmentDao {

    BasicDepartmentEntity getById(Integer departmentId);
    Integer getDepartmentCountByCompanyId(Integer companyId);

    List<DepartmentVo> getDepartmentListByCompanyId(Integer companyId);

    List<HashMap> selectDepartmentsByCompanyId(Integer companyId);

    List<HashMap> getDepartmentsByProjectIds(List<Integer> projectIds);

    List<Integer> getDepartmentIdsByProjectIds(List<Integer> projectIds);

    List<HashMap> getDepartmentsByCompanyId(Integer companyId);

    int checkdepartment(int departmentId);
    void deactivateDPProjects (Integer departmentId);
    void insertDepartment(BasicDepartmentEntity bean);
    void updateDepartment(BasicDepartmentEntity bean);

}

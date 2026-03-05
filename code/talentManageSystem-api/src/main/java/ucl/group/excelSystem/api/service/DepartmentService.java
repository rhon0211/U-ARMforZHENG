package ucl.group.excelSystem.api.service;

import ucl.group.excelSystem.api.db.pojo.BasicDepartmentEntity;
import ucl.group.excelSystem.api.db.pojo.vo.DepartmentVo;

import java.util.HashMap;
import java.util.List;
@SuppressWarnings({ "rawtypes",})
public interface DepartmentService {

    Integer getDepartmentCountByCompanyId(Integer companyId);

    List<DepartmentVo> getDepartmentListByCompanyId(Integer companyId);

    void insertDepartment(BasicDepartmentEntity bean);

    void updateDepartment(BasicDepartmentEntity bean);

    BasicDepartmentEntity getDepartmentById(Integer departmentId);

    List<HashMap> selectDepartmentsByCompanyId(Integer companyId);

    List<HashMap> getDepartmentsByProjectIds(List<Integer> projectIds);

    List<Integer> getDepartmentIdsByProjectIds(List<Integer> projectIds);

    List<HashMap> getDepartmentsByCompanyId(Integer companyId);
    boolean checkdepartment(int departmentId);
}

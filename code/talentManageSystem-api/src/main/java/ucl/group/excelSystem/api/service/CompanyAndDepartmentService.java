package ucl.group.excelSystem.api.service;

import com.github.pagehelper.PageInfo;
import ucl.group.excelSystem.api.db.pojo.BasicCompanyEntity;
import ucl.group.talentManageSystem.api.common.PageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({ "rawtypes"})
public interface CompanyAndDepartmentService {
    PageUtils getCompanyAndDepartmentByPage(Map<String, Object> param);

    void checkUnique(BasicCompanyEntity bean);
    void insertCompany(BasicCompanyEntity bean);

    void updateCompany(BasicCompanyEntity bean);

    BasicCompanyEntity getCompanyById(Integer companyId);

    Long getSaleCompanyCount();

    ArrayList<HashMap> selectSaleCompanyByPage(Map<String, Object> param);

    ArrayList<HashMap> selectSaleCompanyByDepartmentIdsByPage(List<Integer> departmentIds);

    PageInfo<BasicCompanyEntity> getCompanyAndDepartmentByPageHelper(Map<String, Object> param);

    ArrayList<HashMap> selectSaleCompanyByProjectIds(List<Integer> projectIds);
    boolean isCompanyProjectEmpty(int companyId);
}

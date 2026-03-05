package ucl.group.excelSystem.api.db.dao;

import org.apache.ibatis.annotations.Param;
import ucl.group.excelSystem.api.db.pojo.BasicCompanyEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CompanyAndDepartmentDao {
    long selectCompanyByPageCount();
    long selectSaleCompanyByPageCount();
    long selectProcurementCompanyByPageCount();
    long selectUclByPageCount();

    ArrayList<HashMap> selectCompanyByPage(Map<String, Object> param);
    ArrayList<HashMap> selectSaleCompanyByPage(Map<String, Object> param);
    ArrayList<HashMap> selectProcurementCompanyByPage(Map<String, Object> param);
    ArrayList<HashMap> selectUclByPage(Map<String, Object> param);
    ArrayList<HashMap> selectTradeCompanyByPage(Map<String, Object> param);
    ArrayList<HashMap> selectSaleCompanyByDepartmentIdsByPage(List<Integer> departmentIds);
    ArrayList<HashMap> selectUclDepartmentsByPage(List<Integer> departmentIds);
    ArrayList<HashMap> selectSaleCompanyByProjectIds(List<Integer> projectIds);

    void insertCompany(BasicCompanyEntity bean);
    void updateCompany(BasicCompanyEntity bean);
    void deactivateRelatedProjects(Integer companyId);

    BasicCompanyEntity getById(Integer companyId);
    List<BasicCompanyEntity> getAll();
    Long selectSaleCompanyByPageCount1();
    int hasActiveProjects(@Param("companyId") int companyId);


    // 通过 company_name 查找已存在的公司信息
    BasicCompanyEntity findByCompanyName(@Param("companyName") String companyName);

    // 通过 company_abbreviation 查找已存在的公司信息
    BasicCompanyEntity findByCompanyAbbreviation(@Param("companyAbbreviation") String companyAbbreviation);

    // 通过 company_name 查找已存在的公司信息（排除自身 company_id）
    BasicCompanyEntity findByCompanyNameExcludingId(@Param("companyName") String companyName, @Param("companyId") Integer companyId);

    // 通过 company_abbreviation 查找已存在的公司信息（排除自身 company_id）
    BasicCompanyEntity findByCompanyAbbreviationExcludingId(@Param("companyAbbreviation") String companyAbbreviation, @Param("companyId") Integer companyId);

}
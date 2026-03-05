package ucl.group.excelSystem.api.db.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface StaffManagementCompanyDao {
//    @Select({
//            "<script>",
//            "SELECT",
//            "    company_id AS companyId,",
//            "    company_abbreviation AS companyAbbreviation",
//            "FROM basic_v2_company_management",
//            "<where>",
//            "    <if test='param.companyName != null and param.companyName != \"\"'>",
//            "        AND company_name LIKE CONCAT('%', #{param.companyName}, '%')",
//            "    </if>",
//            "</where>",
//            "LIMIT #{param.length} OFFSET #{param.start}",
//            "</script>"
//    })
    List<HashMap> selectCompanyByPage(@Param("param") Map<String, Object> param);

//    @Select("SELECT COUNT(*) FROM basic_v2_company_management")
    public Long selectStaffCompanyByPageCount();

}

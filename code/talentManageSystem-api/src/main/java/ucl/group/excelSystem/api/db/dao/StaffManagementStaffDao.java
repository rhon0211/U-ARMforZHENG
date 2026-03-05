package ucl.group.excelSystem.api.db.dao;

import org.apache.ibatis.annotations.*;
import ucl.group.excelSystem.api.db.pojo.BasicCompanyEntity;
import ucl.group.excelSystem.api.db.pojo.BasicStaffEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface StaffManagementStaffDao {
    @Select({
            "SELECT",
            "   staff_id AS staffId,",
            "   staff_name_kanji AS staffNameKanji,",
            "   staff_name_furikana AS staffNameFurikana,",
            "   latest_evaluation AS latestEvaluation",
            "FROM basic_v2_company_staff ",
            "WHERE company_id = #{companyId} AND active_flg = 1"

    })
    List<HashMap> selectStaffByCompanyId(@Param("companyId") Long companyId);
    @Select({
            "<script>",
            "SELECT",
            "    staff_name_kanji AS staffNameKanji",
            "FROM",
            "    basic_v2_company_staff",
            "</script>"
    })
    ArrayList<HashMap> selectStaff();
    @Update({
            "<script>",
            "UPDATE basic_v2_company_staff ",
            "SET active_flg = 0 ",
            "WHERE staff_id IN ",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
     void deleteStaffById(@Param("ids") List<Integer> staffids);
    @Insert({
            "INSERT INTO basic_v2_company_staff",
            "(company_id,photo_url,staff_name_kanji,staff_name_furikana,staff_name_roma,",
            "staff_birthday,sales_representitive,operation_start_date,operation_end_date,latest_evaluation,active_flg,comment)",
            "VALUES",
            "(#{companyId}, #{photoUrl}, #{staffNameKanji}, ",
            "#{staffNameFurikana}, #{staffNameRoma}, #{staffBirthday}, ",
            "#{salesRepresentitive}, #{operationStartDate}, #{operationEndDate}, ",
            "#{latestEvaluation}, #{activeFlg}, #{commit})"
    })
     void addStaff(BasicStaffEntity basicStaffEntity);
    @Update({
            "UPDATE basic_v2_company_staff ",
            "SET photo_url = #{photoUrl}, ",
            "company_id = #{companyId},",
            "staff_name_kanji = #{staffNameKanji}, ",
            "staff_name_furikana = #{staffNameFurikana}, ",
            "staff_name_roma = #{staffNameRoma}, ",
            "staff_birthday = #{staffBirthday}, ",
            "sales_representitive = #{salesRepresentitive}, ",
            "operation_start_date = #{operationStartDate}, ",
            "operation_end_date = #{operationEndDate}, ",
            "latest_evaluation = #{latestEvaluation}, ",
            "active_flg = #{activeFlg}, ",
            "comment = #{commit} ",
            "WHERE staff_id = #{staffId}"
    })
     void updateStaff(BasicStaffEntity basicStaffEntity);


    BasicStaffEntity getStaffById(Integer staffId);

    List<Integer> getStaffIds();

    String getNameById(Integer id);

    List<BasicCompanyEntity> getCompanys();
//    @Select("SELECT COUNT(1) FROM basic_v2_project_detail WHERE staff_id = #{staffId} AND project_id IS NOT NULL AND active_flg = 1")
    int checkStaffHasActiveProject(int staffId);

    Integer getProcurementCompanyIdByStaffId(Integer staffId);

//    @Delete("DELETE FROM basic_v2_company_staff WHERE staff_id = #{staffId} AND NOT EXISTS (SELECT 1 FROM basic_v2_project_detail WHERE staff_id = #{staffId} AND project_id IS NOT NULL AND active_flg = 1)")
//    int deleteStaffIfNoActiveProject(int staffId);
void setLaborInactiveFromMonth(@Param("staffId") Integer staffId, @Param("fromMonth") String fromMonth);

}

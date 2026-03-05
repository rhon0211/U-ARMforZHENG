package ucl.group.excelSystem.api.db.dao;

import org.apache.ibatis.annotations.Param;
import ucl.group.excelSystem.api.db.pojo.BasicMonthEntity;

import java.time.LocalDate;
import java.util.List;

/**
 * projectName: code
 * package: ucl.group.excelSystem.api.db.dao
 * className: MonthDao
 * author: he_jiale
 * description: TODO
 * date: 2024/07/10 16:03
 * version: 1.0
 */
public interface MonthDao {
     List<BasicMonthEntity> searchByProjectTechIdAndYearMonths(@Param("projectTechnicianId") Long projectTechnicianId,@Param("yearMonths") List<String> yearMonths);
     int add(BasicMonthEntity basicMonthEntity);
     int save(BasicMonthEntity basicMonthEntity);
     int savePresumedTime(BasicMonthEntity basicMonthEntity);
     int saveExpectedPrice(BasicMonthEntity basicMonthEntity);
     int saveActualHours(BasicMonthEntity basicMonthEntity);
     int saveFrom(BasicMonthEntity basicMonthEntity);
     int saveActualPrice(BasicMonthEntity basicMonthEntity);
     List<BasicMonthEntity> searchByYearMonth(LocalDate yearMonth);
     BasicMonthEntity searchById(Long monthId);
     int editTotalAndDays(BasicMonthEntity basicMonthEntity);
     BasicMonthEntity searchByProjectTechIdAndYearMonth(@Param("projectTechnicianId") Long projectTechnicianId,@Param("localDate") LocalDate localDate);
     List<BasicMonthEntity> searchByProjectTechId(Long projectTechnianId);
     Integer searchMonthDays(@Param("localDate") LocalDate localDate);
     int remove(Long[] ids);
     int modifyTotalNumber(@Param("totalNumber") int totalNumber,@Param("localDate") LocalDate localDate);

    //批量更新
    void saveAll(@Param("basicMonthEntities") List<BasicMonthEntity> basicMonthEntities);
}

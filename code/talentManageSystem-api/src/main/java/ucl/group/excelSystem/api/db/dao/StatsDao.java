package ucl.group.excelSystem.api.db.dao;

import ucl.group.excelSystem.api.db.pojo.BasicStatsEntity;

import java.sql.Date;
import java.util.List;

/**
 * projectName: code
 * package: ucl.group.excelSystem.api.db.dao
 * className: StatsDao
 * author: he_jiale
 * description: year_month因为是数据库专用字段，编写sql时必须用``框住
 * date: 2024/07/08 14:27
 * version: 1.0
 */
public interface StatsDao {
     int addStats(BasicStatsEntity basicStatsEntity);
     int modifyStats(BasicStatsEntity basicStatsEntity);
     BasicStatsEntity searchByYearMonth(String yearMonth);
     int edit(BasicStatsEntity basicStatsEntity);
     int countByYearMonth(List<String> yearMonths);
     Date selectYearMonth();
     void updateStatsByYearMonth(BasicStatsEntity basicStatsEntity);
}

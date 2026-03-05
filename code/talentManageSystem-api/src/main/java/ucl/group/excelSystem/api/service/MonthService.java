package ucl.group.excelSystem.api.service;

import ucl.group.excelSystem.api.controller.form.SaveProjectTechnicianListForm;
import ucl.group.excelSystem.api.db.pojo.BasicMonthEntity;
import ucl.group.excelSystem.api.db.pojo.vo.MonthDataListVO;
import ucl.group.excelSystem.api.db.pojo.vo.ProjectTechnicianColumn;

import java.time.LocalDate;
import java.util.List;

/**
 * projectName: code
 * package: ucl.group.excelSystem.api.service
 * className: MonthService
 * author: he_jiale
 * description: TODO
 * date: 2024/07/10 16:09
 * version: 1.0
 */

public interface MonthService {
    /**
     * param projectTechnicianId:
     * return List<MonthDataListVO>
     * author he_jiale
     * description 用于全体项目管理表的monthData生成，根据日期对每个projectTechId生成月份数据列表
     * 如果不存在某期数据，则插入数据库
     * date 2024/07/10 16:17
     */
     List<MonthDataListVO> getMonthDataList(Long projectTechnicianId, LocalDate dateStart, LocalDate dateEnd);

    /**
     * param basicMonthEntities:
     * return void
     * author he_jiale
     * description 增加月份数据，一般都能用
     * date 2024/07/12 10:36
     */
     void addMonthData(List<BasicMonthEntity> basicMonthEntities);

    /**
     * param dateStart:
     * param dateEnd:
     * return List<ProjectTechnicianColumn>
     * author he_jiale
     * description 获取日期范围内的月份列信息数据，用于全体项目管理表
     * date 2024/07/10 16:21
     */
     List<ProjectTechnicianColumn> getColumnData(LocalDate dateStart, LocalDate dateEnd);

    /**
     * param form:
     * return void
     * author he_jiale
     * description 用于修改保存全体项目管理表
     * date 2024/07/12 10:35
     */
     void saveMonthData(SaveProjectTechnicianListForm form);
    /**
     * param form:
     * return void
     * author he_jiale
     * description 用于修改全体项目管理表中的每个字段，需要计算并且覆盖，注意实际时间和乖离的特殊计算方式
     * date 2024/09/17 17:35
     */
     void saveAllItems(SaveProjectTechnicianListForm form);
    /**
     * param form:
     * return void
     * author he_jiale
     * description 用于修改全体项目管理表中的每个月的工作天数
     * date 2024/07/12 10:35
     */
     void saveMonthDays(SaveProjectTechnicianListForm form);
    /**
     * param form:
     * return void
     * author he_jiale
     * description 用于修改保存全体项目管理表中的想定单价
     * date 2024/07/12 10:35
     */
     void saveExpectedPrice(SaveProjectTechnicianListForm form);
    /**
     * param form:
     * return void
     * author he_jiale
     * description 用于修改保存全体项目管理表中的实际时间
     * date 2024/07/12 10:35
     */
     void saveActualHours(SaveProjectTechnicianListForm form);
    /**
     * param form:
     * return void
     * author he_jiale
     * description 用于修改保存全体项目管理表中的乖离
     * date 2024/07/12 10:35
     */
     void saveFrom(SaveProjectTechnicianListForm form);
    /**
     * param form:
     * return void
     * author he_jiale
     * description 用于修改保存全体项目管理表中的实际价格
     * date 2024/07/12 10:35
     */
     void saveActualPrice(SaveProjectTechnicianListForm form);

    /**
     * param tempYearMonth:
     * param dateStart:
     * param dateEnd:
     * return int
     * author he_jiale
     * description 统计某月的总工作人数，用于计算totalNumber
     * date 2024/07/12 10:35
     */
     int getTotalNumByMonth(String tempYearMonth, LocalDate dateStart, LocalDate dateEnd);

    /**
     * param projectTechnicianId:
     * param localDate:
     * return BasicMonthEntity
     * author he_jiale
     * description 用于作业管理表获取每个projectTechnicianId的localDate时候的month数据
     * date 2024/07/12 15:18
     */
     BasicMonthEntity searchByProjectTechIdAndYearMonth(Long projectTechnicianId, LocalDate localDate);
    /**
     * param projectTechnicianId:
     * return List<BasicMonthEntity>
     * author he_jiale
     * description 根据proTechid拿到所有的monthId
     * date 2024/8/9 16:31
     */
     List<BasicMonthEntity> searchByProjectTechId(Long projectTechnicianId);
     void edit(List<BasicMonthEntity> basicMonthEntities);
    /**
     * Description: [dateStart, dateEnd] 刷新month表的人数
     * Param: [dateStart, dateEnd]
     * return: void
     * Author: he_jiale
     * Date: 14:37 2024/9/18
     */
     void flushTotalNumber(LocalDate dateStart,LocalDate dateEnd);



}

package ucl.group.excelSystem.api.service;

import ucl.group.excelSystem.api.controller.form.SaveProjectTechnicianListForm;
import ucl.group.excelSystem.api.controller.form.SaveTechnicianListForm;
import ucl.group.excelSystem.api.db.pojo.vo.*;

import java.time.LocalDate;
import java.util.List;

public interface TableService {

    /**
     * 根据日期范围，查询所有记录
     * 如果开始时间在去年，契约开始时间以dateStart为准
     * 如果结束时间在明年，契约结束时间以dateEnd为准
     *如果契约结束时间有，退场月没有，显示区间
     * 如果契约结束时间有或者没有，退场月有，显示退场月的上一个月
     * param dateStart
     * param dateEnd
     * return
     */
     List<TechnicianListVO> searchTechnicianList(LocalDate dateStart, LocalDate dateEnd);

    /**
     * param technicianListVOS:
     * return void
     * author he_jiale
     * description 根据顾客、项目、所属公司名称排序技术者一览
     * date 2024/07/08 11:05
     */
     void sortTechnicianList(List<TechnicianListVO> technicianListVOS);

    /**
     * param forms:
     * return void
     * author he_jiale
     * description 保存技术者一览中的备注信息
     * date 2024/07/08 12:01
     */
     void saveTechnicianList(List<SaveTechnicianListForm> forms);

    /**
     * param dateStart:
     * param dateEnd:
     * return List<TechnicianListStatsVO>
     * author he_jiale
     * description 查询技术者一览统计信息
     * date 2024/07/09 14:15
     */
     List<TechnicianListStatsVO> searchTechnicianStatsList(LocalDate dateStart, LocalDate dateEnd);

    /**
     * param technicianListVOS:
     * param companyName:
     * return int
     * author he_jiale
     * description 统计列表中的UCL或者BP公司的总人数
     * date 2024/07/12 10:31
     */
     int countTechniciansByCompany(List<TechnicianListVO> technicianListVOS, String companyName);

    /**
     * param technicianListStatsVOS:
     * return void
     * author he_jiale
     * description 保存技术者一览统计信息
     * date 2024/07/08 17:59
     */
     void saveTechnicianListStats(List<TechnicianListStatsVO> technicianListStatsVOS);

    /**
     * param dateStart:
     * param dateEnd:
     * return ProjectTechnicianVO
     * author he_jiale
     * description 查询时间范围内的全体项目管理情况，根据cBeginMonth,cEndMonth确定有数据的月份
     * date 2024/07/09 16:36
     */
     ProjectTechnicianVO searchProjectTechnicianList(LocalDate dateStart, LocalDate dateEnd);

    /**
     * param projectTechnicianRows:
     * return void
     * author he_jiale
     * description 按照顾客名、项目名、所属公司给、技术者id、创建时间排序
     * 如果 o1 的客户名称比 o2 的客户名称小，那么 o1 会排在 o2 前面，反之则 o2 会排在 o1 前面
     * 基于每个字符的ASCII码值
     * 创建时间小的在前面
     * date 2024/07/11 10:47
     */
     void sortProjectTechnicianList(List<ProjectTechnicianRow> projectTechnicianRows);

    /**
     * param form:
     * return void
     * author he_jiale
     * description 修改保存某年某期的全体项目管理表
     * date 2024/07/11 17:36
     */
     void saveProjectTechnicianList(SaveProjectTechnicianListForm form);
    /**
     * param localDate:
     * param customerId:
     * return List<ProjectMonthListVO>
     * author he_jiale
     * description 作业管理表的返回结果
     * date 2024/07/12 15:36
     */
     List<ProjectMonthListVO> searchProjectMonthList(LocalDate localDate,Long customerId);
    /**
     * param form:
     * return void
     * author he_jiale
     * description 作业管理表的保存
     * 因前端逻辑问题，后端需要修改，在这里如下解释：
     * 正确的表头以countCustomer不为0的记录为准
     * 正确的内容以countProject不为0的记录为准
     * date 2024/07/17 11:07
     */
     void saveProjectMonthList(List<ProjectMonthListVO> form);
    /**
    * Description: [dateStart, dateEnd] 刷新stats表的人数和比率
    * Param: [dateStart, dateEnd]
    * return: void
    * Author: he_jiale
    * Date: 14:37 2024/9/18
    */
     void flushStats(LocalDate dateStart,LocalDate dateEnd);

     void flushMonthData(SaveProjectTechnicianListForm form);
}

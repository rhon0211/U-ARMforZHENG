package ucl.group.excelSystem.api.common.utils;

import ucl.group.excelSystem.api.db.pojo.RelatedProjectTechnician;
import ucl.group.excelSystem.api.db.pojo.vo.TechnicianListVO;
import ucl.group.talentManageSystem.api.common.utils.DateUtils;
import ucl.group.talentManageSystem.api.common.utils.StringUtils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * projectName: code
 * package: ucl.group.excelSystem.api.common.utils
 * className: TableUtils
 * author: he_jiale
 * description: 表格生成的一些工具类
 * date: 2024/07/10 16:22
 * version: 1.0
 */
public class TableUtils {


    /**
     * param strDate:
     * return int
     * author he_jiale
     * description 根据yyyy-MM形式的字符串得到1-6
     * date 2024/07/10 16:02
     */
    public static int judgeStrMonNum(String strDate){
        if(StringUtils.isEmpty(strDate)){
            return -1;
        }
        LocalDate localDate= DateUtils.toLocalDateFirstDay(strDate);
        int month = localDate.getMonthValue();
        if (month >= 3 && month <= 8) {
            // 3->1, 4->2, 5->3, 6->4, 7->5, 8->6
            return month - 2;
        } else {
            // 9, 10, 11, 12, 1, 2 -> 9->1, 10->2, 11->3, 12->4, 1->5, 2->6
            return (month <= 2) ? month + 4 : month - 8;
        }
    }

    /**
     * param localDate:
     * return int
     * author he_jiale
     * description 根据localDate获取1-6
     * date 2024/07/10 15:28
     */
    public static int judgeMonthNum(LocalDate localDate){
        if(localDate==null){
            return -1;
        }
        int month = localDate.getMonthValue();
        if (month >= 3 && month <= 8) {
            // 3->1, 4->2, 5->3, 6->4, 7->5, 8->6
            return month - 2;
        } else {
            // 9, 10, 11, 12, 1, 2 -> 9->1, 10->2, 11->3, 12->4, 1->5, 2->6
            return (month <= 2) ? month + 4 : month - 8;
        }
    }
    /**
     * param localDate:
     * return int
     * author he_jiale
     * description 获取原始数字，用于比较大小
     * date 2024/7/25 15:04
     */
    public static int judgeMonthOriginNum(LocalDate localDate){
        if(localDate==null){
            return -1;
        }
        int month = localDate.getMonthValue();
        return month;
    }
    /**
     * param dateStart:
     * param dateEnd:
     * return List<String>
     * author he_jiale
     * description 计算两个年月之间有哪些月份（以月份表示，2024-03）
     * date 2024/07/08 12:37
     */
    public static List<String> getYearMonthsBetween(LocalDate dateStart, LocalDate dateEnd) {
        List<String> yearMonths = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        //变成年月的形式
        YearMonth start = YearMonth.from(dateStart);
        YearMonth end = YearMonth.from(dateEnd);

        while (!start.isAfter(end)) {
            yearMonths.add(start.format(formatter));
            start = start.plusMonths(1);
        }

        return yearMonths;
    }
    /**
     * param dateStart:
     * param dateEnd:
     * return List<String>
     * author he_jiale
     * description 计算两个年月之间有哪些月份天数（以天数表示，2024-03-01）
     * date 2024/07/08 12:37
     */
    public static List<String> getYearMonthDaysBetween(LocalDate dateStart, LocalDate dateEnd) {
        List<String> yearMonths = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 确保从dateStart的第一天开始
        LocalDate current = dateStart;
        while (!current.isAfter(dateEnd)) {
            yearMonths.add(current.format(formatter));
            current = current.plusMonths(1);
        }
        return yearMonths;
    }
    /**
     * param technicianListVOS:
     * param month:yyyy-MM
     * return List<TechnicianListVO>
     * author he_jiale
     * description 获取给定日期仍在工作的的技术者列表，用于统计stats
     * date 2024/07/08 12:49
     */
    public static  List<TechnicianListVO> getTechniciansInRange(List<TechnicianListVO> technicianListVOS, String month) {
        List<TechnicianListVO> count = technicianListVOS.stream()
                    .filter(record -> isOverlapping(record, DateUtils.toLocalDateFirstDay(month)))
                    .collect(Collectors.toList());
        return count;
    }

    /**
     * param record:
     * param tempMonth:
     * return boolean
     * author he_jiale
     * description 判断记录与tempMonth是否有交集
     * date 2024/07/12 10:28
     */
    public static boolean isOverlapping(TechnicianListVO record, LocalDate tempMonth) {
        LocalDate tempStart =DateUtils.toLocalDateFirstDay( record.getcBeginMonth());
        LocalDate tempEnd = DateUtils.toLocalDateFirstDay( record.getcEndMonth());
        if(!StringUtils.isEmpty(record.getStopMonth())){
            // 判断 tempMonth 是否在 tempStart 和 tempEnd 之间，不包括tempEnd本身
            tempEnd= DateUtils.toLocalDateFirstDay( record.getStopMonth());
            return !tempMonth.isBefore(tempStart) && tempMonth.isBefore(tempEnd);
        }else {
            // 判断 tempMonth 是否在 tempStart 和 tempEnd 之间，包括tempEnd本身
            return !tempMonth.isBefore(tempStart) && !tempMonth.isAfter(tempEnd);
        }


    }
    /**
     * param relatedProjectTechnicians:
     * param dateStart:
     * param dateEnd:
     * return List<TechnicianListVO>
     * author he_jiale
     * description 确定新的cBeginMonth、cEndMonth、stopMonth
     * date 2024/07/10 15:33
     */
    public static List<TechnicianListVO> changeStartAndEndMonth(List<RelatedProjectTechnician> relatedProjectTechnicians, LocalDate dateStart, LocalDate dateEnd){
        List<TechnicianListVO> technicianListVOS=new ArrayList<>();
        for (RelatedProjectTechnician tempR : relatedProjectTechnicians) {
            TechnicianListVO technicianListVO = new TechnicianListVO();

            //确定真正的cBeginMonth和cEndMonth
            if(tempR.getCBeginMonth().isBefore(dateStart)){
                //beginMonth在dateStart前,以dateStart为准
                technicianListVO.setcBeginMonth(DateUtils.localDateToStr(dateStart,"yyyy-MM"));
            }else{
                technicianListVO.setcBeginMonth(DateUtils.localDateToStr(tempR.getCBeginMonth(),"yyyy-MM"));
            }
            if(tempR.getCEndMonth()==null){
                technicianListVO.setcEndMonth(DateUtils.localDateToStr(dateEnd, "yyyy-MM"));
            }else {
                if (dateEnd.isBefore(tempR.getCEndMonth())) {
                    //dateEnd在cEndMonth前，以dateEnd为准
                    technicianListVO.setcEndMonth(DateUtils.localDateToStr(dateEnd, "yyyy-MM"));
                } else {
                    technicianListVO.setcEndMonth(DateUtils.localDateToStr(tempR.getCEndMonth(), "yyyy-MM"));
                }
            }
            technicianListVO.setStopMonth(tempR.getStopMonth()!=null? DateUtils.localDateToStr(tempR.getStopMonth(),"yyyy-MM"):"");
            technicianListVO.setTechnicianId(tempR.getTechnicianId());
            technicianListVOS.add(technicianListVO);
        }
        return technicianListVOS;
    }
    /**
     * param relatedProjectTechnicians:
     * return List<RelatedProjectTechnician>
     * author he_jiale
     * description 根据parent_id,去掉重复的技术者，仅用于技术者一览，并且退场时间、结束时间以最新的为准
     * date 2024/07/11 11:56
     */
    public static List<RelatedProjectTechnician> deduplicateRelatedProTech(List<RelatedProjectTechnician> relatedProjectTechnicians){
        // 创建一个Map来存储id到RelatedProjectTechnician的映射
        Map<Long, RelatedProjectTechnician> technicianMap = new HashMap<>();
        for (RelatedProjectTechnician tech : relatedProjectTechnicians) {
            technicianMap.put(tech.getProjectTechnicianId(), tech);
        }

        // 创建一个List来存储结果
        List<RelatedProjectTechnician> result = new ArrayList<>();

        for (RelatedProjectTechnician tech : relatedProjectTechnicians) {
            if (tech.getParentId() == 0) {
                RelatedProjectTechnician lastChild = findLastChildNode(tech, technicianMap);
                if (lastChild == null || lastChild.getProjectTechnicianId().equals(tech.getProjectTechnicianId())) {
                    // 没有子节点或本身就是最后一个节点，添加自己
                    result.add(tech);
                } else {
                    // 添加最后一个子节点
                    result.add(lastChild);
                }
            }
        }

        return result;
    }
    /**
     * param tech:
     * param technicianMap:
     * return RelatedProjectTechnician
     * author he_jiale
     * description 寻找所传tech的子结点，仅用于技术者一览表
     * date 2024/07/12 10:29
     */
    public static  RelatedProjectTechnician findLastChildNode(RelatedProjectTechnician tech, Map<Long, RelatedProjectTechnician> technicianMap) {
        RelatedProjectTechnician lastChild = null;
        for (RelatedProjectTechnician child : technicianMap.values()) {
            if (child.getParentId().equals(tech.getProjectTechnicianId())) {
                lastChild = child;
                RelatedProjectTechnician deeperChild = findLastChildNode(child, technicianMap);
                if (deeperChild != null) {
                    lastChild = deeperChild;
                }
            }
        }
        if(lastChild!=null) {
            //修改为父结点的cBeginMonth
            lastChild.setCBeginMonth(tech.getCBeginMonth());
        }
        return lastChild;
    }

    /**
     * 获取指定日期的下下年2月1日的日期
     * param date 输入日期，格式为"yyyy-MM-dd"
     * return 下下年2月1日的日期，格式为"yyyy-MM-dd"
     * deprecated 已弃用
     */
    public static LocalDate getNextNextYearFebruary(LocalDate date) {
        // 获取date年份
        int year = date.getYear();
        // 获取date月份
        int month = date.getMonthValue();

        // 如果月份是2月或之后，则获取下下年2月1日，否则获取下一年2月1日
        LocalDate resultDate;
        if (month >= 2) resultDate = LocalDate.of(year + 2, 2, 1);
        else resultDate = LocalDate.of(year + 1, 2, 1);
        return resultDate;
    }

}

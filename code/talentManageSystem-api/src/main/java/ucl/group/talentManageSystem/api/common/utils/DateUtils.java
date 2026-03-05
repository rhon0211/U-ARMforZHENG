package ucl.group.talentManageSystem.api.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author hejiale
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils
{
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    /**
     * 获取特定格式的当前日期
     * @param format
     * @return
     */
    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }
    /**
     * 日期路径 即年-月-日 如2018-08-08
     * 用于人才模块
     */
    public static final String dateTimeTalent()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, YYYY_MM_DD);
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算时间差
     *
     * @param endDate 最后时间
     * @param startTime 开始时间
     * @return 时间差（天/小时/分钟）
     */
    public static String timeDistance(Date endDate, Date startTime)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startTime.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor)
    {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor)
    {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }
    /**
     * @param str:
     * @param format:
     * @return LocalDate
     * @author he_jiale
     * @description 将str以"xxxx-XX"格式转为localDate
     * @date 2024/07/05 18:01
     */
    public static LocalDate strToLocalDate(String str,String format){
        return LocalDate.parse(str, DateTimeFormatter.ofPattern(format));
    }
    /**
     * @param date:
     * @param pattern:
     * @return String
     * @author he_jiale
     * @description 将localDate以"xxxx-XX"格式转为str
     * @date 2024/07/09 14:55
     */
    public static String localDateToStr(LocalDate date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(formatter);
    }
    /**
     * @param str:
     * @return LocalDate
     * @author he_jiale
     * @description 获取该yyyy-MM或者yyyy-MM-dd形式的字符串的当月第一天localdate
     * @date 2024/07/08 15:27
     */
    public static   LocalDate toLocalDateFirstDay(String str){
        // 定义两个日期格式
        DateTimeFormatter formatterYearMonth = DateTimeFormatter.ofPattern("yyyy-MM");
        DateTimeFormatter formatterYearMonthDay = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            if (str.length() == 7) {
                // 解析 yyyy-MM 格式的字符串
                YearMonth yearMonth = YearMonth.parse(str, formatterYearMonth);
                return yearMonth.atDay(1);
            } else if (str.length() == 10) {
                // 解析 yyyy-MM-dd 格式的字符串
                return LocalDate.parse(str, formatterYearMonthDay);
            } else {
                throw new IllegalArgumentException("Invalid date format, expected yyyy-MM or yyyy-MM-dd");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + str, e);
        }
    }
    /**
     * @param str:
     * @return LocalDate
     * @author he_jiale
     * @description 获取该string的当月最后一天
     * @date 2024/07/08 15:27
     */
    public static   LocalDate toLocalDateLastDay(String str){
        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        // 解析字符串为 YearMonth 对象
        YearMonth yearMonth = YearMonth.parse(str, formatter);
        // 返回 YearMonth 对象的第一个日期
        return yearMonth.atEndOfMonth();
    }
    /**
     * @param :
     * @return LocalDate
     * @author he_jiale
     * @description 获取系统日期当月第一个天数
     * @date 2024/07/10 10:24
     */
    public static   LocalDate toLocalDateNowDay(){
        // 解析字符串为 YearMonth 对象
        YearMonth yearMonth = YearMonth.now();
        // 返回 YearMonth 对象的第一个日期
        return yearMonth.atDay(1);
    }
    /**
     * @param :
     * @return LocalDate
     * @author he_jiale
     * @description 获取系统日期上个月第一个天数
     * @date 2024/07/10 10:24
     */
    public static   LocalDate toLocalDateBefNowDay(){
        // 解析字符串为 YearMonth 对象
        YearMonth yearMonth = YearMonth.now();
        // 返回 YearMonth 对象的第一个日期
        return yearMonth.minusMonths(1).atDay(1);
    }
    /**
     * @param date:
     * @return LocalDate
     * @author he_jiale
     * @description 获取所传date（2024-01-01）的上一个月的第一天
     * @date 2024/07/12 10:24
     */
    public static LocalDate toLocalDateBefDay(LocalDate date){
        YearMonth yearMonth =YearMonth.from(date);
        // 返回 YearMonth 对象的上一个月的第一个日期
        return yearMonth.minusMonths(1).atDay(1);
    }
    /**
     * @param date:
     * @return LocalDate
     * @author he_jiale
     * @description 获取所传date（2024-01-01）的下一个月的第一天
     * @date 2024/07/12 10:24
     */
    public static LocalDate toLocalDateAftDay(LocalDate date){
        YearMonth yearMonth =YearMonth.from(date);
        // 返回 YearMonth 对象的下一个月的第一个日期
        return yearMonth.plusMonths(1).atDay(1);
    }


}

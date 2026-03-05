package ucl.group.talentManageSystem.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucl.group.talentManageSystem.api.common.utils.StringUtils;
import ucl.group.talentManageSystem.api.db.dao.InterviewInfoDao;
import ucl.group.talentManageSystem.api.db.pojo.bo.MonthAndInterviewer;
import ucl.group.talentManageSystem.api.service.TimeViewService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class TimeViewServiceImpl implements TimeViewService {
    @Autowired
    private InterviewInfoDao interviewInfoDao;

    @Override
    public Integer countInterviewedNumByDate(String starDate, String endDate) {
        return interviewInfoDao.countInterviewedNumByDate(starDate, endDate);
    }

    @Override
    public Integer countHiredNumByDate(String starDate, String endDate) {
        return interviewInfoDao.countHiredNumByDate(starDate, endDate);
    }


    @Override
    public List<MonthAndInterviewer> getDateRanges(String start, String end) throws ParseException {
        //start 2024-05 ; end 2024-06
        List<String> dateRanges = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat outputSdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        if(StringUtils.isEmpty(start) && StringUtils.isEmpty(end)){
            // 设置 end 为当月
            end = sdf.format(cal.getTime());
            // 设置 start 为 6 个月前
            cal.add(Calendar.MONTH, -5);
            start = sdf.format(cal.getTime());
        }
        Date startDate = sdf.parse(start);
        Date endDate = sdf.parse(end);

        //startCal 的日期设置为当月的最后一天
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        startCal.set(Calendar.DAY_OF_MONTH, 1);
        //endCal 的日期设置为当月的最后一天，使用 getActualMaximum(Calendar.DAY_OF_MONTH) 获取当月的最大天数。
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        endCal.set(Calendar.DAY_OF_MONTH, endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
        List<MonthAndInterviewer> monthAndInterviewerList = new ArrayList<>();
        while (!startCal.after(endCal)) {
            //获取 startCal 所在月份的最大天数。
            int maxDay = startCal.getActualMaximum(Calendar.DAY_OF_MONTH);
            //表示当月的第一天
            String monthStart = outputSdf.format(startCal.getTime());
            startCal.set(Calendar.DAY_OF_MONTH, maxDay);
            //表示当月的最后一天
            String monthEnd = outputSdf.format(startCal.getTime());
            MonthAndInterviewer monthAndInterviewer = new MonthAndInterviewer( monthStart, monthEnd);
            monthAndInterviewerList.add(monthAndInterviewer);
            startCal.set(Calendar.DAY_OF_MONTH, startCal.getActualMaximum(Calendar.DAY_OF_MONTH));
            // 然后将日期加一天，这样就跳到下个月的第一天
            startCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return monthAndInterviewerList;
    }

}

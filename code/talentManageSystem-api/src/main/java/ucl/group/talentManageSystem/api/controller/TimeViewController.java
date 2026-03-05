package ucl.group.talentManageSystem.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ucl.group.talentManageSystem.api.common.R;
import ucl.group.talentManageSystem.api.common.utils.DateUtils;
import ucl.group.talentManageSystem.api.controller.form.TimeViewForm;
import ucl.group.talentManageSystem.api.db.pojo.StatisticsRequest;
import ucl.group.talentManageSystem.api.db.pojo.TimeViewEntity;
import ucl.group.talentManageSystem.api.db.pojo.bo.MonthAndInterviewer;
import ucl.group.talentManageSystem.api.service.InterviewInfoService;
import ucl.group.talentManageSystem.api.service.InterviewerService;
import ucl.group.talentManageSystem.api.service.StatisticsService;
import ucl.group.talentManageSystem.api.service.TimeViewService;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/count")
public class TimeViewController {
    @Resource
    private TimeViewService timeViewService;
    @Autowired
    private InterviewInfoService interviewInfoService;
    @Autowired
    private InterviewerService interviewerService;
    @Autowired
    private StatisticsService statisticsService;
    //根据月份判断
    @PostMapping("/byMonth")
    public R getTimeViewDataByMonth( @RequestBody TimeViewForm timeViewForm) throws ParseException {
        TimeViewEntity timeViewData=new TimeViewEntity();
        List<String> xData=new ArrayList<>();
        //竖轴
        String interName="面试回数";
        String hiredName="采用回数";
        List<Integer> interdatas=new ArrayList<>();
        List<Integer> hireddatas=new ArrayList<>();
        List<TimeViewEntity.Series> series=new ArrayList<>();
        TimeViewEntity.Series interseriesIn=new TimeViewEntity.Series();
        TimeViewEntity.Series hiredseriesIn=new TimeViewEntity.Series();

        List<MonthAndInterviewer> dateRanges = timeViewService.getDateRanges(timeViewForm.getStartTime(), timeViewForm.getEndTime());
        for(MonthAndInterviewer temp:dateRanges){
            String tempmMonth= DateUtils.parseDateToStr(DateUtils.YYYY_MM, DateUtils.parseDate(temp.getStartDate()));
            xData.add(tempmMonth);
            int interNum = interviewInfoService.countInterviewedNumByDate(temp.getStartDate(), temp.getEndDate());
            interdatas.add(interNum);
            int hiredNum = interviewInfoService.countHiredNumByDate(temp.getStartDate(), temp.getEndDate());
            hireddatas.add(hiredNum);
        }
        //横轴完成
        timeViewData.setxAxis(xData);
        //竖轴数据
        interseriesIn.setType(interName);
        interseriesIn.setData(interdatas);
        hiredseriesIn.setType(hiredName);
        hiredseriesIn.setData(hireddatas);
        series.add(interseriesIn);
        series.add(hiredseriesIn);
        timeViewData.setSeries(series);
        if (timeViewData != null && timeViewData.getxAxis() != null && !timeViewData.getxAxis().isEmpty()) {
            return R.ok().put("result", timeViewData);
        } else {
            // 如果数据为空，返回适当的错误信息
            return R.error("无法获取时间视图数据");
        }
    }
    @PostMapping("/byName")
    public R getStatistics(@RequestBody StatisticsRequest request) {

        Map<String, Object> result = statisticsService.getStatistics(
                request.getType(), request.getStartTime(), request.getEndTime());

        return R.ok().put("result", result);
    }


}

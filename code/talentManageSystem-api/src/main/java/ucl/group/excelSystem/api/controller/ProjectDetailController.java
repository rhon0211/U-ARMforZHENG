package ucl.group.excelSystem.api.controller;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.group.excelSystem.api.controller.form.SelectProjectByMonthForm;
import ucl.group.excelSystem.api.controller.request.SaveProjectRequest;
import ucl.group.excelSystem.api.service.ProjectDetailService;
import ucl.group.talentManageSystem.api.common.R;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

@Setter
@RestController
@RequestMapping("/api/v2/projectDetail/")
public class ProjectDetailController {
    @Autowired
    private ProjectDetailService projectDetailService;
    @GetMapping("/checkProjectName")
    public R checkProjectName(@RequestParam @Valid String projectName,@RequestParam(required = false) Integer projectId, @RequestParam @Valid Integer departmentId) {
        boolean isExist = projectDetailService.isProjectNameExist(departmentId,projectId ,projectName);
        return R.ok().put("result", isExist);
    }

    /**
     * 查询按年月的项目列表
     */
    @GetMapping("/queryByYearMonth")
    public R queryByYearMonth(@Valid SelectProjectByMonthForm form) {
        int year = (form.getYear() != null) ? form.getYear() : LocalDate.now().getYear();
        int month = (form.getMonth() != null) ? form.getMonth() : LocalDate.now().getMonthValue();
        LocalDate startDate = YearMonth.of(year, month).atEndOfMonth();
//        LocalDate firstDayOfMonth = YearMonth.of(year, month).atDay(1);

        // 查询公司、部门和案件的数据
        //Map<String, Object> queryResult = projectDetailService.queryAllByMonth(startDate, endDate, form.getPage(), form.getLength());

        Map<String, Object> queryResult = projectDetailService.queryAllByMonth(startDate, form.getPage(), form.getLength());
        return R.ok().put("result", queryResult);
    }

    /**
     * 更新
     */
    @GetMapping("/init")
    public R initProjectPage(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) Integer projectId,
            @RequestParam(required = false) Integer salesCompanyId,
            @RequestParam(required = false) Integer departmentId) {
        Map<String, Object> result = projectDetailService.initProjectPage(startDate,projectId, salesCompanyId, departmentId);
        return R.ok().put("result",result); // 使用 R 封装返回结果
    }

    /**
     * 新增初始化接口
     */
    @GetMapping("/newInit")
    public R newInitProjectPage(
            @RequestParam Integer salesCompanyId,
            @RequestParam Integer departmentId) {
        Map<String, Object> result = projectDetailService.newinitProjectPage(salesCompanyId, departmentId);
        return R.ok().put("result",result); // 使用 R 封装返回结果
    }

    /**
     * 添加新项目
     */
    @PostMapping("/add")
    public R addProject(@RequestBody SaveProjectRequest projectRequest) {
        if (projectRequest.getProjectInfo() == null) {
            return R.error("Invalid input: 'projectInfo' is required.");
        }
        if (projectRequest.getProjectDetails() == null || projectRequest.getProjectDetails().isEmpty()) {
            return R.error("Invalid input: 'projectDetails' is required.");
        }

        projectDetailService.addProject(projectRequest);
        return R.ok("Project added successfully!");
    }


    @PutMapping("/update")
    public R updateProject(@RequestBody SaveProjectRequest projectRequest) {
        // 校验请求体是否包含 projectInfo
        if (projectRequest.getProjectInfo() == null) {
            return R.error("Invalid input: 'projectInfo' is required.");
        }
        // 从 projectInfo 中获取 projectId
        Integer projectId = projectRequest.getProjectInfo().getProjectId();
        if (projectId == null) {
            return R.error("Invalid input: 'projectId' is required in 'projectInfo'.");
        }
        // 校验项目详情
//        if (projectRequest.getProjectDetails() == null || projectRequest.getProjectDetails().isEmpty()) {
//            return R.error("Invalid input: 'projectDetails' is required.");
//        }

        // 调用 Service 层进行更新
        projectDetailService.updateProjectDetails(projectId, projectRequest);
        return R.ok("Project updated successfully!");
    }

    /**
     * 追加初始化
     */
    @GetMapping("/additional")
    public R getAdditionalInitializationData() {
        Map<String, Object> result = projectDetailService.newinitAdditionalProjectPage();
        return R.ok().put("result", result); // 使用 R 进行封装
    }

}

package ucl.group.excelSystem.api.controller;

import cn.hutool.core.bean.BeanUtil;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.group.excelSystem.api.controller.form.SelectStaffCompanyByPageForm;
import ucl.group.excelSystem.api.db.pojo.BasicCompanyEntity;
import ucl.group.excelSystem.api.db.pojo.BasicStaffEntity;
import ucl.group.excelSystem.api.service.StaffManagementService;
import ucl.group.talentManageSystem.api.common.PageUtils;
import ucl.group.talentManageSystem.api.common.R;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@RestController
@RequestMapping("/api/v2/staff")
@SuppressWarnings({"unchecked", "rawtypes",})
public class StaffManagementController {
    @Autowired
    private StaffManagementService staffManagementService;
    @GetMapping("/getCompanyList")
    public R getCompanyList(@Valid SelectStaffCompanyByPageForm selectStaffCompanyByPageForm) {

        Map param=BeanUtil.beanToMap(selectStaffCompanyByPageForm);
        int page=selectStaffCompanyByPageForm.getPage();
        int length=selectStaffCompanyByPageForm.getLength();
        int start = (page - 1) * length;
        param.put("start", start);
        param.put("staffNameKanji",selectStaffCompanyByPageForm.getStaffNameKanji());
        PageUtils pageUtils = staffManagementService.selectStaffCompanyByPage(param);

        return R.ok().put("result", pageUtils);
    }
    @GetMapping("/getStaffAll")
    public R selectStaffAll(){
        ArrayList<HashMap> Stafflist = staffManagementService.selectStaff();
        return R.ok().put("result", Stafflist);
    }

    @GetMapping("/canSetInactive/{staffId}")
   public R canSetInactive(@PathVariable int staffId) {
        boolean success = staffManagementService.canSetInactive(staffId);
        return R.ok().put("result", success);
    }
//    @DeleteMapping("/delete/{staffId}")
//    public String deleteStaff(@PathVariable int staffId) {
//        boolean success = staffManagementService.deleteStaffIfAllowed(staffId);
//        return success ? "删除成功" : "删除失败，staff_id 仍有活跃的项目";
//    }
    @PostMapping("/addStaff")
    public R addStaff(@RequestBody BasicStaffEntity basicStaffEntity){
        staffManagementService.addStaff(basicStaffEntity);
        return R.ok();
    }
    @PutMapping("/updateStaff")
    public R updateStaff(@RequestBody BasicStaffEntity basicStaffEntity) {
        staffManagementService.updateStaff(basicStaffEntity);
        return R.ok();
    }

    @GetMapping("/getStaffById/{staffId}")
    public R getStaffById(@PathVariable Integer staffId) {
        BasicStaffEntity staff = staffManagementService.getStaffById(staffId);
        return R.ok().put("result", staff);
    }

    @GetMapping("/getCompanys")
    public R getCompanys() {
        List<BasicCompanyEntity> companys = staffManagementService.getCompanys();
        return R.ok().put("result", companys);
    }

}

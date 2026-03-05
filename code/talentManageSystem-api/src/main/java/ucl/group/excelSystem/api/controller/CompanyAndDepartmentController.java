package ucl.group.excelSystem.api.controller;


import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageInfo;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;
import ucl.group.excelSystem.api.controller.request.CompanyAndDepartmentPageRequest;
import ucl.group.excelSystem.api.controller.request.InsertOrUpdateCompanyRequest;
import ucl.group.excelSystem.api.controller.request.InsertOrUpdateDepartmentRequest;
import ucl.group.excelSystem.api.db.pojo.BasicCompanyEntity;
import ucl.group.excelSystem.api.db.pojo.BasicDepartmentEntity;
import ucl.group.excelSystem.api.service.CompanyAndDepartmentService;
import ucl.group.excelSystem.api.service.DepartmentService;
import ucl.group.talentManageSystem.api.common.PageUtils;
import ucl.group.talentManageSystem.api.common.R;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;

@Setter
@RestController
@RequestMapping("/api/v2/companyAndDepartment")
public class CompanyAndDepartmentController {

    @Resource
    private CompanyAndDepartmentService companyAndDepartmentService;

    @Resource
    private DepartmentService departmentService;


    @GetMapping("/getByPage")
    public R getCompanyAndDepartmentByPage(@Valid CompanyAndDepartmentPageRequest request) {
        // 将请求参数转换为Map对象
        Map<String, Object> param = BeanUtil.beanToMap(request);
        // 获取页码和每页长度
        int page = request.getPage();
        int length = request.getLength();
        // 计算起始位置
        int start = (page - 1) * length;

        // 将起始位置添加到参数Map中
        param.put("start", start);
        param.put("saleOrProcurement", request.getSaleOrProcurement());
        // 调用服务方法获取分页数据
        PageUtils pageUtils = companyAndDepartmentService.getCompanyAndDepartmentByPage(param);

        // 返回包含分页结果的响应对象
        return R.ok().put("result", pageUtils);
    }
    @PostMapping("/checkUnique")
    public R checkUnique(@RequestBody @Valid InsertOrUpdateCompanyRequest request) {
        try {
            companyAndDepartmentService.checkUnique(BeanUtil.toBean(request, BasicCompanyEntity.class));
        } catch (RuntimeException e) {
            return R.error(400, e.getMessage());
        }
        return R.ok().put("message", "唯一性验证通过");
    }
    @GetMapping("/is-empty")
    public R isProjectEmpty(@RequestParam("companyId") int companyId) {
        boolean isEmpty = companyAndDepartmentService.isCompanyProjectEmpty(companyId);
        return R .ok().put("result", isEmpty);
    }

    @PostMapping("/insertCompany")
    public R insertCompany(@RequestBody @Valid InsertOrUpdateCompanyRequest request) {
        try {
            companyAndDepartmentService.insertCompany(BeanUtil.toBean(request, BasicCompanyEntity.class));
        } catch (RuntimeException e) {
            return R.error(400, e.getMessage());
        }
        return R.ok().put("message", "插入成功");
    }

    @PutMapping("/updateCompany")
    public R updateCompany(@RequestBody @Valid InsertOrUpdateCompanyRequest request) {
        try {
            companyAndDepartmentService.updateCompany(BeanUtil.toBean(request, BasicCompanyEntity.class));
        } catch (RuntimeException e) {
            return R.error(400, e.getMessage());
        }
        return R.ok().put("message", "更新成功");
    }

    @PostMapping("/insertDepartment")
    public R insertDepartment(@RequestBody @Valid InsertOrUpdateDepartmentRequest request) {

        BasicDepartmentEntity bean = BeanUtil.toBean(request, BasicDepartmentEntity.class);

        // 调用服务方法插入数据
        departmentService.insertDepartment(bean);

        // 返回成功响应对象
        return R.ok();
    }

    @PutMapping("/updateDepartment")
    public R updateDepartment(@RequestBody @Valid InsertOrUpdateDepartmentRequest request) {

        BasicDepartmentEntity bean = BeanUtil.toBean(request, BasicDepartmentEntity.class);

        // 调用服务方法更新数据
        departmentService.updateDepartment(bean);

        // 返回成功响应对象
        return R.ok();
    }
    @GetMapping("/hasActivePj")
    public R hasActivePj(@RequestParam("departmentId") Integer departmentId){
        boolean success = departmentService.checkdepartment(departmentId);
        return R.ok().put("result", success);
    }
    @GetMapping("/getDepartmentById")
    public R getDepartmentById(@RequestParam("departmentId") Integer departmentId) {
        // 调用服务方法获取部门信息
        BasicDepartmentEntity department = departmentService.getDepartmentById(departmentId);

        // 返回包含部门的信息的响应对象
        return R.ok().put("result", department);
    }

    @GetMapping("/getCompanyById")
    public R getCompanyById(@RequestParam("companyId") Integer companyId) {
        // 调用服务方法获取公司信息
        BasicCompanyEntity company = companyAndDepartmentService.getCompanyById(companyId);

        // 返回包含公司信息的响应对象
        return R.ok().put("result", company);
    }

    @GetMapping("/getByPageHelper")
    public R getCompanyAndDepartmentByPageHelper(@Valid CompanyAndDepartmentPageRequest request) {
        // 将请求参数转换为Map对象
        Map<String, Object> param = BeanUtil.beanToMap(request);
        // 获取页码和每页长度
        int page = request.getPage();
        int length = request.getLength();
        // 计算起始位置
        int start = (page - 1) * length;
        // 将起始位置添加到参数Map中
        param.put("start", start);
        param.put("saleOrProcurement", request.getSaleOrProcurement());
        // 调用服务方法获取分页数据
        PageInfo<BasicCompanyEntity> companyAndDepartmentByPageHelper = companyAndDepartmentService.getCompanyAndDepartmentByPageHelper(param);
        // 返回包含分页结果的响应对象
        return R.ok().put("result", companyAndDepartmentByPageHelper);
    }

}

package ucl.group.excelSystem.api.controller;

import cn.hutool.core.bean.BeanUtil;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;
import ucl.group.excelSystem.api.controller.form.DeleteTechForm;
import ucl.group.excelSystem.api.controller.form.InsertTechForm;
import ucl.group.excelSystem.api.controller.form.SelectTechByPageForm;
import ucl.group.excelSystem.api.controller.form.UpdateTechForm;
import ucl.group.excelSystem.api.db.pojo.BasicTechnicianEntity;
import ucl.group.excelSystem.api.service.TechnicianService;
import ucl.group.talentManageSystem.api.common.PageUtils;
import ucl.group.talentManageSystem.api.common.R;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;

@Setter
@RestController
@RequestMapping("/api/v2/technician")
@SuppressWarnings({"unchecked", "rawtypes",})
public class TechnicianController {

	@Resource
	private TechnicianService technicianService;
	//@Log(title = "管理表格系统v2-技术者管理", businessType = BusinessType.INSERT)
	@PostMapping
	//@SaCheckLogin
	public R insertTech(@RequestBody @Valid InsertTechForm form) {
		BasicTechnicianEntity basicTechnicianEntity = BeanUtil.toBean(form, BasicTechnicianEntity.class);
		technicianService.insertTech(basicTechnicianEntity);
		return R.ok();
	}

	@GetMapping
	//@SaCheckLogin
	public R selectTechByPage(@Valid SelectTechByPageForm form) {
		Map param = BeanUtil.beanToMap(form);
		int page = form.getPage();
		int length = form.getLength();
		int start = (page - 1) * length;
		param.put("start", start);
		PageUtils pageUtils = technicianService.selectTechByPage(param);

		return R.ok().put("result", pageUtils);
	}

	//@Log(title = "管理表格系统v2-技术者管理", businessType = BusinessType.UPDATE)
	@PutMapping
	//@SaCheckLogin
	public R updateTech(@RequestBody @Valid UpdateTechForm form) {
		technicianService.updateTech(BeanUtil.toBean(form, BasicTechnicianEntity.class));
		return R.ok();
	}
	//@Log(title = "管理表格系统v2-技术者管理", businessType = BusinessType.DELETE)
	@DeleteMapping
	//@SaCheckLogin
	public R deleteTech(@RequestBody @Valid DeleteTechForm form) {
		technicianService.deleteTech(form.getIds());
		return R.ok();
	}

}

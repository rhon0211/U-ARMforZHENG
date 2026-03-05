package ucl.group.excelSystem.api.service.impl;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucl.group.excelSystem.api.db.dao.DepartmentDao;
import ucl.group.excelSystem.api.db.pojo.BasicDepartmentEntity;
import ucl.group.excelSystem.api.db.pojo.vo.DepartmentVo;
import ucl.group.excelSystem.api.service.DepartmentService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Setter
@Service
@Slf4j
@SuppressWarnings({"rawtypes",})
public class DepartmentServiceImpl implements DepartmentService {

    @Resource
    private DepartmentDao departmentDao;

    @Override
    public Integer getDepartmentCountByCompanyId(Integer companyId) {
        return departmentDao.getDepartmentCountByCompanyId(companyId);
    }

    @Override
    public boolean checkdepartment(int departmentId) {
        int count = departmentDao.checkdepartment(departmentId);
        return count == 0;
    }

    @Override
    @Transactional
    public List<DepartmentVo> getDepartmentListByCompanyId(Integer companyId) {
        return departmentDao.getDepartmentListByCompanyId(companyId);
    }

    @Override
    @Transactional
    public void insertDepartment(BasicDepartmentEntity bean) {
        departmentDao.insertDepartment(bean);
    }

    @Override
    @Transactional
    public void updateDepartment(BasicDepartmentEntity bean) {
        departmentDao.updateDepartment(bean);
        if (bean.getActiveFlg() != null && bean.getActiveFlg() == 0) {
            departmentDao.deactivateDPProjects(bean.getDepartmentId());
        }
    }

    @Override
    public BasicDepartmentEntity getDepartmentById(Integer departmentId) {
        return departmentDao.getById(departmentId);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List<HashMap> selectDepartmentsByCompanyId(Integer companyId) {
        return departmentDao.selectDepartmentsByCompanyId(companyId);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List<HashMap> getDepartmentsByProjectIds(List<Integer> projectIds) {
        return departmentDao.getDepartmentsByProjectIds(projectIds);
    }

    @Override
    public List<Integer> getDepartmentIdsByProjectIds(List<Integer> projectIds) {
        return departmentDao.getDepartmentIdsByProjectIds(projectIds);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List<HashMap> getDepartmentsByCompanyId(Integer companyId) {
        return departmentDao.getDepartmentsByCompanyId(companyId);
    }

}

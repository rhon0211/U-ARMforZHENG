package ucl.group.excelSystem.api.service.impl;

import cn.hutool.core.map.MapUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucl.group.excelSystem.api.db.dao.CompanyAndDepartmentDao;
import ucl.group.excelSystem.api.db.pojo.BasicCompanyEntity;
import ucl.group.excelSystem.api.db.pojo.vo.DepartmentVo;
import ucl.group.excelSystem.api.service.CompanyAndDepartmentService;
import ucl.group.excelSystem.api.service.DepartmentService;
import ucl.group.talentManageSystem.api.common.PageUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Service
@Slf4j
@SuppressWarnings({"unchecked", "rawtypes",})
public class CompanyAndDepartmentServiceImpl implements CompanyAndDepartmentService {

    @Resource
    private CompanyAndDepartmentDao companyAndDepartmentDao;

    @Resource
    private DepartmentService departmentService;

    @Override
    @Transactional
    public PageUtils getCompanyAndDepartmentByPage(Map<String, Object> param) {
        Object saleOrProcurement = param.get("saleOrProcurement");
        ArrayList<HashMap> list ;
        //PageUtils pageUtils = null;
        long count;
        int page;
        int length;
        if (saleOrProcurement != null && "1".equals(saleOrProcurement.toString())) {
            // 获取采购公司的信息
            count = companyAndDepartmentDao.selectSaleCompanyByPageCount();
            if (count > 0) {
                list = companyAndDepartmentDao.selectSaleCompanyByPage(param);
            } else {
                list = new ArrayList<>();
            }
            for (HashMap hashMap : list) {
                Integer companyId = MapUtil.getInt(hashMap, "companyId");
                Integer departmentCount = departmentService.getDepartmentCountByCompanyId(companyId);
                hashMap.put("departmentCount", departmentCount);
                List<DepartmentVo> departmentList = departmentService.getDepartmentListByCompanyId(companyId);
                hashMap.put("departmentList", departmentList);
            }
            // 从参数中获取当前页码
            page = MapUtil.getInt(param, "page");

            // 从参数中获取每页长度
            length = MapUtil.getInt(param, "length");
        } else
            if (saleOrProcurement != null && "2".equals(saleOrProcurement.toString())) {
            // 获取销售公司的信息
            count = companyAndDepartmentDao.selectProcurementCompanyByPageCount();
            if (count > 0) {
                list = companyAndDepartmentDao.selectProcurementCompanyByPage(param);
            } else {
                list = new ArrayList<>();
            }
            for (HashMap hashMap : list) {
                Integer companyId = MapUtil.getInt(hashMap, "companyId");
                Integer departmentCount = departmentService.getDepartmentCountByCompanyId(companyId);
                hashMap.put("departmentCount", departmentCount);
                List<DepartmentVo> departmentList = departmentService.getDepartmentListByCompanyId(companyId);
                hashMap.put("departmentList", departmentList);
            }
            // 从参数中获取当前页码
            page = MapUtil.getInt(param, "page");

            // 从参数中获取每页长度
            length = MapUtil.getInt(param, "length");
        } else
            if (saleOrProcurement != null && "3".equals(saleOrProcurement.toString())) {
                count = companyAndDepartmentDao.selectUclByPageCount();
                if (count > 0){
                    list = companyAndDepartmentDao.selectUclByPage(param);
                }else {
                    list = new ArrayList<>();
                }
                for (HashMap hashMap : list){
                    Integer companyId = MapUtil.getInt(hashMap, "companyId");
                    Integer departmentCount = departmentService.getDepartmentCountByCompanyId(companyId);
                    hashMap.put("departmentCount", departmentCount);
                    List<DepartmentVo> departmentList = departmentService.getDepartmentListByCompanyId(companyId);
                    hashMap.put("departmentList", departmentList);
                }
                page = MapUtil.getInt(param, "page");
                length = MapUtil.getInt(param, "length");
            }
            else {
            // 获取所有公司的信息
            count = companyAndDepartmentDao.selectCompanyByPageCount();
            if (count > 0) {
                list = companyAndDepartmentDao.selectCompanyByPage(param);
            } else {
                list = new ArrayList<>();
            }
            for (HashMap hashMap : list) {
                Integer companyId = MapUtil.getInt(hashMap, "companyId");
                Integer departmentCount = departmentService.getDepartmentCountByCompanyId(companyId);
                hashMap.put("departmentCount", departmentCount);
                List<DepartmentVo> departmentList = departmentService.getDepartmentListByCompanyId(companyId);
                hashMap.put("departmentList", departmentList);
            }
            // 从参数中获取当前页码
            page = MapUtil.getInt(param, "page");

            // 从参数中获取每页长度
            length = MapUtil.getInt(param, "length");
        }
        // 创建并返回一个包含分页信息和客户信息列表的PageUtils对象
        return new PageUtils(list, count, page, length);

    }


    @Override
    @Transactional
    public void checkUnique(BasicCompanyEntity bean) {
        if (bean.getCompanyId() == null) { // 插入检查
            if (companyAndDepartmentDao.findByCompanyName(bean.getCompanyName()) != null) {
                throw new RuntimeException("企業名はすでに存在します: " + bean.getCompanyName());
            }
            if (companyAndDepartmentDao.findByCompanyAbbreviation(bean.getCompanyAbbreviation()) != null) {
                throw new RuntimeException("企業略称はすでに存在します: " + bean.getCompanyAbbreviation());
            }
        } else { // 更新检查（排除自身）
            if (companyAndDepartmentDao.findByCompanyNameExcludingId(bean.getCompanyName(), bean.getCompanyId()) != null) {
                throw new RuntimeException("企業名はすでに存在します: " + bean.getCompanyName());
            }
            if (companyAndDepartmentDao.findByCompanyAbbreviationExcludingId(bean.getCompanyAbbreviation(), bean.getCompanyId()) != null) {
                throw new RuntimeException("企業略称はすでに存在します: " + bean.getCompanyAbbreviation());
            }
        }
    }


    @Override
    @Transactional
    public void insertCompany(BasicCompanyEntity bean) {
        checkUnique(bean); // 先检查唯一性
        companyAndDepartmentDao.insertCompany(bean);
    }

    @Override
    @Transactional
    public void updateCompany(BasicCompanyEntity bean) {
        checkUnique(bean); // 先检查唯一性
        companyAndDepartmentDao.updateCompany(bean);
        if (bean.getActiveFlg() != null && bean.getActiveFlg() == 0) {
            companyAndDepartmentDao.deactivateRelatedProjects(bean.getCompanyId());
        }
    }


    @Override
    public BasicCompanyEntity getCompanyById(Integer companyId) {
        return  companyAndDepartmentDao.getById(companyId);

    }
    @Override
    public boolean isCompanyProjectEmpty(int companyId) {
        // 存在活跃项目 → 返回 false；否则 true
        int result = companyAndDepartmentDao.hasActiveProjects(companyId);
        return result == 0;
    }

    @Override
    public Long getSaleCompanyCount() {
        return companyAndDepartmentDao.selectSaleCompanyByPageCount1();
    }

    @Override
    public ArrayList<HashMap> selectSaleCompanyByPage(Map<String, Object> param) {
        return companyAndDepartmentDao.selectTradeCompanyByPage(param);
    }

    @Override
    public ArrayList<HashMap> selectSaleCompanyByDepartmentIdsByPage(List<Integer> departmentIds) {
        return companyAndDepartmentDao.selectSaleCompanyByDepartmentIdsByPage(departmentIds);
    }

    @Override
    public PageInfo<BasicCompanyEntity> getCompanyAndDepartmentByPageHelper(Map<String, Object> param) {
        int length = (int) param.get("length");
        int start = (int) param.get("start");
        //PageHelper.startPage(start, length);
        //List<BasicCompanyEntity> companys = companyAndDepartmentDao.getAll();
        //PageInfo<BasicCompanyEntity> pageInfo = new PageInfo<>(companys);
        //Lambda 表达式
        try (Page<BasicCompanyEntity> ignored = PageHelper.startPage(start, length)) {
            List<BasicCompanyEntity> companys = companyAndDepartmentDao.getAll();
            return new PageInfo<>(companys);
        }
    }

    @Override
    public ArrayList<HashMap> selectSaleCompanyByProjectIds(List<Integer> projectIds) {
        return companyAndDepartmentDao.selectSaleCompanyByProjectIds(projectIds);
    }

}

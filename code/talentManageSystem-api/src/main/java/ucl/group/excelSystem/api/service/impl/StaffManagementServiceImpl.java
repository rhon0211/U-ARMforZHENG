package ucl.group.excelSystem.api.service.impl;

import cn.hutool.core.map.MapUtil;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucl.group.excelSystem.api.db.dao.StaffManagementCompanyDao;
import ucl.group.excelSystem.api.db.dao.StaffManagementStaffDao;
import ucl.group.excelSystem.api.db.pojo.BasicCompanyEntity;
import ucl.group.excelSystem.api.db.pojo.BasicStaffEntity;
import ucl.group.excelSystem.api.service.StaffManagementService;
import ucl.group.talentManageSystem.api.common.PageUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Setter
@Service
@SuppressWarnings({"rawtypes", "unchecked"})
public class StaffManagementServiceImpl implements StaffManagementService {
    @Autowired
    private StaffManagementCompanyDao staffManagementCompanyDao;
    @Autowired
    private StaffManagementStaffDao staffManagementStaffDao;

    @Override
    @Transactional
    public PageUtils selectStaffCompanyByPage(Map param) {
        List<HashMap> originalList ;

        // 获取客户信息的总数
        long count = staffManagementCompanyDao.selectStaffCompanyByPageCount();

        // 如果存在客户信息，则根据参数分页查询
        if (count > 0) {
            originalList = staffManagementCompanyDao.selectCompanyByPage(param);
        } else {
            originalList = new ArrayList<>();
        }


        // 创建一个新的列表保存包含员工信息的公司数据
        List<HashMap> updatedList = new ArrayList<>();

        // 遍历原始公司列表
        for (Map<String, Object> company : originalList) {
            int companyId = (int) company.get("companyId");  // 获取公司ID


            // 查询该公司对应的员工列表
            List<HashMap> staffList = staffManagementStaffDao.selectStaffByCompanyId((long) companyId);

            // 将员工列表添加到公司信息中
            company.put("staffList", staffList);
            company.put("staffCount", staffList.size());

            // 添加到新的列表
            updatedList.add((HashMap) company);
        }

        // 从参数中获取当前页码
        int page = MapUtil.getInt(param, "page");
        // 从参数中获取每页长度
        int length = MapUtil.getInt(param, "length");

        // 创建并返回一个包含分页信息和客户信息列表的 PageUtils 对象
        return new PageUtils(updatedList, count, page, length);

    }
    @Override
    public boolean canSetInactive(int staffId) {
        // 检查员工是否关联有活跃的项目
        int count = staffManagementStaffDao.checkStaffHasActiveProject(staffId);
        return count == 0;
    }

    @Override
    public Integer getProcurementCompanyIdByStaffId(Integer staffId) {
        return staffManagementStaffDao.getProcurementCompanyIdByStaffId(staffId);
    }

//    @Override
//    @Transactional
//    public boolean deleteStaffIfAllowed(int staffId) {
//        // 先检查 staff_id 是否有活跃的 project_id
//        int count = staffManagementStaffDao.checkStaffHasActiveProject(staffId);
//
//        if (count > 0) {
//            return false; // 不能删除
//        }
//
//        // 如果没有活跃的 project_id，则删除
//        int deletedRows = staffManagementStaffDao.deleteStaffIfNoActiveProject(staffId);
//        return deletedRows > 0;
//    }

    @Override
    @Transactional
    public ArrayList<HashMap> selectStaff() {
        return staffManagementStaffDao.selectStaff();

    }

    @Override
    @Transactional
    public void deleteStaff(List<Integer> staffids) {
        if (staffids == null || staffids.isEmpty()) {
            throw new IllegalArgumentException("IDリストはヌルまたはからにできません");
        }
        staffManagementStaffDao.deleteStaffById(staffids);
    }

    @Override
    public void addStaff(BasicStaffEntity basicStaffEntity) {
        staffManagementStaffDao.addStaff(basicStaffEntity);
    }

    @Override
    public void updateStaff(BasicStaffEntity basicStaffEntity) {
        staffManagementStaffDao.updateStaff(basicStaffEntity);

        if (Objects.equals(basicStaffEntity.getActiveFlg(), "0")) {
            Integer staffId = basicStaffEntity.getStaffId();
            String endDateStr = basicStaffEntity.getOperationEndDate();
            if (endDateStr != null) {
                try {
                    // 解析成 LocalDate，然后取出年月加一个月
                    LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    LocalDate afterMonth = endDate.plusMonths(1);
                    String afterMonthStr = afterMonth.format(DateTimeFormatter.ofPattern("yyyy-MM")); // 转换成 "2025-06" 形式

                    // 更新劳务表
                    staffManagementStaffDao.setLaborInactiveFromMonth(staffId, afterMonthStr);
                } catch (Exception e) {
                    throw new RuntimeException("退場時間格式錯誤: " + endDateStr, e);
                }
            }
        }
    }


    @Override
    public BasicStaffEntity getStaffById(Integer staffId) {
        return staffManagementStaffDao.getStaffById(staffId);
    }

    @Override
    public List<Integer> getStaffIds() {
        return staffManagementStaffDao.getStaffIds();
    }

    @Override
    public String getNameById(Integer id) {
        return staffManagementStaffDao.getNameById(id);
    }

    @Override
    public List<BasicCompanyEntity> getCompanys() {
        return staffManagementStaffDao.getCompanys();
    }

}
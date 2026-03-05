package ucl.group.excelSystem.api.service;

import org.springframework.stereotype.Service;
import ucl.group.excelSystem.api.db.pojo.BasicCompanyEntity;
import ucl.group.excelSystem.api.db.pojo.BasicStaffEntity;
import ucl.group.talentManageSystem.api.common.PageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@SuppressWarnings({"rawtypes",})
public interface StaffManagementService {
     PageUtils selectStaffCompanyByPage(Map param);
     ArrayList<HashMap> selectStaff();
     void deleteStaff(List<Integer> staffids);
     void addStaff(BasicStaffEntity basicStaffEntity);
     void updateStaff(BasicStaffEntity basicStaffEntity);

    BasicStaffEntity getStaffById(Integer staffId);

    List<Integer> getStaffIds();

    String getNameById(Integer id);

    List<BasicCompanyEntity> getCompanys();
//    boolean deleteStaffIfAllowed(int staffId);
    boolean canSetInactive(int staffId);

    Integer getProcurementCompanyIdByStaffId(Integer staffId);
}

package ucl.group.excelSystem.api.service;

import ucl.group.excelSystem.api.db.pojo.BasicCustomerEntity;
import ucl.group.talentManageSystem.api.common.PageUtils;

import java.util.Map;
@SuppressWarnings({ "rawtypes",})
public interface CustomerService {
     void insertCustomer(BasicCustomerEntity basicCustomerEntity);

     void updateCustomer(BasicCustomerEntity basicCustomerEntity);
     void deleteCustomer(Long[] customerIds);
     PageUtils selectCustomerByPage(Map param);
}

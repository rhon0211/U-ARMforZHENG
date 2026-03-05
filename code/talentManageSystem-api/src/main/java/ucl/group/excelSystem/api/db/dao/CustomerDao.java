package ucl.group.excelSystem.api.db.dao;

import ucl.group.excelSystem.api.db.pojo.BasicCustomerEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface CustomerDao {
 void insertCustomer(BasicCustomerEntity basicCustomerEntity);

 void updateCustomer(BasicCustomerEntity basicCustomerEntity);

 void deleteCustomer(Long[] customerIds);

 long selectCustomerByPageCount();

 ArrayList<HashMap> selectCustomerByPage(Map param);
BasicCustomerEntity searchById(Long customerId);
}

package ucl.group.excelSystem.api.service;

import org.springframework.stereotype.Service;
import ucl.group.excelSystem.api.db.pojo.BasicPartnerEntity;
import ucl.group.talentManageSystem.api.common.PageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@SuppressWarnings({ "rawtypes",})
public interface PartnerService {
     void addPartner(BasicPartnerEntity basicPartnerEntity);
     void updatePartner(BasicPartnerEntity basicPartnerEntity);
     void deletePartner(List<Integer> companyids);
     PageUtils selectCompanyByPage(Map param);
     ArrayList<HashMap> selectCompany();
}
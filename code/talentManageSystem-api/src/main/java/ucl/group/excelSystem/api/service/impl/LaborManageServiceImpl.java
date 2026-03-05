package ucl.group.excelSystem.api.service.impl;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ucl.group.excelSystem.api.db.dao.LaborManageDao;
import ucl.group.excelSystem.api.db.pojo.vo.LaborManageVO;
import ucl.group.excelSystem.api.db.pojo.vo.PageResultVO;
import ucl.group.excelSystem.api.service.LaborManageService;
import ucl.group.excelSystem.api.service.StaffManagementService;
import ucl.group.talentManageSystem.api.exception.ServiceException;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Setter
@Service
public class LaborManageServiceImpl implements LaborManageService {

    @Autowired
    private LaborManageDao laborManageDao;

    @Getter
    @Resource
    private StaffManagementService staffManagementService;
    //获取上个月的时间字符串 (假设时间格式为 "yyyy-MM")

    // 生成周期月份列表（2月 ~ 次年1月）
    private List<String> generateCycleMonths(int startYear) {
        List<String> result = new ArrayList<>();
        for (int m = 2; m <= 12; m++) {
            result.add(String.format("%04d-%02d", startYear, m));
        }
        result.add(String.format("%04d-01", startYear + 1));
        return result;
    }

    private void copyLaborFields(LaborManageVO from, LaborManageVO to) {
        BigDecimal overtimeAllowance = from.getOvertimeAllowance() == null ? BigDecimal.ZERO : from.getOvertimeAllowance();
        BigDecimal advancePayment = from.getAdvancePayment() == null ? BigDecimal.ZERO : from.getAdvancePayment();
        BigDecimal TaxablePaymentTotal = from.getTaxablePaymentTotal() == null ? BigDecimal.ZERO : from.getTaxablePaymentTotal();
        BigDecimal TotalLaborExpenses = from.getTotalLaborExpenses() == null ? BigDecimal.ZERO : from.getTotalLaborExpenses();
        BigDecimal result1 = TaxablePaymentTotal.subtract(advancePayment).subtract(overtimeAllowance);
        BigDecimal result2 = TotalLaborExpenses.subtract(advancePayment).subtract(overtimeAllowance);
        to.setBaseSalary(from.getBaseSalary());
        to.setPositionAllowance(from.getPositionAllowance());
        to.setOtherAllowances(from.getOtherAllowances());
        to.setCommutingExpense(from.getCommutingExpense());
        to.setTaxablePaymentTotal(result1);
        to.setHealthInsuranceFee(from.getHealthInsuranceFee());
        to.setNursingInsuranceFee(from.getNursingInsuranceFee());
        to.setWelfarePensionInsurance(from.getWelfarePensionInsurance());
        to.setEmploymentInsuranceFee(from.getEmploymentInsuranceFee());
        to.setOtherLaborExpenses(from.getOtherLaborExpenses());
        to.setTotalLaborExpenses(result2);
        to.setAdvancePayment(null);
        to.setOvertimeAllowance(null);
    }

    // 计算上个月的时间字符串
//    @NotNull
//    private String getPreviousMonth(String time) {
//        try {
//            LocalDate date = LocalDate.parse(time + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//            LocalDate previousMonth = date.minusMonths(1);
//            return previousMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
//        } catch (Exception e) {
//            throw new IllegalArgumentException("時刻形式エラー。yyyy-MM である必要があります", e);
//        }
//    }
    @Override
    @Transactional
    public PageResultVO<LaborManageVO> getLaborManageByPage(String time, Integer page, Integer pageSize) {
        // Step 1: 参数处理
        time = StringUtils.hasText(time) ? time.trim() : "DEFAULT_TIME";
        page = (page == null || page < 1) ? 1 : page;
        pageSize = (pageSize == null || pageSize < 1) ? 100 : pageSize;
//        int offset = (page - 1) * pageSize;
//        String previousMonth = getPreviousMonth(time);

        // Step 2: 获取活跃员工
        List<Map<String, Object>> uclStaffList = laborManageDao.getUCLStaffWithNamesByPage();
        if (uclStaffList == null || uclStaffList.isEmpty()) {
            throw new ServiceException("UCL の従業員情報はありません。");
        }

        Map<Integer, String> staffNameMap = new HashMap<>();
        for (Map<String, Object> staff : uclStaffList) {
            String endMonth = (String) staff.get("operationEndMonth");
            String startMonth = (String) staff.get("operationStartMonth");
            if (endMonth != null && time.compareTo(endMonth) > 0 ||
                    startMonth != null && time.compareTo(startMonth) < 0) {
                continue;
            }
            staffNameMap.put((Integer) staff.get("staffId"), (String) staff.get("staffNameKanji"));
        }

        List<Integer> staffIds = new ArrayList<>(staffNameMap.keySet());

        // Step 2.5: 初始化周期数据（全年）
        List<LaborManageVO> activeEmployees = staffIds.stream()
                .map(staffId -> {
                    LaborManageVO vo = new LaborManageVO();
                    vo.setStaffId(staffId);
                    vo.setStaffNameKanji(staffNameMap.get(staffId));
                    return vo;
                }).collect(Collectors.toList());

        int year = Integer.parseInt(time.substring(0, 4));
        int month = Integer.parseInt(time.substring(5, 7));
        int startYear = (month >= 2) ? year : year - 1;

        initializeEmptyLaborRecordsForCycleIfMissing(activeEmployees, startYear);

        // Step 3: 获取当前月记录
        List<LaborManageVO> currentMonthRecords = laborManageDao.getLaborManageByTimeAndStaffIds(time, staffIds);

        // Step 3.5: 特殊逻辑 - 若为2月，且当前无记录，则从1月复制
        // Step 3.5: 特殊逻辑 - 每年2月，如果已存在记录但 laborStatus = 0，则从1月复制字段覆盖
        if (time.endsWith("-02")) {
            String january = time.substring(0, 4) + "-01";

            // 查出当前2月记录中 laborStatus == 0 的员工
            Map<Integer, LaborManageVO> febMap = currentMonthRecords.stream()
                    .filter(vo -> vo.getLaborStatus() != null && vo.getLaborStatus() == 0)
                    .collect(Collectors.toMap(LaborManageVO::getStaffId, Function.identity()));

            // 查出1月记录
            List<LaborManageVO> janRecords = laborManageDao.getLaborManageByTimeAndStaffIds(january, staffIds);
            Map<Integer, LaborManageVO> janMap = janRecords.stream()
                    .collect(Collectors.toMap(LaborManageVO::getStaffId, Function.identity()));

            // 重组2月记录（只替换字段）
            List<LaborManageVO> updatedFebList = new ArrayList<>();
            for (Map.Entry<Integer, LaborManageVO> entry : febMap.entrySet()) {
                Integer staffId = entry.getKey();
                LaborManageVO febOriginal = entry.getValue();
                LaborManageVO jan = janMap.get(staffId);

                if (jan != null) {
                    LaborManageVO merged = new LaborManageVO();
                    // 保留原始信息
                    merged.setId(febOriginal.getId());
                    merged.setStaffId(staffId);
                    merged.setTime(time);
                    merged.setStaffNameKanji(febOriginal.getStaffNameKanji());
                    merged.setLaborStatus(0);
                    // 从 1 月复制字段
                    copyLaborFields(jan, merged);
                    updatedFebList.add(merged);
                }
            }

            // 用合成后的 updatedFebList 替换 currentMonthRecords 中对应项
            for (int i = 0; i < currentMonthRecords.size(); i++) {
                LaborManageVO old = currentMonthRecords.get(i);
                LaborManageVO updated = updatedFebList.stream()
                        .filter(u -> u.getStaffId().equals(old.getStaffId()))
                        .findFirst().orElse(null);
                if (updated != null) {
                    currentMonthRecords.set(i, updated);
                }
            }
        }

        // Step 4: 组装结果（只使用当前月记录或创建空记录）
        Map<Integer, LaborManageVO> currentMap = currentMonthRecords.stream()
                .collect(Collectors.toMap(LaborManageVO::getStaffId, Function.identity()));

        List<LaborManageVO> finalList = new ArrayList<>();
        List<LaborManageVO> toInsertList = new ArrayList<>();

        for (Integer staffId : staffIds) {
            LaborManageVO current = currentMap.get(staffId);
            if (current != null) {
                finalList.add(current);
            } else {
                LaborManageVO empty = createNewLaborRecord(staffId, staffNameMap.get(staffId), time);
                toInsertList.add(empty);
                finalList.add(empty);
            }
        }

        // Step 5: 插入缺失记录（非2月情况一般是冗余保险）
        if (!toInsertList.isEmpty()) {
            try {
                laborManageDao.batchInsertLaborManage(toInsertList);
            } catch (Exception e) {
                throw new RuntimeException("バッチ挿入が失敗したため、トランザクションがロールバックされました", e);
            }
        }

        // Step 6: 返回分页数据
        Long totalCount = laborManageDao.getTotalLaborManageCount(time);
        return new PageResultVO<>(totalCount, page, pageSize, finalList);
    }



    // **辅助方法：创建新劳务记录**
    private LaborManageVO createNewLaborRecord(Integer staffId, String staffName, String time) {
        LaborManageVO newLabor = new LaborManageVO();
        newLabor.setId(0);
        newLabor.setStaffId(staffId);
        newLabor.setTime(time);
        newLabor.setStaffNameKanji(staffName);
        newLabor.setLaborStatus(0); // 默认状态
        newLabor.setBaseSalary(null);
        newLabor.setPositionAllowance(null);
        newLabor.setOvertimeAllowance(null);
        newLabor.setOtherAllowances(null);
        newLabor.setAdvancePayment(null);
        newLabor.setCommutingExpense(null);
        newLabor.setTaxablePaymentTotal(null);
        newLabor.setHealthInsuranceFee(null);
        newLabor.setNursingInsuranceFee(null);
        newLabor.setWelfarePensionInsurance(null);
        newLabor.setEmploymentInsuranceFee(null);
        newLabor.setOtherLaborExpenses(null);
        newLabor.setTotalLaborExpenses(null);
        return newLabor;
    }


    @Override
    @Transactional
    public void updateLaborManage(List<LaborManageVO> laborManageVOList) {
//        String time = laborManageVOList.get(0).getTime();
        for (LaborManageVO laborManageVO : laborManageVOList) {
            if(laborManageVO.getId() == 0) {
                laborManageDao.insertLaborManage(laborManageVO);
            } else {
                laborManageDao.updateLaborManage(laborManageVO);
            }
        }

    }


    @Override
    @Transactional
    public void updateLaborStatusBatch(List<LaborManageVO> laborManageVOList) {
        if (laborManageVOList == null || laborManageVOList.isEmpty()) {
            throw new IllegalArgumentException("労務情報リストが空です");
        }

        // 提取 ID 和 laborStatus（假设所有记录都应具有相同的 laborStatus）
        List<Integer> ids = laborManageVOList.stream()
                .map(LaborManageVO::getId)
                .collect(Collectors.toList());
        Integer laborStatus = laborManageVOList.get(0).getLaborStatus();

        if (laborStatus == null || (laborStatus != 1 && laborStatus != 2)) {
            throw new IllegalArgumentException("無効なステータス値です");
        }

        // Step 1: 更新原始ID记录
        laborManageDao.saveStatusBatch(ids, laborStatus);

        // Step 2: 若状态为 1，则进行“后续月数据继承”逻辑
        if (laborStatus == 1) {
            for (LaborManageVO base : laborManageVOList) {
                if (base == null) continue;
                laborManageDao.updateLaborManage(base);
                String baseMonth = base.getTime();   // e.g., "2024-04"
                Integer staffId = base.getStaffId();

                // 计算周期范围：n年2月到n+1年1月
                int year = Integer.parseInt(baseMonth.substring(0, 4));
                int month = Integer.parseInt(baseMonth.substring(5, 7));

                int startYear = (month >= 2) ? year : year - 1;
                List<String> cycleMonths = generateCycleMonths(startYear);

                // 找出当前月之后的周期月份（不含当前月）
                List<String> targetMonths = cycleMonths.stream()
                        .filter(m -> m.compareTo(baseMonth) > 0)
                        .collect(Collectors.toList());

                if (targetMonths.isEmpty()) continue;

                // 查询该员工后续月份的 laborStatus != 1 的记录
                List<LaborManageVO> targetRecords = laborManageDao.getLaborManageByTimeRangeAndStaffId(staffId, targetMonths);
                List<LaborManageVO> updatedList = new ArrayList<>();

                for (LaborManageVO record : targetRecords) {
                    if (record.getLaborStatus() != 1) {
                        copyLaborFields(base, record);
                        updatedList.add(record);
                    }
                }

                if (!updatedList.isEmpty()) {
                    laborManageDao.updateLaborBatch(updatedList);
                }
            }
        }
    }
//空记录初始化
private void initializeEmptyLaborRecordsForCycleIfMissing(List<LaborManageVO> activeEmployees, int startYear) {
    List<String> cycleMonths = generateCycleMonths(startYear);
    List<LaborManageVO> toInsert = new ArrayList<>();

    for (LaborManageVO employee : activeEmployees) {
        Integer staffId = employee.getStaffId();
        String staffName = employee.getStaffNameKanji();

        for (String month : cycleMonths) {
            boolean exists = laborManageDao.existsLaborRecord(staffId, month);
            if (!exists) {
                LaborManageVO empty = createNewLaborRecord(staffId, staffName, month);
                toInsert.add(empty);
            }
        }
    }

    if (!toInsert.isEmpty()) {
        laborManageDao.batchInsertLaborManage(toInsert);
    }
}




    @Override
    public List<Integer> getLaborStatusByStaffIds(List<Integer> staffIds, String time) {
        return laborManageDao.getLaborStatusByStaffIds(staffIds, time);
    }

}


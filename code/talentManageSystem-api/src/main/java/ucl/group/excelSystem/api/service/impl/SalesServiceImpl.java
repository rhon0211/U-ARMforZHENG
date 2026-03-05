package ucl.group.excelSystem.api.service.impl;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucl.group.excelSystem.api.db.dao.SalesDao;
import ucl.group.excelSystem.api.db.pojo.vo.SalesVO;
import ucl.group.excelSystem.api.service.SalesService;
import ucl.group.talentManageSystem.api.exception.ServiceException;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Setter
@Service
public class SalesServiceImpl implements SalesService {
    @Autowired
    private SalesDao salesDao;

    @Override
    //  获取销售数据，设置匹配的开始时间和结束时间
    /*
     * 1.终了预定日在期间，导出开始到终了
     * 2.终了预定日在期间之后，导出期间内的数据
     * 3.终了预定日在期间之前，不导出
     * 4.按照终了日导出
     * 5.终了和预定都没有，则导出期间
     */
    public List<SalesVO> getData(String fiscalYear) {
        LocalDate start = LocalDate.of(Integer.parseInt(fiscalYear), 3, 1);
        LocalDate end = LocalDate.of(Integer.parseInt(fiscalYear) + 1, 2, 28);
        //  获取有效项目ID
        List<Integer> projectIds = getValidProjectIds(start, end);
        if (projectIds.isEmpty()) {
            throw new ServiceException("対象プロジェクトがありません");
        }
        //  获取销售数据
        List<SalesVO> vos = salesDao.getData(projectIds, start, end);
        if (vos == null || vos.isEmpty()) {
            throw new ServiceException("結果が空です");
        }
        //  格式化和丰富销售数据
        formatAndEnrichSalesData(vos);
        return extrapolateMissingSalesMonths(vos);
    }
    //  获取有效项目ID
    private List<Integer> getValidProjectIds(LocalDate start, LocalDate end) {
        List<HashMap<String, Object>> rawProjects = salesDao.getProjects();
        return rawProjects.stream()
                .filter(p -> {
                    LocalDate s = toLocalDate(p.get("projectStartDate"));
                    LocalDate e = toLocalDate(p.get("projectEndDate"));
                    LocalDate se = toLocalDate(p.get("projectScheduledEndDate"));
                    LocalDate effectiveEnd = (e != null) ? e : (se != null ? se : end);
                    return (s == null || !s.isAfter(end)) && !effectiveEnd.isBefore(start);
                })
                .map(p -> (Integer) p.get("projectId"))
                .collect(Collectors.toList());
    }
    //  格式化和丰富销售数据
    private void formatAndEnrichSalesData(List<SalesVO> vos) {
        DateTimeFormatter inputFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFmt = DateTimeFormatter.ofPattern("yyyy-MM");

        for (SalesVO vo : vos) {
            LocalDate date = LocalDate.parse(vo.getApplicableMonth(), inputFmt);
            vo.setApplicableMonth(date.format(outputFmt));
            vo.setDepartmentAbbreviation(
                    vo.getDepartmentAbbreviation() + " " + vo.getPrincipalName());
            if (vo.getTotalSalesAmount() == null || vo.getTotalAmountRaised() == null) {
                vo.setTotalSalesAmount(Optional.ofNullable(salesDao.getSalesAmount(vo.getProjectId()))
                        .orElse(BigDecimal.ZERO));
                vo.setTotalAmountRaised(Optional.ofNullable(salesDao.getAmountRaised(vo.getProjectId()))
                        .orElse(BigDecimal.ZERO));
                vo.setTradingStatus("null");
            }

            List<Integer> staffIds = salesDao.getStaffIds(vo.getProjectId());
            if (staffIds == null || staffIds.isEmpty()) {
                vo.setTotalLaborExpenses(BigDecimal.ZERO);
                continue;
            }
            BigDecimal expenses = salesDao.getTotalLaborExpenses(staffIds);
            vo.setTotalLaborExpenses(expenses == null ? BigDecimal.ZERO : expenses);
        }
    }

    //  补充缺失的销售月份
    private List<SalesVO> extrapolateMissingSalesMonths(List<SalesVO> vos) {
        Map<Integer, List<SalesVO>> grouped = vos.stream()
                .collect(Collectors.groupingBy(SalesVO::getProjectId));

        List<SalesVO> result = new ArrayList<>();

        for (Map.Entry<Integer, List<SalesVO>> entry : grouped.entrySet()) {
            Integer projectId = entry.getKey();
            List<SalesVO> projectVos = entry.getValue();
            Set<String> existingMonths = projectVos.stream()
                    .map(SalesVO::getApplicableMonth)
                    .collect(Collectors.toSet());

            projectVos.sort(Comparator.comparing(SalesVO::getApplicableMonth));
            SalesVO baseVO = projectVos.get(projectVos.size() - 1);
            LocalDate lastMonth = LocalDate.parse(baseVO.getApplicableMonth() + "-01");
            LocalDate endDate = getEffectiveEndDate(projectId);
            if (endDate == null || endDate.isBefore(lastMonth)) {
                result.addAll(projectVos);
                continue;
            }

            for (LocalDate month = lastMonth.plusMonths(1);
                 !month.isAfter(endDate);
                 month = month.plusMonths(1)) {
                String monthStr = month.format(DateTimeFormatter.ofPattern("yyyy-MM"));
                if (existingMonths.contains(monthStr)) continue;
                Map<String, List<BigDecimal>> staffMap = salesDao.getStaffFinancials(projectId, month)
                        .stream()
                        .collect(Collectors.toMap(
                                m -> m.get("staff_id").toString(),
                                m -> Arrays.asList(
                                        (BigDecimal) m.get("sales_amount"),
                                        (BigDecimal) m.get("amount_raised")
                                )
                        ));

                List<String> retiredStaff = salesDao.getRetiredStaff(projectId, month.withDayOfMonth(1));
                BigDecimal retiredSales = BigDecimal.ZERO;
                BigDecimal retiredRaise = BigDecimal.ZERO;
                for (String retired : retiredStaff) {
                    List<BigDecimal> amounts = staffMap.getOrDefault(retired, Arrays.asList(BigDecimal.ZERO, BigDecimal.ZERO));
                    retiredSales = retiredSales.add(amounts.get(0));
                    retiredRaise = retiredRaise.add(amounts.get(1));
                }
                BigDecimal finalSales = baseVO.getTotalSalesAmount().subtract(retiredSales);
                BigDecimal finalRaise = baseVO.getTotalAmountRaised().subtract(retiredRaise);
                SalesVO newVo = cloneVO(baseVO);
                newVo.setApplicableMonth(monthStr);
                newVo.setTotalSalesAmount(finalSales);
                newVo.setTotalAmountRaised(finalRaise);
                newVo.setTradingStatus("null");
                projectVos.add(newVo);
            }

            result.addAll(projectVos);
        }

        return result;
    }

    private SalesVO cloneVO(SalesVO vo) {
        SalesVO newVo = new SalesVO();
        newVo.setProjectId(vo.getProjectId());
        newVo.setCompanyAbbreviation(vo.getCompanyAbbreviation());
        newVo.setDepartmentAbbreviation(vo.getDepartmentAbbreviation());
        newVo.setProjectNameAbbreviation(vo.getProjectNameAbbreviation());
        newVo.setTradingStatus(vo.getTradingStatus());
        newVo.setTotalLaborExpenses(vo.getTotalLaborExpenses());
        return newVo;
    }

    private LocalDate getEffectiveEndDate(Integer projectId) {
        Date end = salesDao.getProjectEndDate(projectId);
        Date scheduled = salesDao.getProjectScheduledEndDate(projectId);
        if (end != null) return end.toLocalDate();
        if (scheduled != null) return scheduled.toLocalDate();
        return LocalDate.of(LocalDate.now().getYear() + 1, 2, 28);
    }

    private LocalDate toLocalDate(Object obj) {
        if (obj instanceof Date) {
            return ((Date) obj).toLocalDate();
        }
        return null;
    }

}
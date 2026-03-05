package ucl.group.excelSystem.api.service.impl;


import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucl.group.excelSystem.api.db.dao.TransactionDetailDao;
import ucl.group.excelSystem.api.db.pojo.BasicClearingBalanceEntity;
import ucl.group.excelSystem.api.db.pojo.BasicReceivableAndPayable;
import ucl.group.excelSystem.api.db.pojo.BasicTradingManagementEntity;
import ucl.group.excelSystem.api.db.pojo.BasicTransactionDetailEntity;
import ucl.group.excelSystem.api.db.pojo.vo.BasicClearBalance1Entity;
import ucl.group.excelSystem.api.service.LaborManageService;
import ucl.group.excelSystem.api.service.ProjectService;
import ucl.group.excelSystem.api.service.StaffManagementService;
import ucl.group.excelSystem.api.service.TransactionDetailService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"rawtypes", "SuspiciousMethodCalls"})
@Service
public class TransactionDetailServiceImpl implements TransactionDetailService {
    private final TransactionDetailDao transactionDetailDao;
    private final ProjectService projectService;
    private final StaffManagementService staffManagementService;
    private final LaborManageService laborManageService;

    //构造器注入
    @Autowired
    public TransactionDetailServiceImpl(TransactionDetailDao transactionDetailDao,
                                        ProjectService projectService,
                                        StaffManagementService staffManagementService,
                                        LaborManageService laborManageService) {
        this.transactionDetailDao = transactionDetailDao;
        this.projectService = projectService;
        this.staffManagementService = staffManagementService;
        this.laborManageService = laborManageService;
    }
    // ================= 工具方法 ===================
    // 计算工数
    private double calculateOperatingTime(Integer projectId, String month) {
        Double hours = projectService.getDailyOperatingHoursByProjectId(projectId);
        Integer days = Integer.parseInt(projectService.getWorkDaysByMonth(month));
        return hours * days * 1.00;
    }
    // 计算超出时间
    private BigDecimal calculateOverTime(BigDecimal opTime, BigDecimal lower, BigDecimal upper) {
        if (opTime.compareTo(lower) < 0) return opTime.subtract(lower);//得出负值
        if (opTime.compareTo(upper) > 0) return opTime.subtract(upper);//得出正值
        return BigDecimal.ZERO;
    }
    // 计算动态金额，在贩卖金额和调达金额中使用
    private BigDecimal calcDynamicAmount(BigDecimal base, BigDecimal over, BigDecimal inc, BigDecimal dec) {
        if (over.signum() == 0) return base;
        return over.signum() > 0 ? inc.multiply(over).add(base) : dec.multiply(over).add(base);
    }
    // 计算结算日
    private LocalDate getSettlementDate(String closingDateStr, LocalDate currentDate) {
        return switch (closingDateStr) {
            case "末日締め" -> currentDate.withDayOfMonth(currentDate.lengthOfMonth());//每月最后一天
            case "20日締め" -> LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 20);
            case "15日締め" -> LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 15);
            case "10日締め" -> LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 10);
            default -> currentDate;
        };
    }
    // 获取上个月的日期
    private String getPreviousMonth(String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        return date.minusMonths(1).toString();
    }
    // 获取当前日期
    private LocalDate parseDate(String dateStr) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Override
    public HashMap<String, BigDecimal> getSettlementUpperAndLowerLimit(Integer projectId) {
        return projectService.getUpperAndLowerLimit(projectId);
    }
    /**
     * todo 这个方法里有很多重复代码，可以抽取成独立的方法
     * 买卖管理和工数金额入力是连起来，所以是一个人完成的
     * 如果要修改的话，也尽量一个人理解代码和修改
     * 逻辑会更连贯一点
     * 注释我尽量都写了，所以看代码会更好理解一点
     */
    public void doData(Integer projectId, String month) {
        if (transactionDetailDao.isExistTradingManage(projectId, month)) {
            LocalDate monthDate = LocalDate.parse(month); // 例如 "2024-05-01"
            String lastDayOfMonth = monthDate.with(TemporalAdjusters.lastDayOfMonth()).toString(); // "2024-05-31"
            List<Integer> tradingIds = transactionDetailDao.checkProjectIdAndMonth(projectId, month,lastDayOfMonth);
            for (Integer tradingId : tradingIds) {
                transactionDetailDao.deleteTransactionDetailsByTradingId(tradingId);
//                transactionDetailDao.deleteClearingBalanceByTradingId(tradingId);
                transactionDetailDao.deleteTradingManagement(tradingId);
            }
            return; // 删除完不再继续执行下去
        }
        laborManageService.getLaborManageByPage(month.substring(0, 7), 1, 10);
        Integer salesCompanyId = projectService.getSalesCompanyId(projectId);
        Integer departmentId = projectService.getDepartmentId(projectId);
        String closingDateStr = projectService.getClosingDate(salesCompanyId);
        LocalDate currentDate = parseDate(month);//将传入的字符串月份转换为 LocalDate
        //计算结算日
        LocalDate settlementDate = getSettlementDate(closingDateStr, currentDate);
        //将结算日转换为字符串格式
        String settlementDateStr = settlementDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        //想定贩卖金额
        BigDecimal sumSalesAmount = projectService.getSumSalesAmount(projectId);
        //贩卖金额（是总的贩卖金额）所有要员的贩卖金额的合计
        BigDecimal totalSalesAmount = projectService.getTotalSalesAmountByProjectId(projectId);
        //前月残精算额 = 上月的当月残精算额
        String preMonth = getPreviousMonth(month);
        // 计算上个月的清算金额
        BigDecimal prevResidual = Optional.ofNullable(
                projectService.getTheMonthOfResidualAmount(projectId, preMonth)).orElse(BigDecimal.ZERO);
        // 定义日期格式
        LocalDate lastDay = currentDate.with(TemporalAdjusters.lastDayOfMonth());//和withDayOfMonth方法作用一样，获取该月的最后一天
        //查询的是在入场日和退场日之内的要员
        List<HashMap> staffs = transactionDetailDao.getStaffsByProjectId(projectId, lastDay.toString(), month +"-01");
        if (staffs == null || staffs.isEmpty()) {
            // 插入仅用于标记的交易管理记录
            BasicTradingManagementEntity manageEntity = BasicTradingManagementEntity.builder()
                    .applicableMonth(settlementDateStr)
                    .tradingStatus(0)
                    .salesCompanyId(salesCompanyId)
                    .departmentId(departmentId)
                    .projectId(projectId)
                    .totalOverTime(0.0F)
                    .totalOperatingTime(0.0F)
                    .totalSalesAmount(BigDecimal.ZERO)
                    .totalAmountRaised(BigDecimal.ZERO)
                    .estimatedSalesAmount(sumSalesAmount)
                    .totalSettlementAmount(BigDecimal.ZERO)
                    .currentMonthBillingAmount(BigDecimal.ZERO)
                    .theMonthOfResidualAmount(prevResidual)
                    .build();

            transactionDetailDao.insertTradingManagement(manageEntity);

            // 不插入交易明细和清算余额，只记录管理表
            return;
        }
        // 过滤掉 companyAbbreviation 为 "UCL" 的项，获取 staffId
        List<Integer> staffIds = staffs.stream()
                .filter(staff -> !"UCL".equals(staff.get("companyAbbreviation")))
                .map(staff -> (Integer) staff.get("staffId"))
                .collect(Collectors.toList());
        // 用staffIds查询调达金额,即除去 ucl 员工的调达金额和
        BigDecimal totalAmountRaised = staffIds.isEmpty() ? BigDecimal.ZERO
                : transactionDetailDao.getAmountRaised(staffIds, projectId);
        // 插入交易管理表

        BasicTradingManagementEntity manageEntity = BasicTradingManagementEntity.builder()
                .applicableMonth(settlementDateStr)
                .tradingStatus(0)
                .salesCompanyId(salesCompanyId)
                .departmentId(departmentId)
                .projectId(projectId)
                .totalOverTime(0.0F)
                .totalOperatingTime(0.0F)
                .totalSalesAmount(totalSalesAmount)
                .totalAmountRaised(totalAmountRaised)
                .estimatedSalesAmount(sumSalesAmount)
                .totalSettlementAmount(BigDecimal.ZERO)
                .currentMonthBillingAmount(BigDecimal.ZERO)
                .theMonthOfResidualAmount(BigDecimal.ZERO)
                .build();
        transactionDetailDao.insertTradingManagement(manageEntity);

        Integer tradingId = manageEntity.getTradingId();

        // 插入交易明细表，计算工时
        double opTime = calculateOperatingTime(projectId, month);
        BigDecimal opTimeBD = BigDecimal.valueOf(opTime);
        //取得上下限，用key-value的形式存储，方便后续使用
        HashMap<String, BigDecimal> limits = projectService.getUpperAndLowerLimit(projectId);
        BigDecimal overTime = calculateOverTime(opTimeBD, limits.get("settlementLowerLimit"), limits.get("settlementUpperLimit"));
        double totalOverTime = 0.0;
        double totalOperatingTime = 0.0;
        BigDecimal addSales = BigDecimal.ZERO;
        BigDecimal addRaised = BigDecimal.ZERO;
        List<BasicTransactionDetailEntity> results = new ArrayList<>();//遍历staffs，存放每个staff的明细
        for (HashMap staff : staffs) {
            Integer staffId = (Integer) staff.get("staffId");
            //判断是否是ucl员工，如果是ucl员工，amountRaised为-9999.00。
            boolean isUcl = projectService.getAmountRaisedByProjectIdAndStaffId(projectId, staffId)
                    .compareTo(BigDecimal.valueOf(-9999.00)) == 0;
            BasicTransactionDetailEntity entity = buildDetail(projectId, staffId, opTimeBD, overTime, isUcl);
            entity.setTradingId(tradingId);
            results.add(entity);//将每个staff的明细添加到results中
            totalOverTime += overTime.doubleValue();
            totalOperatingTime += opTime;
            addSales = addSales.add(entity.getSalesAmount());
            addRaised = addRaised.add(isUcl ? BigDecimal.ZERO : entity.getAmountRaised());
        }
        // 插入交易明细表
        BigDecimal actuarialAmount = sumSalesAmount.subtract(addSales);
        BigDecimal residualAmount = addSales.add(prevResidual).add(actuarialAmount);
        // 更新清算余额
        transactionDetailDao.updateTradingManagementTotals(
                totalOperatingTime, totalOverTime, addSales, addRaised,
                actuarialAmount, residualAmount, tradingId);
        if (results == null || results.isEmpty()) {
            throw new RuntimeException(String.format("无交易明细数据，不执行插入。projectId=%d, month=%s", projectId, month));
        } else {
            transactionDetailDao.insertTransactionDetails(results);
        }

        BasicClearingBalanceEntity balanceEntity = BasicClearingBalanceEntity.builder()
                .projectId(projectId)
                .targetMonth(settlementDateStr)
                .previousMonthActuarialBalance(prevResidual)
                .theMonthOfActuarialAmount(BigDecimal.ZERO)
                .theMonthOfResidualAmount(residualAmount)
                .build();

        transactionDetailDao.insertClearingBalance(balanceEntity);
    }

    private BasicTransactionDetailEntity buildDetail(Integer projectId, Integer staffId,
                                                     BigDecimal opTime, BigDecimal overTime, boolean isUcl) {
        //取得贩卖金额和调达金额基准
        BigDecimal baseSales = projectService.getSalesAmountByProjectIdAndStaffId(projectId, staffId);
        BigDecimal baseProcure = projectService.getAmountRaisedByProjectIdAndStaffId(projectId, staffId);
        //取得贩卖金额和调达金额的增减金以及上下限
        HashMap<String, BigDecimal> saleMap = projectService.getSalesIncrementAndDecrementUnitPriceHour(projectId, staffId);
        HashMap<String, BigDecimal> procureMap = projectService.getProcurementIncrementAndDecrementUnitPriceHour(projectId, staffId);
        HashMap<String, String> procureLimits = projectService.getAmountRaisedUpperAndLowerLimit(projectId, staffId);
        // 计算贩卖金额和调达金额的增减金
        BigDecimal procureOver = calculateOverTime(opTime,
                new BigDecimal(procureLimits.get("lowerLimit")), new BigDecimal(procureLimits.get("upperLimit")));
        BigDecimal sales = calcDynamicAmount(baseSales, overTime,
                saleMap.get("salesIncrementUnitPriceHour"), saleMap.get("salesDecrementUnitPriceHour"));
        BigDecimal procure = calcDynamicAmount(baseProcure, procureOver,
                procureMap.get("procurementIncrementUnitPriceHour"), procureMap.get("procurementDecrementUnitPriceHour"));
        return BasicTransactionDetailEntity.builder()
                .staffId(staffId)
                .procurementCompanyId(staffManagementService.getProcurementCompanyIdByStaffId(staffId))
                .operatingTime(opTime.doubleValue())
                .overTime(overTime.doubleValue())
                .salesAmount(sales)
                .amountRaised(isUcl ? BigDecimal.valueOf(-9999.00) : procure)
                .build();
    }
    @Transactional
    public void updateTransactionDetails(Integer projectId, String month) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentDate = parseDate(month);
        LocalDate lastDayOfMonth = currentDate.with(TemporalAdjusters.lastDayOfMonth());
        // 仅更新新要员
        List<HashMap> staffs = transactionDetailDao.getStaffsByProjectId(projectId, lastDayOfMonth.toString(), month + "-01");
        List<Integer> exists = transactionDetailDao.getStaffIdsByProjectIdApplicableMonth(projectId, month);
        staffs.removeIf(staff -> exists.contains(staff.get("staffId")));
        if (staffs.isEmpty()) return;
        Integer tradingId = transactionDetailDao.getTradingIdByProjectId(projectId, month);
        if (tradingId == null) return;
        // 想定贩卖金额
        BigDecimal estimatedSalesAmount = transactionDetailDao.getEstimatedSalesAmountByTradingId(tradingId);
        String preMonth = getPreviousMonth(month);
        BigDecimal prevResidual = Optional.ofNullable(
                projectService.getTheMonthOfResidualAmount(projectId, preMonth)).orElse(BigDecimal.ZERO);
        double opTime = calculateOperatingTime(projectId, month);
        BigDecimal opTimeBD = BigDecimal.valueOf(opTime);
        HashMap<String, BigDecimal> limits = projectService.getUpperAndLowerLimit(projectId);
        BigDecimal overTime = calculateOverTime(opTimeBD, limits.get("settlementLowerLimit"), limits.get("settlementUpperLimit"));
        double totalOverTime = 0.0;
        double totalOperatingTime = 0.0;
        BigDecimal addSales = BigDecimal.ZERO;
        BigDecimal addRaised = BigDecimal.ZERO;
        List<BasicTransactionDetailEntity> newDetails = new ArrayList<>();
        for (HashMap staff : staffs) {
            Integer staffId = (Integer) staff.get("staffId");
            boolean isUcl = projectService.getAmountRaisedByProjectIdAndStaffId(projectId, staffId)
                    .compareTo(BigDecimal.valueOf(-9999.00)) == 0;
            BasicTransactionDetailEntity entity = buildDetail(projectId, staffId, opTimeBD, overTime, isUcl);
            entity.setTradingId(tradingId);
            newDetails.add(entity);
            totalOverTime += overTime.doubleValue();
            totalOperatingTime += opTime;
            addSales = addSales.add(entity.getSalesAmount());
            addRaised = addRaised.add(isUcl ? BigDecimal.ZERO : entity.getAmountRaised());
        }
        BigDecimal actuarialAmount = estimatedSalesAmount.subtract(addSales);
        BigDecimal residualAmount = addSales.add(prevResidual).add(actuarialAmount);
        transactionDetailDao.updateClearBalance(actuarialAmount, residualAmount, projectId, month);
        transactionDetailDao.updateTradingManagementTotals(totalOperatingTime, totalOverTime, addSales, addRaised, actuarialAmount, residualAmount, tradingId);
        transactionDetailDao.insertTransactionDetails(newDetails);
    }


    @Transactional
    public List<BasicTransactionDetailEntity> queryAndUpdateTradingManagement(String month, Integer projectId) {
        LocalDate currentDate = parseDate(month);
        LocalDate lastDay = currentDate.with(TemporalAdjusters.lastDayOfMonth());
        // 先更新交易明细
        updateTransactionDetails(projectId, month);
        boolean exists = transactionDetailDao.isExist(projectId, month);
        if (exists) {
            Integer tradingId = transactionDetailDao.getTradingIdByProjectId(projectId, month);
            // ➤ 获取关联 staffId
            List<Integer> staffIds = transactionDetailDao.getStaffIdsByTradingId(tradingId);
            // ➤ 检查在岗状态，不在岗的就删
            for (Integer staffId : staffIds) {
                Integer inService = transactionDetailDao.checkStaffInService(
                        staffId, month + "-01", lastDay.toString()
                );
                // 如果不在岗，删除交易明细对应员工数据，删除交易管理表中该员工的销售金额
                if (inService == 0) {
                    transactionDetailDao.deleteTsdByTradingIdAndStaffId(tradingId, staffId);
                    Integer salesAmount = transactionDetailDao.getSalesAmount(projectId, staffId);
                    transactionDetailDao.deleteStaffSales(tradingId,salesAmount);
                }
            }
            return transactionDetailDao.getDetailsByMonthAndProjectWithTotals(month, projectId);
        } else {
            List<HashMap> staffs = transactionDetailDao.getStaffsByProjectId(projectId, lastDay.toString(), month + "-01");
            return staffs.stream().map(staff -> BasicTransactionDetailEntity.builder()
                    .tradingId(0)
                    .procurementCompanyName((String) staff.get("procurementCompanyName"))
                    .staffId((Integer) staff.get("staffId"))
                    .staffNameKanji((String) staff.get("staffNameKanji"))
                    .operatingTime(0.0)
                    .overTime(0.0)
                    .salesAmount(BigDecimal.ZERO)
                    .amountRaised(BigDecimal.ZERO)
                    .totalSalesAmount(BigDecimal.ZERO)
                    .totalOperatingTime(0.0)
                    .totalOverTime(0.0)
                    .build()).collect(Collectors.toList());
        }
    }
    @Override
    @Transactional
    public BasicClearBalance1Entity getClearBalanceData(String month, Integer projectId) {
        boolean exists = transactionDetailDao.isExistClearing(projectId, month);
        if (exists) {
            return transactionDetailDao.getClearBalanceData(month, projectId);
        } else {
            return BasicClearBalance1Entity.builder()
                    .theMonthActuarialAmount(BigDecimal.ZERO)
                    .currentMonthBillingAmount(BigDecimal.ZERO)
                    .theMonthResidualAmount(BigDecimal.ZERO)
                    .totalAmountRaised(BigDecimal.ZERO)
                    .previousMonthActuarialBalance(BigDecimal.ZERO)
                    .salesAmount(BigDecimal.ZERO)
                    .build();
        }
    }


    @Override
    @Transactional
    public void updateTransactionDetail(BasicTransactionDetailEntity transactionDetail) {
        Integer tradingId = transactionDetail.getTradingId();
        if (tradingId == 0) {
            //insert
            transactionDetailDao.insertTransactionDetail(transactionDetail);
        } else {
            //update
            transactionDetailDao.updateTransactionDetail(transactionDetail);
        }
    }

    @Override
    @Transactional
    public void updateBalances(BigDecimal theMonthActuarialAmount, BigDecimal currentMonthBillingAmount, BigDecimal theMonthResidualAmount,
                               BigDecimal salesAmount,BigDecimal totalAmountRaised,Integer projectId, String month) {

        // 如果 当月清算 theMonthActuarialAmount 不为空，则更新 basic_v2_clearing_balance
        if (theMonthActuarialAmount != null && theMonthResidualAmount != null) {
            transactionDetailDao.updateTheMonthActuarialAmount(theMonthActuarialAmount, theMonthResidualAmount, projectId, month);
        }
        // 如果 当月请求 currentMonthBillingAmount 不为空，则更新 basic_v2_trading_management
        if (currentMonthBillingAmount != null && salesAmount != null && totalAmountRaised != null) {
            transactionDetailDao.updateCurrentMonthBillingAmount(currentMonthBillingAmount, salesAmount, totalAmountRaised, projectId, month);
        }

//        更新状态为 1
//        transactionDetailDao.updateStatus(1, projectId, month);
    }



    @Override
    @Transactional
    public void updateTradingStatus(Integer projectId, String month) {

        Integer status = transactionDetailDao.selectTradingStatus(projectId, month);
        // 交易状态固定更新为 3
        int tradingStatus = 3;

        if (status < 3) {
            //卖
            BasicTradingManagementEntity basicTradingManagementEntity = transactionDetailDao.searchOne(projectId, month);
            //BasicReceivableAndPayable basicReceivableAndPayable = new BasicReceivableAndPayable();
            // company id
            Integer salesCompanyId = basicTradingManagementEntity.getSalesCompanyId();
            // 买卖类型
            Integer buyAndSellType = transactionDetailDao.getSaleOrProcurement(salesCompanyId);
            // 对象年月
            String targetYearAndMonth = basicTradingManagementEntity.getApplicableMonth();
            // 締日
            String closingDate = transactionDetailDao.getClosingDate(salesCompanyId);
            // 明细合计
            String lineTotal = transactionDetailDao.getTotalSettlementAmountThismonth(salesCompanyId, projectId, month);
            //出入金预定日
            //翌月末、翌々月末、翌月10日、翌月15日、翌月20日、翌月25日
            String expectedDateOfPayment = transactionDetailDao.getPaymentTerm(salesCompanyId);
            String expectedDateOfPaymentValue;

            //买
            List<Integer> procurementCompanyIds = transactionDetailDao.getProcurementCompanyIds(projectId, month);
            // 先判断是否有数据
            if (transactionDetailDao.countReceivable(salesCompanyId, buyAndSellType, String.valueOf(targetYearAndMonth)) > 0) {
                for (Integer procurementCompanyId : procurementCompanyIds) {
                    if (transactionDetailDao.countPayable(procurementCompanyId, 2, String.valueOf(targetYearAndMonth)) > 0) {
                        transactionDetailDao.updateTradingStatusByMonth(tradingStatus, projectId, month);
                        return;  // 直接返回，不执行插入操作
                    }
                }
            }
            expectedDateOfPaymentValue = getExpectedDateOfPaymentValue(month, expectedDateOfPayment);

            BigDecimal lineTotalValue = (lineTotal != null && !lineTotal.trim().isEmpty())
                    ? new BigDecimal(lineTotal)
                    : BigDecimal.ZERO;

            //卖
            BasicReceivableAndPayable basicReceivable = BasicReceivableAndPayable.builder()
                    .buyAndSellType(buyAndSellType)
                    .companyId(salesCompanyId)
                    .targetYearAndMonth(String.valueOf(targetYearAndMonth))
                    .closingDate(getClosingDateValue(month, closingDate))
                    .lineTotal(lineTotalValue)
                    .expectedDateOfPayment(expectedDateOfPaymentValue)
                    .build();
            transactionDetailDao.insertAccountsReceivableAndPayable(basicReceivable);

            //买
            for (Integer procurementCompanyId : procurementCompanyIds) {
                String closingDateValue = transactionDetailDao.getClosingDate(procurementCompanyId);
                String paymentTerm = transactionDetailDao.getPaymentTerm(salesCompanyId);

                BigDecimal totalAmountRaised = transactionDetailDao.getTotalAmountRaised(projectId, month);
                BasicReceivableAndPayable basicPayable = BasicReceivableAndPayable.builder()
                        .buyAndSellType(2)
                        .companyId(procurementCompanyId)
                        .targetYearAndMonth(String.valueOf(targetYearAndMonth))
                        .closingDate(getClosingDateValue(month, closingDateValue))
                        //调达金额
                        .lineTotal(totalAmountRaised)
                        .expectedDateOfPayment(getExpectedDateOfPaymentValue(month, paymentTerm))
                        .build();
                if (transactionDetailDao.countPayable(procurementCompanyId, 2, String.valueOf(targetYearAndMonth)) > 0) {
                    continue;  // 跳过当前公司，继续处理其他公司
                }
                transactionDetailDao.insertAccountsReceivableAndPayable(basicPayable);
            }
        }



        // 调用 Mapper 方法进行更新
        transactionDetailDao.updateTradingStatusByMonth(tradingStatus, projectId, month);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 将字符串转换为 LocalDate 对象
        LocalDate date = LocalDate.parse(month, formatter);

        // 计算下月的第一天
        LocalDate firstDayOfPreviousMonth = date.plusMonths(1).withDayOfMonth(1);
        // 格式化为所需的字符串格式
        String result = firstDayOfPreviousMonth.format(formatter);
        //将上月的数据更新到下个月
        //当月的当月残
        BigDecimal theMonthOfResidualAmount = transactionDetailDao.getTheMonthOfResidualAmount(projectId, month);
        if (theMonthOfResidualAmount != null) {
            //更新下月的前月残
            transactionDetailDao.updatePreviousMonthActuarialBalance(theMonthOfResidualAmount, projectId, result);
        }
    }

    private static String getExpectedDateOfPaymentValue(String month, String expectedDateOfPayment) {
        String expectedDateOfPaymentValue ;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 将字符串转换为 LocalDate
        LocalDate date = LocalDate.parse(month, formatter);

        // 翌月末
        LocalDate nextMonthEnd = date.plusMonths(1) // 加一个月
                .withDayOfMonth(1) // 设置为下个月的第一天
                .minusDays(1); // 返回前一天，即下个月的最后一天

        // 下个月25日
        expectedDateOfPaymentValue = switch (expectedDateOfPayment) {
            case "翌月末" -> nextMonthEnd.format(outputFormatter);  // 格式化为yyyy-MM-dd

            case "翌々月末" -> nextMonthEnd.plusMonths(1).format(outputFormatter);  // 计算下个月末并格式化

            case "翌月10日" -> date.plusMonths(1).withDayOfMonth(10).format(outputFormatter);  // 下个月10日

            case "翌月15日" -> date.plusMonths(1).withDayOfMonth(15).format(outputFormatter);  // 下个月15日

            case "翌月20日" -> date.plusMonths(1).withDayOfMonth(20).format(outputFormatter);  // 下个月20日

            case "翌月25日" -> date.plusMonths(1).withDayOfMonth(25).format(outputFormatter);
            default -> null;
        };
        return expectedDateOfPaymentValue;
    }

    @Nullable
    private static String getClosingDateValue(String month, String closingDate) {
        LocalDate dateValue = LocalDate.parse(month.substring(0, 7) + "-01");
        String closingDateValue = null;
        if (closingDate == null) {
            closingDateValue = month;
        } else {
            //末日締め、20日締め、15日締め、10日締め
            switch (closingDate) {
                case "末日締め":
                    closingDateValue = dateValue.withDayOfMonth(dateValue.lengthOfMonth()).toString();
                    break;
                case "20日締め":
                    closingDateValue = dateValue.withDayOfMonth(20).toString();
                    break;
                case "15日締め":
                    closingDateValue = dateValue.withDayOfMonth(15).toString();
                    break;
                case "10日締め":
                    closingDateValue = dateValue.withDayOfMonth(10).toString();
                    break;
                default:
                    break;
            }
        }
        return closingDateValue;
    }

//    public static void updateTradingManagement(String month, Integer projectId) {
//        transactionDetailDao.updateStatus(2, projectId, month);
//    }
    @Transactional
    @Override
    public void updateConfirmInput(Integer status, Integer projectId, String month) {
        transactionDetailDao.updateStatus(status, projectId, month);
    }



}

//    @Override
//    @Transactional
//    public void doData(Integer projectId, String month) {
//
//        //校验数据是否存在
//        if (transactionDetailDao.isExistTradingManage(projectId, month)) {
//            return;
//        }
//
//        laborManageService.getLaborManageByPage(month.substring(0, 7), 1, 10);
//
//        //1. trading_manage
//        Integer salesCompanyId = projectService.getSalesCompanyId(projectId);
//        Integer departmentId = projectService.getDepartmentId(projectId);
//        // 締日 转换
//        String closingDateString = projectService.getClosingDate(salesCompanyId);
//
//        // 将传入的字符串月份转换为 LocalDate
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate currentDate = LocalDate.parse(month, formatter);
//
//        int year = currentDate.getYear();
//        int monthValue = currentDate.getMonthValue();
//
//        LocalDate closingDate;
//
//        if (closingDateString == null) {
//            throw new RuntimeException("締日は空にできません");
//        }
//        // 根据结算日规则计算结算日
//        closingDate = switch (closingDateString) {
//            case "末日締め" -> // 每月最后一天
//                    currentDate.withDayOfMonth(currentDate.lengthOfMonth());
//            case "20日締め" -> // 每月20日
//                    LocalDate.of(year, monthValue, 20);
//            case "15日締め" -> // 每月15日
//                    LocalDate.of(year, monthValue, 15);
//            case "10日締め" -> // 每月10日
//                    LocalDate.of(year, monthValue, 10);
//            default -> null;
//        };
//        String settlementDate;
//        settlementDate = Objects.requireNonNullElseGet(closingDate, LocalDate::now).format(formatter);
//
//        //8 想定贩卖金额
//        BigDecimal sumSalesAmount = projectService.getSumSalesAmount(projectId);
//        //9 贩卖金额（是总的贩卖金额）所有要员的贩卖金额的合计
//        BigDecimal totalSalesAmount = projectService.getTotalSalesAmountByProjectId(projectId);
//        //11 前月残精算额 = 上月的当月残精算额
//        LocalDate date = LocalDate.parse(month);
//        // 计算上个月的同一天
//        LocalDate previousMonthSameDay = date.minusMonths(1);
//        String preMonth = previousMonthSameDay.toString();
//        BigDecimal previousMonthActuarialBalance = projectService.getTheMonthOfResidualAmount(projectId, preMonth);
//        if (previousMonthActuarialBalance == null) {
//            previousMonthActuarialBalance = BigDecimal.ZERO;
//        }
//
//        // 定义日期格式
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        // 将输入字符串转换为 LocalDate
//        LocalDate firstDayOfMonth = LocalDate.parse(month, dateTimeFormatter);
//        // 获取该月的最后一天
//        LocalDate lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());
//        //14 調達金額合計,除去 ucl 员工的 調達金額和
//        //查询的是在入场日和退场日之内的要员
//        List<HashMap> staffs = transactionDetailDao.getStaffsByProjectId(projectId, lastDayOfMonth.toString(), month);
//        List<Integer> staffIds = staffs.stream()
//                .filter(staff -> !staff.get("companyAbbreviation").equals("UCL"))  // 过滤掉 companyAbbreviation 为 "UCL" 的项
//                .map(staff -> (Integer) staff.get("staffId"))  // 提取 staffId
//                .collect(Collectors.toList());  // 收集到 List 中
//        BigDecimal totalAmountRaised = BigDecimal.ZERO;
//        if (!staffIds.isEmpty()) {
//            totalAmountRaised = transactionDetailDao.getAmountRaised(staffIds, projectId);
//        }
//        BasicTradingManagementEntity basicTradingManagementEntity = BasicTradingManagementEntity.builder()
//                .applicableMonth(settlementDate)
//                //默认为 0
//                .tradingStatus(0)
//                //查询
//                .salesCompanyId(salesCompanyId)
//                //查询
//                .departmentId(departmentId)
//                .projectId(projectId)
//                //超過時間合計 更新
//                .totalOverTime(0.0F)
//                //稼働時間合計 更新
//                .totalOperatingTime(0.0F)
//                //販売金額合計
//                .totalSalesAmount(totalSalesAmount)
//                //調達金額合計
//                .totalAmountRaised(totalAmountRaised)
//                //想定販売金額
//                .estimatedSalesAmount(sumSalesAmount)
//                // 当月清算金額合計
//                .totalSettlementAmount(BigDecimal.ZERO)
//                //12 当月請求金額
//                .currentMonthBillingAmount(BigDecimal.ZERO)
//                //当月残精算
//                .theMonthOfResidualAmount(BigDecimal.ZERO)
//                .build();
//        //插入
//        transactionDetailDao.insertTradingManagement(basicTradingManagementEntity);
//        //2.transaction_details
//        // 获取插入后的 tradingId
//        Integer tradingId = basicTradingManagementEntity.getTradingId();
//        List<BasicTransactionDetailEntity> result = new ArrayList<>();
//        // 稼働時間
//        Integer dailyOperatingHours = projectService.getDailyOperatingHoursByProjectId(projectId);
//        Integer workDays = Integer.valueOf(projectService.getWorkDaysByMonth(month));
//        double operatingTime = dailyOperatingHours * workDays * 1.0;
//        //  贩卖超过时间
//        //在上下限内为 0
//        //下限之下：时间 - 下限 （负值）
//        //上限之上：时间 - 上限 （正值）
//        HashMap<String, BigDecimal> limits = projectService.getUpperAndLowerLimit(projectId);
//        BigDecimal upperLimit = limits.get("settlementUpperLimit");
//        BigDecimal lowerLimit = limits.get("settlementLowerLimit");
//        BigDecimal operatingTimeBigDecimal = new BigDecimal(operatingTime);
//        // 初始化 overtime 为 0
//        BigDecimal time = BigDecimal.ZERO;
//        // 判断 operatingTime 与上下限的关系
//        if (operatingTimeBigDecimal.compareTo(lowerLimit) < 0) {
//            // 下限之下：时间 - 下限（负值）
//            time = operatingTimeBigDecimal.subtract(lowerLimit);  // 计算时间与下限的差值（负值）
//        } else if (operatingTimeBigDecimal.compareTo(upperLimit) > 0) {
//            // 上限之上：时间 - 上限（正值）
//            time = operatingTimeBigDecimal.subtract(upperLimit);  // 计算时间与上限的差值（正值）
//        }
//        double overTime = time.doubleValue();
//        //10 当月清算 = 想定贩卖 - 贩卖金额（正/负）
//        BigDecimal theMonthOfActuarialAmount ;
//        //13 当月残精算额 = 贩卖金额 + 当月请求 + 前月残精算额
//        BigDecimal theMonthOfResidualAmount = BigDecimal.ZERO;
//        double totalOverTime = 0.0;
//        double totalOperatingTime = 0.0;
//        BigDecimal addSalesAmount = BigDecimal.ZERO;
//        BigDecimal addAmountRaised = BigDecimal.ZERO;
//
//        if(!staffs.isEmpty()) {
//            for (HashMap hashMap : staffs) {
//
//                //販売金額 調達金額
//                /*販売金額は
//                超過時間０の以外の場合、
//                上限超過(+)→販売増単金×販売超過時間＋販売金額
//                下限未満(-)→販売減単金×販売超過時間＋販売金額
//
//                調達金額もおなじように動的計算
//                調達の上限下限情報で計算するため、取得が必要
//                上限超過→調達増単金×調達超過時間＋販売金額
//                下限未満→調達減単金×調達超過時間＋販売金額
//                */
//
//                //6 贩卖金额 project_detail 表里查
//                //只用 projectId 和 staffId 查,不符合最左匹配原则
//                Integer staffId = (Integer) hashMap.get("staffId");
//                BigDecimal salesAmount = BigDecimal.ZERO;
//
//                //7 调达金额 project_detail 表里查
//                BigDecimal amountRaised = BigDecimal.ZERO;
//                //贩卖
//                BigDecimal salesAmountByProjectIdAndStaffId = projectService.getSalesAmountByProjectIdAndStaffId(projectId, staffId);
//                //调达
//                BigDecimal amountRaisedByProjectIdAndStaffId = projectService.getAmountRaisedByProjectIdAndStaffId(projectId, staffId);
//
//                boolean isUcl = amountRaisedByProjectIdAndStaffId.compareTo(BigDecimal.valueOf(-9999.00)) == 0;
//
//                //販売増/减単金（時間）
//                HashMap<String, BigDecimal> salesIncrementAndDecrementUnitPriceHour = projectService.getSalesIncrementAndDecrementUnitPriceHour(projectId, staffId);
//                BigDecimal incrementPriceHour = salesIncrementAndDecrementUnitPriceHour.get("salesIncrementUnitPriceHour");
//                BigDecimal decrementPriceHour = salesIncrementAndDecrementUnitPriceHour.get("salesDecrementUnitPriceHour");
//
//                BigDecimal overTime2BigDecimal = BigDecimal.valueOf(overTime);
//                //贩卖的超过时间
//                if (overTime == 0.0) {
//                    salesAmount = salesAmountByProjectIdAndStaffId;
//                } else if (overTime >= 0.0) {
//                    salesAmount = incrementPriceHour.multiply(overTime2BigDecimal).add(salesAmountByProjectIdAndStaffId);
//                } else if (overTime < 0.0) {
//                    salesAmount = decrementPriceHour.multiply(overTime2BigDecimal).add(salesAmountByProjectIdAndStaffId);
//                }
//
//                //调达的上下限
//                HashMap<String, String> amountRaisedUpperAndLowerLimit = projectService.getAmountRaisedUpperAndLowerLimit(projectId, staffId);
//                BigDecimal amountRaisedUpperLimit = new BigDecimal(amountRaisedUpperAndLowerLimit.get("upperLimit"));
//                BigDecimal amountRaisedLowerLimit = new BigDecimal(amountRaisedUpperAndLowerLimit.get("lowerLimit"));
//                //调达的超过时间
//                // 初始化 overtime 为 0
//                BigDecimal amountRaisedOverTime = BigDecimal.ZERO;
//
//                //调达增减单金
//                HashMap<String, BigDecimal> procurementIncrementAndDecrementUnitPriceHour = projectService.getProcurementIncrementAndDecrementUnitPriceHour(projectId, staffId);
//                BigDecimal procurementIncrementPriceHour = procurementIncrementAndDecrementUnitPriceHour.get("procurementIncrementUnitPriceHour");
//                BigDecimal procurementDecrementPriceHour = procurementIncrementAndDecrementUnitPriceHour.get("procurementDecrementUnitPriceHour");
//
//                // 判断 operatingTime 与上下限的关系
//                if (operatingTimeBigDecimal.compareTo(amountRaisedLowerLimit) < 0) {
//                    // 下限之下：时间 - 下限（负值）
//                    amountRaisedOverTime = operatingTimeBigDecimal.subtract(amountRaisedLowerLimit);  // 计算时间与下限的差值（负值）
//                } else if (operatingTimeBigDecimal.compareTo(amountRaisedUpperLimit) > 0) {
//                    // 上限之上：时间 - 上限（正值）
//                    amountRaisedOverTime = operatingTimeBigDecimal.subtract(amountRaisedUpperLimit);  // 计算时间与上限的差值（正值）
//                }
//
//                if (amountRaisedOverTime.compareTo(BigDecimal.ZERO) == 0) {
//                    amountRaised = amountRaisedByProjectIdAndStaffId;
//                } else if (amountRaisedOverTime.compareTo(BigDecimal.ZERO) > 0) {
//                    // 上限之上：时间 - 上限（正值）
//                    amountRaised = procurementIncrementPriceHour.multiply(amountRaisedOverTime).add(amountRaisedByProjectIdAndStaffId);
//                } else if (amountRaisedOverTime.compareTo(BigDecimal.ZERO) < 0) {
//                    // 下限之下：时间 - 下限（负值）
//                    amountRaised = procurementDecrementPriceHour.multiply(amountRaisedOverTime).add(amountRaisedByProjectIdAndStaffId);
//                }
//
//                totalOverTime += overTime;
//                totalOperatingTime += operatingTime;
//                addSalesAmount = addSalesAmount.add(salesAmount);
//                addAmountRaised = addAmountRaised.add(isUcl ? BigDecimal.ZERO : amountRaised);
//
//                //获取调达公司 id
//                Integer procurementCompanyId = staffManagementService.getProcurementCompanyIdByStaffId(staffId);
//
//                BasicTransactionDetailEntity basicTransactionDetailEntity = BasicTransactionDetailEntity.builder()
//                        //获取插入后的 tradingId
//                        .tradingId(tradingId)
//                        //传入
//                        .procurementCompanyId(procurementCompanyId)
//                        //查询
//                        .staffId(staffId)
//                        //稼働時間
//                        .operatingTime(operatingTime)
//                        //超過時間
//                        .overTime(overTime)
//                        //販売金額
//                        .salesAmount(salesAmount)
//                        //調達金額
//                        .amountRaised(isUcl ? BigDecimal.valueOf(-9999.00) : amountRaised)
//                        .build();
//                result.add(basicTransactionDetailEntity);
//            }
//
//            //当月清算
//            theMonthOfActuarialAmount = sumSalesAmount.subtract(addSalesAmount);
//
//            //13 当月残精算额 = 贩卖金额 + 当月清算 + 前月残精算额 - 当月请求
//            theMonthOfResidualAmount = addSalesAmount.add(previousMonthActuarialBalance).add(theMonthOfActuarialAmount);
//
//            transactionDetailDao.updateTradingManagementTotals(totalOperatingTime, totalOverTime, addSalesAmount, addAmountRaised, theMonthOfActuarialAmount, theMonthOfResidualAmount, tradingId);
//
//            transactionDetailDao.insertTransactionDetails(result);
//        }
//
//
//        //3.clear_balance
//
//        BasicClearingBalanceEntity basicClearingBalanceEntity = BasicClearingBalanceEntity.builder()
//                //插入
//                .projectId(projectId)
//                .targetMonth(settlementDate)
//                //前月残精算额
//                .previousMonthActuarialBalance(previousMonthActuarialBalance)
//                //当月精算额
//                //.theMonthOfActuarialAmount(theMonthOfActuarialAmount)
//                .theMonthOfActuarialAmount(BigDecimal.ZERO)
//                //当月残精算额
//                .theMonthOfResidualAmount(theMonthOfResidualAmount)
//                .build();
//        transactionDetailDao.insertClearingBalance(basicClearingBalanceEntity);
//    }



//
//    todo 重复的代码可以抽取成方法
//    @Transactional
//    public void updateTransactionDetails(Integer projectId, String month) {
//
//        // 定义日期格式
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//        // 将输入字符串转换为 LocalDate
//        LocalDate firstDayOfMonth = LocalDate.parse(month, formatter);
//
//        // 获取该月的最后一天
//        LocalDate lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());
//
//        //修改
//        //根据projectId 查询要员信息
//        List<HashMap> staffs = transactionDetailDao.getStaffsByProjectId(projectId, lastDayOfMonth.toString(), month);
//
//
//        //todo 要员筛选 入场/退场日期
//        //List<Integer> staffIdsByProjectId = transactionDetailDao.getStaffIdsByProjectId(projectId);
//        List<Integer> staffIdsByProjectId = transactionDetailDao.getStaffIdsByProjectIdApplicableMonth(projectId,month);
//        staffs.removeIf(staff -> staffIdsByProjectId.contains(staff.get("staffId")));
//        if (staffs.isEmpty() ) {
//            return;
//        }
//
//        //查询trading_management里是否已经插入了数据
//        Integer tradingId = transactionDetailDao.getTradingIdByProjectId(projectId, month);
//        if (tradingId == null) {
//            return;
//        }
//
//        //校验是否有 0 （占位数据）
//        //Boolean exist = transactionDetailDao.isExistTransactionDetails(tradingId);
//
//        //插入要员数据
//        //4 稼働時間
//        Integer dailyOperatingHours = projectService.getDailyOperatingHoursByProjectId(projectId);
//        Integer workDays = Integer.valueOf(projectService.getWorkDaysByMonth(month));
//        double operatingTime = dailyOperatingHours * workDays * 1.0;
//
//        // 5 超过时间
//        //在上下限内为 0
//        //下限之下：时间 - 下限 （负值）
//        //上限之上：时间 - 上限 （正值）
//
//        HashMap<String, BigDecimal> limits = projectService.getUpperAndLowerLimit(projectId);
//        BigDecimal upperLimit = limits.get("settlementUpperLimit");
//        BigDecimal lowerLimit = limits.get("settlementLowerLimit");
//
//        BigDecimal operatingTimeBigDecimal = new BigDecimal(operatingTime);
//
//        // 贩卖overtime
//        BigDecimal time = BigDecimal.ZERO;
//
//        // 判断 operatingTime 与上下限的关系
//        if (operatingTimeBigDecimal.compareTo(lowerLimit) < 0) {
//            // 下限之下：时间 - 下限（负值）
//            time = operatingTimeBigDecimal.subtract(lowerLimit);  // 计算时间与下限的差值（负值）
//        } else if (operatingTimeBigDecimal.compareTo(upperLimit) > 0) {
//            // 上限之上：时间 - 上限（正值）
//            time = operatingTimeBigDecimal.subtract(upperLimit);  // 计算时间与上限的差值（正值）
//        }
//        double overTime = time.doubleValue();
//
//        //11 前月残精算额 = 上月的当月残精算额
//        LocalDate date = LocalDate.parse(month);
//        // 计算上个月的同一天
//        LocalDate previousMonthSameDay = date.minusMonths(1);
//        String preMonth = previousMonthSameDay.toString();
//        BigDecimal previousMonthActuarialBalance = projectService.getTheMonthOfResidualAmount(projectId, preMonth);
//        if (previousMonthActuarialBalance == null) {
//            previousMonthActuarialBalance = BigDecimal.ZERO;
//        }
//
//
//        //10 当月清算 = 想定贩卖 - 贩卖金额（正/负）
//        BigDecimal theMonthOfActuarialAmount;
//        BigDecimal estimatedSalesAmount = transactionDetailDao.getEstimatedSalesAmountByTradingId(tradingId);
//
//
//        //13 当月残精算额 = 贩卖金额 + 当月请求 + 前月残精算额
//        BigDecimal theMonthOfResidualAmount;
//
//
//
//        double totalOverTime = 0.0;
//        double totalOperatingTime = 0.0;
//        BigDecimal addSalesAmount = BigDecimal.ZERO;
//        BigDecimal addAmountRaised = BigDecimal.ZERO;
//
//        //删除 占位数据
//        //transactionDetailDao.deletePendingTransactionDetails(tradingId);
//
//        List<BasicTransactionDetailEntity> newData = new ArrayList<>();
//        for (HashMap hashMap : staffs) {
//            //6 贩卖金额
//            Integer staffId = (Integer) hashMap.get("staffId");
//            //BigDecimal salesAmount = projectService.getSalesAmountByProjectIdAndStaffId(projectId, staffId);
//
//            //7 调达金额 project_detail 表里查
//            //BigDecimal amountRaised = projectService.getAmountRaisedByProjectIdAndStaffId(projectId, staffId);
//
//            //販売増/减単金（時間）
//            HashMap<String, BigDecimal> salesIncrementAndDecrementUnitPriceHour = projectService.getSalesIncrementAndDecrementUnitPriceHour(projectId, staffId);
//            BigDecimal incrementPriceHour = salesIncrementAndDecrementUnitPriceHour.get("salesIncrementUnitPriceHour");
//            BigDecimal decrementPriceHour = salesIncrementAndDecrementUnitPriceHour.get("salesDecrementUnitPriceHour");
//            BigDecimal overTime2BigDecimal = BigDecimal.valueOf(overTime);
//
//            //6 贩卖金额
//            BigDecimal salesAmount = null;
//            BigDecimal salesAmountByProjectIdAndStaffId = projectService.getSalesAmountByProjectIdAndStaffId(projectId, staffId);
//
//            //7 调达金额 project_detail 表里查
//            BigDecimal amountRaised = null;
//            BigDecimal amountRaisedByProjectIdAndStaffId = projectService.getAmountRaisedByProjectIdAndStaffId(projectId, staffId);
//
//            boolean isUcl = salesAmountByProjectIdAndStaffId.compareTo(BigDecimal.valueOf(-9999.00)) == 0;
//
//            //贩卖的超过时间
//            if (overTime == 0.0) {
//                salesAmount = salesAmountByProjectIdAndStaffId;
//            } else if (overTime >= 0.0) {
//                salesAmount = incrementPriceHour.multiply(overTime2BigDecimal).add(salesAmountByProjectIdAndStaffId);
//            } else if (overTime < 0.0) {
//                salesAmount = decrementPriceHour.multiply(overTime2BigDecimal).add(salesAmountByProjectIdAndStaffId);
//            }
//
//            //调达的超过时间
//            // 初始化 overtime 为 0
//            BigDecimal amountRaisedOverTime = BigDecimal.ZERO;
//
//            //调达增减单金
//            HashMap<String, BigDecimal> procurementIncrementAndDecrementUnitPriceHour = projectService.getProcurementIncrementAndDecrementUnitPriceHour(projectId, staffId);
//            BigDecimal procurementIncrementPriceHour = procurementIncrementAndDecrementUnitPriceHour.get("procurementIncrementUnitPriceHour");
//            BigDecimal procurementDecrementPriceHour = procurementIncrementAndDecrementUnitPriceHour.get("procurementDecrementUnitPriceHour");
//
//            //调达的上下限
//            HashMap<String, String> amountRaisedUpperAndLowerLimit = projectService.getAmountRaisedUpperAndLowerLimit(projectId, staffId);
//            BigDecimal amountRaisedUpperLimit = new BigDecimal(amountRaisedUpperAndLowerLimit.get("upperLimit"));
//            BigDecimal amountRaisedLowerLimit = new BigDecimal(amountRaisedUpperAndLowerLimit.get("lowerLimit"));
//
//            // 判断 operatingTime 与上下限的关系
//            if (operatingTimeBigDecimal.compareTo(amountRaisedLowerLimit) < 0) {
//                // 下限之下：时间 - 下限（负值）
//                amountRaisedOverTime = operatingTimeBigDecimal.subtract(amountRaisedLowerLimit);  // 计算时间与下限的差值（负值）
//            } else if (operatingTimeBigDecimal.compareTo(amountRaisedUpperLimit) > 0) {
//                // 上限之上：时间 - 上限（正值）
//                amountRaisedOverTime = operatingTimeBigDecimal.subtract(amountRaisedUpperLimit);  // 计算时间与上限的差值（正值）
//            }
//
//            if (amountRaisedOverTime.compareTo(BigDecimal.ZERO) == 0) {
//                amountRaised = amountRaisedByProjectIdAndStaffId;
//            } else if (amountRaisedOverTime.compareTo(BigDecimal.ZERO) > 0) {
//                // 上限之上：时间 - 上限（正值）
//                amountRaised = procurementIncrementPriceHour.multiply(amountRaisedOverTime).add(amountRaisedByProjectIdAndStaffId);
//            } else if (amountRaisedOverTime.compareTo(BigDecimal.ZERO) < 0) {
//                // 下限之下：时间 - 下限（负值）
//                amountRaised = procurementDecrementPriceHour.multiply(amountRaisedOverTime).add(amountRaisedByProjectIdAndStaffId);
//            }
//
//            totalOverTime += overTime;
//            totalOperatingTime += operatingTime;
//            addSalesAmount = addSalesAmount.add(salesAmount);
//            addAmountRaised = addAmountRaised.add(isUcl ? BigDecimal.ZERO : amountRaised);
//
//
//            Integer procurementCompanyId = staffManagementService.getProcurementCompanyIdByStaffId(staffId);
//            newData.add(BasicTransactionDetailEntity.builder()
//                    .tradingId(tradingId)
//                    .staffId(staffId)
//                    .procurementCompanyId(procurementCompanyId)
//                    .operatingTime(operatingTime)
//                    .overTime(overTime)
//                    .salesAmount(salesAmount)
//                    .amountRaised(isUcl ? BigDecimal.valueOf(-9999.00) : amountRaised)
//                    .build());
//        }
//
//        theMonthOfActuarialAmount = estimatedSalesAmount.subtract(addSalesAmount);
//        //当月残精算额 = 贩卖金额 + 当月清算 + 前月残精算额 - 当月请求
//        theMonthOfResidualAmount = addSalesAmount.add(previousMonthActuarialBalance).add(theMonthOfActuarialAmount);
//
//
//        transactionDetailDao.updateClearBalance(theMonthOfActuarialAmount, theMonthOfResidualAmount, projectId, month);
//        transactionDetailDao.updateTradingManagementTotals(totalOperatingTime, totalOverTime, addSalesAmount, addAmountRaised, theMonthOfActuarialAmount, theMonthOfResidualAmount, tradingId);
//        transactionDetailDao.insertTransactionDetails(newData);
//
//
//    }

//查要员
//    @Transactional
//    public List<BasicTransactionDetailEntity> queryAndUpdateTradingManagement(String month, Integer projectId) {
//        // 定义日期格式
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//        // 将输入字符串转换为 LocalDate
//        LocalDate firstDayOfMonth = LocalDate.parse(month, formatter);
//
//        // 获取该月的最后一天
//        LocalDate lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());
//
//        //todo 更新要员信息
//        updateTransactionDetails(projectId, month);
//
//        //1.先去trading_manage 表中查询是否有数据
//        Boolean exist = transactionDetailDao.isExist(projectId, month);
//        List<BasicTransactionDetailEntity> details;
//        if (exist) {
//            // 查询数据并计算总和
//            details = transactionDetailDao.getDetailsByMonthAndProjectWithTotals(month, projectId);
//        } else {
//            //要员名等要查询
//            //数据条数通过项目下的要员数量决定
//            List<HashMap> staffs = transactionDetailDao.getStaffsByProjectId(projectId, lastDayOfMonth.toString(), month);
//            int size = staffs.size();
//            details = new ArrayList<>(size);
//            BasicTransactionDetailEntity tmp ;
//            for (HashMap hashMap : staffs) {
//                tmp = BasicTransactionDetailEntity.builder()
//                        //插入前没有 id
//                        .tradingId(0)
//                        //公司名 从 company 表里查
//                        .procurementCompanyName((String) hashMap.get("companyAbbreviation"))
//                        //要员 id 查询
//                        .staffId((Integer) hashMap.get("staffId"))
//                        //要员名 查询
//                        .staffNameKanji((String) hashMap.get("staffNameKanji"))
//                        .operatingTime(0.0)
//                        .overTime(0.0)
//                        .salesAmount(BigDecimal.ZERO)
//                        .amountRaised(BigDecimal.ZERO)
//                        .totalSalesAmount(BigDecimal.ZERO)
//                        .totalOperatingTime(0.0)
//                        .totalOverTime(0.0)
//                        .build();
//                details.add(tmp);
//            }
//        }
//
//        return details;
//    }

//清算
//    @Override
//    @Transactional
//    public BasicClearBalance1Entity getClearBalanceData(String month, Integer projectId) {
//        Boolean existClearing = transactionDetailDao.isExistClearing(projectId, month);
//        BasicClearBalance1Entity clearBalanceData;
//        if (existClearing) {
//            clearBalanceData = transactionDetailDao.getClearBalanceData(month, projectId);
//        } else {
//            clearBalanceData = BasicClearBalance1Entity.builder()
//                    .theMonthActuarialAmount(BigDecimal.ZERO)
//                    .currentMonthBillingAmount(BigDecimal.ZERO)
//                    .theMonthResidualAmount(BigDecimal.ZERO)
//                    .totalAmountRaised(BigDecimal.ZERO)
//                    .previousMonthActuarialBalance(BigDecimal.ZERO)
//                    .salesAmount(BigDecimal.ZERO)
//                    .build();
//        }
//        return clearBalanceData;
//    }
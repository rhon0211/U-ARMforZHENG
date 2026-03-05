package ucl.group.excelSystem.api.service.impl;

import cn.hutool.core.map.MapUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucl.group.excelSystem.api.db.dao.TradeDao;
import ucl.group.excelSystem.api.service.*;
import ucl.group.talentManageSystem.api.common.PageUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
@Service
@Slf4j
@SuppressWarnings({"rawtypes", "unchecked", "SuspiciousMethodCalls"})
public class TradeServiceImpl implements TradeService {

    @Resource
    private TradeDao tradeDao;

    @Resource
    private CompanyAndDepartmentService companyAndDepartmentService;

    @Resource
    private DepartmentService departmentService;

    @Resource
    private ProjectService projectService;

    @Resource
    private LaborManageService laborManageService;

    /**
     * 这个方法实现通过 project 和 日期查询，再去查询 company department project
     * 并且用到的表很多，后续可以改成 只用一个表 basic_v2_trading_management 查询到 id 再去各自的表里查询数据
     * 并且三个 for 循环拼接数据，所以需要优化
     * @param param 包含分页参数和查询日期的Map对象
     * @return 返回一个PageUtils对象，包含分页数据和分页信息
     */
    @Override
    @Transactional
    public PageUtils getByPage(Map<String, Object> param) {
        String date = (String) param.get("date");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate queryMonth;
        LocalDate lastDayOfMonth;

        //案件结束日 和 予定案件结束日的处理

        if (date == null || date.isEmpty()) {
            // 如果 date 为空，获取当前月份的第一天
            queryMonth = LocalDate.now().withDayOfMonth(1);  // 获取当前月的第一天
        } else {
            // 如果 date 不为空，解析 date 字符串为 LocalDate
            queryMonth = LocalDate.parse(date, formatter);  // 默认是每月的第一天
        }
        // 获取当前月的最后一天
        lastDayOfMonth = queryMonth.withDayOfMonth(queryMonth.lengthOfMonth());


        ArrayList<HashMap> list = null;

        // 数据准备
        // 通过时间去 project 表里查询
        List<Integer> projectIds = projectService.getProjectIds(queryMonth, lastDayOfMonth);
        //List<Integer> projectIds = tradeDao.getProjectIds(queryMonth);
        if(projectIds.isEmpty()) {
            return new PageUtils(list, 0, 1, 1);
        }
        //List<HashMap> departments = departmentService.getDepartmentsByProjectIds(projectIds);
        //List<Integer> departmentIds = departmentService.getDepartmentIdsByProjectIds(projectIds);
        //ArrayList<HashMap> companys = companyAndDepartmentService.selectSaleCompanyByDepartmentIdsByPage(departmentIds);

        //查询公司
        //应该根据 id 从 company 表里查
        ArrayList<HashMap> maps = companyAndDepartmentService.selectSaleCompanyByProjectIds(projectIds);
        //ArrayList<HashMap> maps = tradeDao.selectSaleCompanyByProjectId(projectIds);

        //去重
        List<HashMap> uniqueCompanysList  = new ArrayList<>(maps.stream()
                .collect(Collectors.toMap(
                        company -> company.get("companyId"), // 使用 companyId 作为键
                        company -> company,
                        (existing, replacement) -> existing)) // 如果存在重复的companyId，保留第一个
                .values());

        ArrayList<HashMap> companys = new ArrayList<>(uniqueCompanysList);
        //分页
        long count = companys.size();

        if (count > 0) {
            list = companys;
        } else {
            list = new ArrayList<>();
        }

        //companyId,companyAbbreviation,staffNum,totalSalesAmount,status,departmentList
        //公司
        for (HashMap company : list) {
            Integer companyId = (Integer) company.get("companyId");
            // 通过 project 来查询 department
            //List<HashMap> departmentList = departmentService.selectDepartmentsByCompanyId(companyId);

            //从 project departement 表查
            //1.先查出 project 对应的部门
            //2.再遍历 company 查他们对应的 department 取交集
            // 1
            List<HashMap> departmentsByProjectIds = tradeDao.getDepartmentsByProjectIds(projectIds);
            //2
            List<HashMap> departmentsByCompanyId = departmentService.getDepartmentsByCompanyId(companyId);
            //3 取交集
            List<HashMap> departments = departmentsByProjectIds.stream()
                    .filter(departmentsByCompanyId::contains) // 过滤出同时存在于 departmentsByCompanyId 的元素
                    .collect(Collectors.toList());

            //去重
            List<HashMap> departmentList = new ArrayList<>(departments.stream().collect(Collectors.toMap(
                    department -> department.get("departmentId"),
                    department -> department,
                    (existing, replacement) -> existing
            )).values());

            int companyStaffNum = 0;
            //departmentId,departmentAbbreviation,staffNum,totalSalesAmount,previousMonthActuarialBalance,status,projectList
            BigDecimal companyTotalSalesAmount = BigDecimal.ZERO;
            int companyStatus;
            boolean companyAllAreTwo = true;
            boolean companyHasZero = true;
            //部门
            for (HashMap department : departmentList) {
                Integer departmentId = (Integer) department.get("departmentId");
                //List<HashMap> projectList = projectService.selectProjectsByDepartmentId(departmentId);

                //从 project 表查
                List<HashMap> projects = projectService.selectProjectsByDepartmentId(departmentId);
                //筛选
                List<HashMap> projectList = projects.stream()
                        .filter(project -> projectIds.contains(project.get("projectId")))
                        .collect(Collectors.toList());

                //projectId,projectName,totalSalesAmount,previousMonthActuarialBalance,
                // monthlySettlement,tradingStatus,generalAffairs,businessConfirmation
                BigDecimal departmentTotalSalesAmount = BigDecimal.ZERO;
                BigDecimal departmentPreviousMonthActuarialBalance = BigDecimal.ZERO;
                Integer departmentStaffNum = 0;
                int departmentStatus;
                boolean allAreTwo = true;
                boolean hasZero = true;
                //项目
                for (HashMap project : projectList) {

                    Integer projectId = (Integer) project.get("projectId");
                    Integer staffNum = projectService.getStaffNumByProjectId(projectId, queryMonth, lastDayOfMonth);


                    //增加时间
                    BigDecimal totalSalesAmount =  new BigDecimal("-9999");
                    //清算金额
                    BigDecimal previousMonthActuarialBalance =  BigDecimal.ZERO;
                    BigDecimal previousMonthActuarialBalance1 = projectService.getPreviousMonthActuarialBalance(projectId, queryMonth);
                    if (previousMonthActuarialBalance1 != null) {
                        previousMonthActuarialBalance = previousMonthActuarialBalance1;
                    }
                    Integer status;

                    //売上
                    //验证数据是否存在
                    if (tradeDao.isExist(projectId,queryMonth)) {
                        totalSalesAmount = tradeDao.getTotalSalesAmountByProjectId(projectId,queryMonth);
                        previousMonthActuarialBalance = tradeDao.getPreviousMonthActuarialBalanceByProjectId(projectId,queryMonth);
                        status = tradeDao.getTradingStatus(projectId,queryMonth);
                    } else {
                        status = 0;
                    }
                    if(status == null) {
                        status = 0;
                    }
                    project.put("previousMonthActuarialBalance", previousMonthActuarialBalance);
                    project.put("totalSalesAmount", totalSalesAmount);

                    //工数入力 0/1/2
                    project.put("tradingStatus", status >= 2 ? 2 : status);
                    //总务入力 0/1
                    //如果项目里有UCL的社员,确认他们的劳务状态，若都为1，则总务入力为1，否则为0（按位 与运算&）
                    //如果没有则为 -
                    DateTimeFormatter matter = DateTimeFormatter.ofPattern("yyyy-MM");
                    String time = queryMonth.format(matter);

                    //查询当前 project 内的 staff 的公司是 ucl 的 staffId
                    List<Integer> staffIds = projectService.getStaffIdsByProjectId(projectId);
                    if (staffIds == null || staffIds.isEmpty()) {
                        project.put("generalAffairs", "-");
                    } else {
                        int result;
                        List<Integer> laborStatus = laborManageService.getLaborStatusByStaffIds(staffIds, time);

                        if (laborStatus != null && !laborStatus.isEmpty()) {
                            // 检查所有元素是否为 0
                            boolean allZero = laborStatus.stream().allMatch(x -> x == 0);
                            // 检查所有元素是否为 1
                            boolean allOne = laborStatus.stream().allMatch(x -> x == 1);
                            if (allZero) {
                                result = 0;
                            } else if (allOne) {
                                result = 1;
                            } else {
                                result = 2;
                            }
                            project.put("generalAffairs", result);}else {
                            project.put("generalAffairs", "-");
                        }
                    }

                    //project.put("generalAffairs", status == 3 ? 1 : 0);
                    //营业确认 0/1
                    project.put("businessConfirmation", status == 3 ? 1 : 0);
                    departmentTotalSalesAmount = departmentTotalSalesAmount.add(totalSalesAmount != null && totalSalesAmount.compareTo(new BigDecimal("-9999")) != 0 ? totalSalesAmount : BigDecimal.ZERO);
                    departmentPreviousMonthActuarialBalance = departmentPreviousMonthActuarialBalance.add(previousMonthActuarialBalance != null ? previousMonthActuarialBalance : BigDecimal.ZERO);
                    departmentStaffNum += staffNum;

                    status = status > 2 ? 2 : status;
                    if (status != 0) {
                        hasZero = false;
                    }
                    if (status != 2) {
                        allAreTwo = false;
                    }
                    //departmentStatus ^= status ^ 2;
                }
                if (hasZero) {
                    // 如果数组中有0，设置为0
                    departmentStatus = 0;
                } else if (allAreTwo) {
                    // 如果数组中所有元素都是2，设置为2
                    departmentStatus = 2;
                } else {
                    // 其余情况设置为1
                    departmentStatus = 1;
                }
                //如果都为 2 ,则结果应该是 0 ,如果不是 0 ,则 结果应该是 1 (入力完成)
                //departmentStatus = departmentStatus == 0 ? 1 : 0;
                department.put("departmentStatus", departmentStatus);
                department.put("departmentTotalSalesAmount", departmentTotalSalesAmount);
                department.put("departmentPreviousMonthActuarialBalance", departmentPreviousMonthActuarialBalance);
                department.put("departmentStaffNum", departmentStaffNum);
                department.put("projectList", projectList);
                companyTotalSalesAmount = companyTotalSalesAmount.add(departmentTotalSalesAmount);
                //采用异或判断状态是否都为 2
                if (departmentStatus != 0) {
                    companyHasZero = false;
                }
                if (departmentStatus != 2) {
                    companyAllAreTwo = false;
                }
                //companyStatus ^= departmentStatus ^ 2;
                companyStaffNum += departmentStaffNum;
            }
            //如果都为 2 ,则结果应该是 0 ,如果不是 0 ,则 结果应该是 1 (入力完成)
            company.put("departmentList", departmentList);
            //companyStatus = companyStatus == 0 ? 1 : 0;
            if (companyHasZero) {
                // 如果数组中有0，设置为0
                companyStatus = 0;
            } else if (companyAllAreTwo) {
                // 如果数组中所有元素都是2，设置为2
                companyStatus = 2;
            } else {
                // 其余情况设置为1
                companyStatus = 1;
            }
            company.put("companyStatus", companyStatus);
            company.put("companyTotalSalesAmount", companyTotalSalesAmount);
            company.put("companyStaffNum", companyStaffNum);
        }

        // 从参数中获取当前页码
        int page = MapUtil.getInt(param, "page");

        // 从参数中获取每页长度
        int length = MapUtil.getInt(param, "length");
        int start = (int) param.get("start");
        if(length > count) {
            length = (int) count;
        }
        // 创建并返回一个包含分页信息和客户信息列表的PageUtils对象
        return new PageUtils(list.subList(start, length), count, page, length);

    }

}

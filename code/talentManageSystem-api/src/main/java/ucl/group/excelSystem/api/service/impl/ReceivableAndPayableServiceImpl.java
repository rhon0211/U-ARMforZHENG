package ucl.group.excelSystem.api.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucl.group.excelSystem.api.db.dao.ReceivableAndPayableDao;
import ucl.group.excelSystem.api.db.pojo.vo.ReceivableAndPayableVO;
import ucl.group.excelSystem.api.service.ReceivableAndPayableService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class ReceivableAndPayableServiceImpl implements ReceivableAndPayableService {

    @Resource
    private ReceivableAndPayableDao receivableAndPayableDao;

    @FunctionalInterface
    public interface QuadFunction<A, B, C, D, R> {
        R apply(A a, B b, C c, D d);
    }
    @Override
    @Transactional
    public PageInfo<ReceivableAndPayableVO> getReceivableByPageHelper(Map<String, Object> param) {
        int length = (int) param.get("length");
        int page = (int) param.get("page");
//        int start = (int) param.get("start");,不需要偏移量。
        String[] expectedDateOfPayment = (String[]) param.get("expectedDateOfPayment");//如初金预定日
        String[] closingDate = (String[]) param.get("closingDate");//谛日

        List<ReceivableAndPayableVO> receivableByPageHelper;

        PageHelper.startPage(page, length);
        if ((expectedDateOfPayment == null || expectedDateOfPayment.length == 0)
                && (closingDate == null || closingDate.length == 0)) {
            receivableByPageHelper = receivableAndPayableDao.getReceivableByPageHelper();
        } else {
            receivableByPageHelper = getDataByDateRange(
                    expectedDateOfPayment,
                    closingDate,
                    receivableAndPayableDao::getReceivableByDateRange
            );
        }

        for (ReceivableAndPayableVO vo : receivableByPageHelper) {
            Integer companyId = vo.getCompanyId();
            ArrayList<HashMap> receivableList = receivableAndPayableDao.getReceivableList(companyId, vo.getTargetYearAndMonth());
            List<HashMap> uniqueList = new ArrayList<>(receivableList.stream()
                    .collect(Collectors.toMap(
                            map -> map.get("projectAbbreviation"),
                            map -> map,
                            (existing, replacement) -> existing
                    ))
                    .values());

            BigDecimal lineTotal = BigDecimal.ZERO;
            for (HashMap map : uniqueList) {
                map.put("closingDate", vo.getClosingDate());
                lineTotal = lineTotal.add((BigDecimal) map.get("totalSalesAmount"));
            }
            vo.setList(uniqueList);
            vo.setLineTotal(lineTotal);
        }

        return new PageInfo<>(receivableByPageHelper);
    }

    @Override
    @Transactional
    public PageInfo<ReceivableAndPayableVO> getPayableByPageHelper(Map<String, Object> param) {
        int length = (int) param.get("length");
//        int start = (int) param.get("start");
        int page = (int) param.get("page");
        String[] expectedDateOfPayment = (String[]) param.get("expectedDateOfPayment");
        String[] closingDate = (String[]) param.get("closingDate");

        List<ReceivableAndPayableVO> payableByPageHelper;

        PageHelper.startPage(page, length);
        if ((expectedDateOfPayment == null || expectedDateOfPayment.length == 0)
                && (closingDate == null || closingDate.length == 0)) {
            payableByPageHelper = receivableAndPayableDao.getPayableByPageHelper();
        } else {
            payableByPageHelper = getDataByDateRange(
                    expectedDateOfPayment,
                    closingDate,
                    receivableAndPayableDao::getPayableByDateRange
            );
        }

        for (ReceivableAndPayableVO vo : payableByPageHelper) {
            Integer companyId = vo.getCompanyId();
            ArrayList<HashMap> payableList = receivableAndPayableDao.getPayableList(companyId, vo.getTargetYearAndMonth());
            List<HashMap> uniqueList = new ArrayList<>(payableList.stream()
                    .collect(Collectors.toMap(
                            map -> map.get("staffNameKanji"),
                            map -> map,
                            (existing, replacement) -> existing
                    ))
                    .values());

            BigDecimal lineTotal = BigDecimal.ZERO;
            for (HashMap map : uniqueList) {
                map.put("closingDate", vo.getClosingDate());
                BigDecimal amountRaised = (BigDecimal) map.get("amountRaised");
                lineTotal = lineTotal.add(amountRaised.compareTo(new BigDecimal("-9999")) == 0 ? BigDecimal.ZERO : amountRaised);
            }
            vo.setList(uniqueList);
            vo.setLineTotal(lineTotal);
        }

        return new PageInfo<>(payableByPageHelper);
    }




    @Override
    @Transactional
    public void updateReceivable(List<ReceivableAndPayableVO> dataList) {
        for (ReceivableAndPayableVO receivableAndPayableVO : dataList) {
            BigDecimal amountCharged = receivableAndPayableVO.getAmountCharged();
            BigDecimal depositAmount = receivableAndPayableVO.getDepositAmount();
            BigDecimal commission = amountCharged.subtract(depositAmount);
            receivableAndPayableDao.updateReceivable(receivableAndPayableVO.getCompanyId(), receivableAndPayableVO.getDepositAmount(), receivableAndPayableVO.getDepositDate(), commission);
        }
    }

    @Override
    @Transactional
    public void updatePayable(List<ReceivableAndPayableVO> dataList) {
        for (ReceivableAndPayableVO receivableAndPayableVO : dataList) {
            BigDecimal amountCharged = receivableAndPayableVO.getAmountCharged();
            BigDecimal depositAmount = receivableAndPayableVO.getDepositAmount();
            BigDecimal commission = amountCharged.subtract(depositAmount);
            receivableAndPayableDao.updateReceivable(receivableAndPayableVO.getCompanyId(), receivableAndPayableVO.getDepositAmount(), receivableAndPayableVO.getDepositDate(), commission);
        }
    }
    private List<ReceivableAndPayableVO> getDataByDateRange(
            String[] expectedDateOfPayment,
            String[] closingDate,
            QuadFunction<LocalDate, LocalDate, LocalDate, LocalDate, List<ReceivableAndPayableVO>> daoMethod) {

        LocalDate expectedStart = null, expectedEnd = null;
        LocalDate closingStart = null, closingEnd = null;

        if (expectedDateOfPayment != null && expectedDateOfPayment.length == 2) {
            expectedStart = LocalDate.parse(expectedDateOfPayment[0]);
            expectedEnd = LocalDate.parse(expectedDateOfPayment[1]);
        } else if (expectedDateOfPayment != null) {
            throw new IllegalArgumentException("expectedDateOfPayment 参数必须为长度为 2 的数组");
        }

        if (closingDate != null && closingDate.length == 2) {
            closingStart = LocalDate.parse(closingDate[0]);
            closingEnd = LocalDate.parse(closingDate[1]);
        } else if (closingDate != null) {
            throw new IllegalArgumentException("closingDate 参数必须为长度为 2 的数组");
        }

        return daoMethod.apply(expectedStart, expectedEnd, closingStart, closingEnd);
    }




}

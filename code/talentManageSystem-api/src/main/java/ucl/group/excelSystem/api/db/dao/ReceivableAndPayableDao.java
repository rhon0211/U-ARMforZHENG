package ucl.group.excelSystem.api.db.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import ucl.group.excelSystem.api.db.pojo.vo.ReceivableAndPayableVO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * todo
 * dao 层的方法比较多，
 * 可以通过动态 sql 拼接，减少方法的数量
 * 大致可以减少到 6 个左右
 */

@Mapper
@SuppressWarnings({"rawtypes"})
public interface ReceivableAndPayableDao {

    //没条件
    List<ReceivableAndPayableVO> getReceivableByPageHelper();

    List<ReceivableAndPayableVO> getPayableByPageHelper();

    //只有第一个范围的左边界
    List<ReceivableAndPayableVO> getReceivableByLeft1ByPageHelper(LocalDate left1);

    List<ReceivableAndPayableVO> getPayableByLeft1ByPageHelper(LocalDate left1);

    //只有第二个范围的左边界
    List<ReceivableAndPayableVO> getReceivableByLeft2ByPageHelper(LocalDate left2);

    List<ReceivableAndPayableVO> getPayableByLeft2ByPageHelper(LocalDate left2);

    //只有第一个范围或者第二个范围的左右边界
    List<ReceivableAndPayableVO> getReceivableByLeft1AndRight1ByPageHelper(LocalDate left1, LocalDate right1);

    List<ReceivableAndPayableVO> getReceivableByLeft2AndRight2ByPageHelper(LocalDate left2, LocalDate right2);

    List<ReceivableAndPayableVO> getPayableByLeft1AndRight1ByPageHelper(LocalDate left1, LocalDate right1);

    List<ReceivableAndPayableVO> getPayableByLeft2AndRight2ByPageHelper(LocalDate left2, LocalDate right2);

    //只有两个范围的左边界
    List<ReceivableAndPayableVO> getReceivableByLeft1AndLeft2ByPageHelper(LocalDate left1, LocalDate left2);

    List<ReceivableAndPayableVO> getPayableByLeft1AndLeft2ByPageHelper(LocalDate left1, LocalDate left2);

    //只有第一个范围的左右边界和第二个范围的左边界
    List<ReceivableAndPayableVO> getReceivableByLeft1AndLeft2AndRight1ByPageHelper(LocalDate left1, LocalDate right1, LocalDate left2);

    List<ReceivableAndPayableVO> getPayableByLeft1AndLeft2AndRight1ByPageHelper(LocalDate left1, LocalDate right1, LocalDate left2);

    //只有第两个范围的左右边界和第一个范围的左边界
    List<ReceivableAndPayableVO> getReceivableByLeft1AndLeft2AndRight2ByPageHelper(LocalDate left1, LocalDate left2, LocalDate right2);

    List<ReceivableAndPayableVO> getPayableByLeft1AndLeft2AndRight2ByPageHelper(LocalDate left1, LocalDate left2, LocalDate right2);

    // 两个范围的左右边界都有
    List<ReceivableAndPayableVO> getReceivableByLeft1AndLeft2AndRight1AndRight2ByPageHelper(LocalDate left1, LocalDate right1, LocalDate left2, LocalDate right2);



    ArrayList<HashMap> getReceivableList(Integer companyId,String targetYearAndMonth );

    ArrayList<HashMap> getPayableList(Integer companyId,String targetYearAndMonth );


    List<ReceivableAndPayableVO> getPayableByDateRange(
            @Param("expectedStart") LocalDate expectedStart,
            @Param("expectedEnd") LocalDate expectedEnd,
            @Param("closingStart") LocalDate closingStart,
            @Param("closingEnd") LocalDate closingEnd
    );

    List<ReceivableAndPayableVO> getReceivableByDateRange(
            @Param("expectedStart") LocalDate expectedStart,
            @Param("expectedEnd") LocalDate expectedEnd,
            @Param("closingStart") LocalDate closingStart,
            @Param("closingEnd") LocalDate closingEnd
    );

    void updateReceivable(Integer companyId, BigDecimal depositAmount, String depositDate, BigDecimal commission);
}

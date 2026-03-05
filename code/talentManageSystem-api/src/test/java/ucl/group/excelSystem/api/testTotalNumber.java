package ucl.group.excelSystem.api;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ucl.group.excelSystem.api.common.utils.TableUtils;
import ucl.group.excelSystem.api.db.dao.RelatedProjectTechnicianDao;
import ucl.group.excelSystem.api.db.pojo.RelatedProjectTechnician;
import ucl.group.excelSystem.api.db.pojo.vo.TechnicianListVO;
import ucl.group.talentManageSystem.api.Application;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
/**
 * @projectName: code
 * @package: ucl.group.excelSystem.api
 * @className: testTotalNumber
 * @author: he_jiale
 * @description: 何嘉乐测试工作
 * @date: 2024/7/22 11:03
 * @version: 1.0
 */
@SpringBootTest(classes = Application.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testTotalNumber {

    @Autowired
    private RelatedProjectTechnicianDao relatedProjectTechnicianDao;

    @Test
    public void getTotalNumber(){
        String tempYearMonth="2024-06";
        LocalDate dateStart=LocalDate.parse("2024-03-01");
        LocalDate dateEnd=LocalDate.parse("2024-08-01");
        List<RelatedProjectTechnician> relatedProjectTechnicians = relatedProjectTechnicianDao.searchBetweenStartAndEnd(String.valueOf(dateStart), String.valueOf(dateEnd));
        List<TechnicianListVO> technicianListVOS = TableUtils.changeStartAndEndMonth(relatedProjectTechnicians,dateStart,dateEnd);
        List<TechnicianListVO> techniciansInRanges = TableUtils.getTechniciansInRange(technicianListVOS, tempYearMonth);
        int temp=techniciansInRanges.size();
        System.out.println(temp);
    }
    @Test
    public void testBigDecimal() {
        BigDecimal a=BigDecimal.TEN.subtract(BigDecimal.valueOf(3));
        System.out.println(a);
    }
}

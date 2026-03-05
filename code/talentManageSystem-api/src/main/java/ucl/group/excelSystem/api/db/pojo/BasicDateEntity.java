package ucl.group.excelSystem.api.db.pojo;

import lombok.Data;

import java.time.LocalDate;

/**
 * 日期表实体类
 */
@Data
public class BasicDateEntity {

    private Long dateId;
    private Long monthId;
    private String name;
    private LocalDate monthDate;

}

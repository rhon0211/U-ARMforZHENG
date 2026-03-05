package ucl.group.excelSystem.api.db.pojo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TimeOrderEntity {
 private Integer projectId;
 private LocalDate projectEndDate;
 private LocalDate projectScheduledEndDate;
}
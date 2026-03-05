package ucl.group.excelSystem.api.db.pojo.vo;
import lombok.Data;

import java.util.List;



@Data
public class ProjectDetailVO {
        private Integer companyId;
        private Integer salesCompanyId;
        private Integer departmentId;
        private Integer projectId;
        private String projectName;
        private String projectNameAbbreviation;
        private String projectStartDate;
        private String projectScheduledEndDate;
        private String projectEndDate;
        private String siteAddress;
        private String startTime;
        private String endTime;
        private String comment;
        private String salesCompanyName; // 销售公司名
        private String departmentName;   // 部门名
        private Double dailyOperatingHours;
        private Double settlementUpperLimit;
        private Double settlementLowerLimit;
        private List<StaffDetailVO> StaffDetail; // 案件明细
    }


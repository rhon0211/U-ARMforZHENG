package ucl.group.talentManageSystem.api.db.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class BasicProjectInfoEntity extends BasicEntity {
    private Integer projectId;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Tokyo")
    private Date appointStartTime;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Tokyo")
    private Date appointEndTime;
    /**
     * 任用评价;从5个方面评价：技术评价、沟通能力、态度评价、勤务评价、leader评价
     */
    private String appointEvalution;


    public BasicProjectInfoEntity(Integer projectId, String name, Date appointStartTime,
            Date appointEndTime, String appointEvalution) {
        this.projectId = projectId;
        this.name = name;
        this.appointStartTime = appointStartTime;
        this.appointEndTime = appointEndTime;
        this.appointEvalution = appointEvalution;
    }

    public BasicProjectInfoEntity(String remark, Integer projectId, String name,
            Date appointStartTime, Date appointEndTime, String appointEvalution) {
        super("0", remark);
        this.projectId = projectId;
        this.name = name;
        this.appointStartTime = appointStartTime;
        this.appointEndTime = appointEndTime;
        this.appointEvalution = appointEvalution;
    }


}

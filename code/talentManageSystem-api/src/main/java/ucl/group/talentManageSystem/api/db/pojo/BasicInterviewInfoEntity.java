package ucl.group.talentManageSystem.api.db.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class BasicInterviewInfoEntity extends BasicEntity {

    private Integer interId;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Tokyo")
    private Date busDate;
    /**
     * 这里是主键id
     */
    private Integer busInterviewer;
    private String busEvaluation;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Tokyo")
    private Date techDate;
    /**
     * 这里是主键id
     */
    private Integer techInterviewer;
    private String techEvaluation;

    /**
     * 传给前端字段 业务面试官信息
     */

    private String busName;
    private String busNamePse;
    /**
     * 传给前端字段 技术面试官信息
     */
    private String techName;
    private String techNamePse;

    private String picture;

    public BasicInterviewInfoEntity() {
        super();
    }

    public BasicInterviewInfoEntity(String status, String remark, Integer interId, Date busDate,
            Integer busInterviewer, String busEvaluation, Date techDate, Integer techInterviewer,
            String techEvaluation, String picture) {
        super(status, remark);
        this.interId = interId;
        this.busDate = busDate;
        this.busInterviewer = busInterviewer;
        this.busEvaluation = busEvaluation;
        this.techDate = techDate;
        this.techInterviewer = techInterviewer;
        this.techEvaluation = techEvaluation;
        this.picture = picture;
    }

    public BasicInterviewInfoEntity(Integer interId, Date busDate, Integer busInterviewer,
            String busEvaluation, Date techDate, Integer techInterviewer, String techEvaluation,
            String picture) {
        this.interId = interId;
        this.busDate = busDate;
        this.busInterviewer = busInterviewer;
        this.busEvaluation = busEvaluation;
        this.techDate = techDate;
        this.techInterviewer = techInterviewer;
        this.techEvaluation = techEvaluation;
        this.picture = picture;
    }
}

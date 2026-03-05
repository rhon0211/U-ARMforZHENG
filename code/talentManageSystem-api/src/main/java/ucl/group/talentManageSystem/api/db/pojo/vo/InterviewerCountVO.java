package ucl.group.talentManageSystem.api.db.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class InterviewerCountVO {
    private int interviewerId;
    private String name;
    private String pseudonym;
    private String type;
    private String status;
    private String delFlag;
    private String createBy;
    private Date createTime;
    //面试人数
    private int interviewedNum;
    //采用人数
    private int hiredNum;

    public InterviewerCountVO(int interviewerId, String name, String pseudonym, String type, String status, String delFlag, String createBy, Date createTime, int interviewedNum, int hiredNum) {
        this.interviewerId = interviewerId;
        this.name = name;
        this.pseudonym = pseudonym;
        this.type = type;
        this.status = status;
        this.delFlag = delFlag;
        this.createBy = createBy;
        this.createTime = createTime;
        this.interviewedNum = interviewedNum;
        this.hiredNum = hiredNum;
    }
}

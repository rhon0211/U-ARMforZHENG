package ucl.group.talentManageSystem.api.db.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode
public class BasicEntity {
    /**
     * 标志
     */
    private String status;
    /**
     * 删除标志位
     */
    private String delFlag;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 创建人名称
     */
    private String createBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 更新人名称
     */
    private String updateBy;

    public BasicEntity() {}

    public BasicEntity(String status, String remark, Date createTime, String createBy) {
        this.status = status;
        this.remark = remark;
        this.createTime = createTime;
        this.createBy = createBy;
    }


    // 带参数的构造函数
    public BasicEntity(String status, String remark) {
        this.status = status;
        this.remark = remark;
    }
}

package ucl.group.talentManageSystem.api.db.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ucl.group.talentManageSystem.api.db.pojo.vo.BusinessYear;
import ucl.group.talentManageSystem.api.db.pojo.vo.LabelYear;

import java.util.Date;
import java.util.List;

@Data
public class BasicTalentEntity  extends  BasicEntity{
    private Integer talentId;
    private String name;
    private String pseudonym;
    private String englishName;
    private String sex;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String birth;
    private int exprYear;
    private String nation;
    private int inJapanYear;
    private String japanLevel;
    private String station;
    private String school;
    private String major;
    private String picture;
    private String skillSheet;
    private String talentDescription;
    private String belongCompany;
    private String appointStatus;
    private String blacklistReason;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String blacklistTime;
    private String  blacklistBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String deletedTime;
    private String deletedBy;
    /**
     * 一级保密信息
     */
    private String email;
    private String phone;
    private String wechat;
    private String line;
    /**
     * 二级保密信息
     */
    private int price;

    /**
     * 面试信息
     */
    private List<BasicInterviewInfoEntity> interviewInfoList;  //
    /**
     * 项目信息
     */
    private List<BasicProjectInfoEntity> projectInfoList;
    /**
     * 工程信息
     */
    private List<BasicEngineerEntity> engineerNameList;
    /**
     * 标签年份信息
     */
    private List<LabelYear> talentLabelList;
    /**
     * 行业年份信息
     */
    private List<BusinessYear> talentBusinessList;

    /**
     * 新增：找工作月份、公司名
     */
    private String predictMonth;
    private String companyName;


    /**
     * vo字段
     */
    private Date latestDate;



    public BasicTalentEntity(){
      super();
    }

    public BasicTalentEntity(String status, String remark, Date createTime, String createBy, Integer  talentId, String name, String pseudonym, String englishName, String sex, String birth, int exprYear, String nation, int inJapanYear, String japanLevel, String station, String school, String major, String picture, String skillSheet, String talentDescription, String belongCompany, String appointStatus, String blacklistReason, String blacklistTime, String blacklistBy, String email, String phone, String wechat, String line, int price,String companyName, String predictMonth) {
        super(status,  remark, createTime, createBy);
        this.talentId=talentId;
        this.name = name;
        this.pseudonym = pseudonym;
        this.englishName = englishName;
        this.sex = sex;
        this.birth = birth;
        this.exprYear = exprYear;
        this.nation = nation;
        this.inJapanYear = inJapanYear;
        this.japanLevel = japanLevel;
        this.station = station;
        this.school = school;
        this.major = major;
        this.picture = picture;
        this.skillSheet = skillSheet;
        this.talentDescription = talentDescription;
        this.belongCompany = belongCompany;
        this.appointStatus = appointStatus;
        this.blacklistReason = blacklistReason;
        this.blacklistTime = blacklistTime;
        this.blacklistBy = blacklistBy;
        this.email = email;
        this.phone = phone;
        this.wechat = wechat;
        this.line = line;
        this.price = price;
        this.predictMonth = predictMonth;
        this.companyName = companyName;
    }



}

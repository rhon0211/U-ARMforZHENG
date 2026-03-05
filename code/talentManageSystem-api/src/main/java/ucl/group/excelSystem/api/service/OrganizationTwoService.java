package ucl.group.excelSystem.api.service;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotEmpty;

import org.apache.ibatis.annotations.Param;

import ucl.group.excelSystem.api.db.pojo.BasicOrganizationEntity;
import ucl.group.excelSystem.api.db.pojo.bo.InsertOrganizationBO;
import ucl.group.excelSystem.api.db.pojo.bo.UpdateOrganizationBO;
import ucl.group.talentManageSystem.api.common.PageUtils;

public interface OrganizationTwoService {

    PageUtils selectOrganizationTwoByPage(@Param("param") Map<String, Object> param);

    void insertOrganizationTwo(
            @Param("insertOrganizationBO") InsertOrganizationBO insertOrganizationBO);

    void updateOrganizationTwo(@Param("bean") UpdateOrganizationBO bean);

    void deleteOrganizationTwo(@NotEmpty(
            message = "organizationIdを入力してください") @Param("organizationId") Long organizationId);

    List<BasicOrganizationEntity> selectOrganizationTwoById(
            @Param("organizationId") Long organizationId);

    List<BasicOrganizationEntity> selectAll();

    List<BasicOrganizationEntity> selectOrganizationTwoByIds(@Param("ids") List<Long> ids);

    PageUtils getUpper(@Param("param") Map<String, Object> param);

    BasicOrganizationEntity getById(Long id);

    List<Long> getAllId();
}

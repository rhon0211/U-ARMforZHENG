package ucl.group.excelSystem.api.db.dao;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import ucl.group.excelSystem.api.db.pojo.vo.LaborManageVO;

import java.util.List;
import java.util.Map;

// LaborManageDao.java
@Mapper
public interface LaborManageDao {
    @MapKey("staffId")
    List<Map<String, Object>> getUCLStaffWithNamesByPage();

    List<LaborManageVO> getLaborManageByTimeAndStaffIds(@Param("time") String time,
            @Param("staffIds") List<Integer> staffIds);

    void batchInsertLaborManage(@Param("list") List<LaborManageVO> laborManageList);

    Long getTotalLaborManageCount(@Param("time") String time);
    // 查询 UCL 员工的 ID 和姓名

    // 根据员工ID列表和时间查询已有的劳务数据
    List<LaborManageVO> getLaborManageByStaffIdsAndTime(@Param("staffIds") List<Integer> staffIds,
            @Param("time") String time);

    // 插入新的劳务管理数据
    // 分页查询（新增 yearMonth 参数）
    List<LaborManageVO> selectLaborManageByPage(@Param("time") String time,
            @Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    // 总数查询（新增 time 参数）
    Long countLaborManage(@Param("time") String time);

    // 以下方法保持不变
    LaborManageVO getLaborById(Integer id);

    void saveStatusBatch(@Param("ids") List<Integer> ids,
            @Param("laborStatus") Integer laborStatus);

    void batchUpdate(@Param("list") List<LaborManageVO> laborManageVOList);

    Boolean isExist(Integer id);

    LaborManageVO getLaborByIdAndTime(Integer id, String time);

    void insertLaborManage(LaborManageVO laborManageVO);

    void updateLaborManage(LaborManageVO laborManageVO);

    List<Integer> getLaborStatusByStaffIds(List<Integer> staffIds, String time);

    List<LaborManageVO> getLaborManageByTimeRangeAndStaffId(@Param("staffId") Integer staffId,
            @Param("months") List<String> months);

    void updateLaborBatch(@Param("list") List<LaborManageVO> list);

    boolean existsLaborRecord(@Param("staffId") Integer staffId, @Param("time") String time);

}

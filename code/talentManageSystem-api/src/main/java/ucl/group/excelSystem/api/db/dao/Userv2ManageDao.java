package ucl.group.excelSystem.api.db.dao;

import ucl.group.excelSystem.api.db.pojo.Userv2Entity;
import ucl.group.excelSystem.api.db.pojo.vo.Userv2VO;

import java.util.List;
import java.util.Map;

public interface Userv2ManageDao {

    // 查询表：分页查询用户列表
    List<Userv2VO> selectUserByPage(Map<String, Object> param);

    // 查询表：获取分页总记录数
    long selectUserByPageCount(Map<String, Object> param);

    // 新增用户
    void insertUser(Userv2Entity userv2Entity);

    // 更新用户
    void updateUser(Userv2Entity userv2Entity);

    // 删除用户（批量删除）
    void deleteUser(Long userId);

    // 修改密码
    void updatePassword(Long userId, String newPassword);

    // 按 ID 查询用户详情
    Userv2Entity searchById(Long userId);

    // 用户校验密码是否正确
    boolean checkPassword(Long userId, String password);
}

package ucl.group.excelSystem.api.service;

import ucl.group.excelSystem.api.db.pojo.Userv2Entity;
import ucl.group.excelSystem.api.db.pojo.vo.Userv2VO;

import java.util.List;
import java.util.Map;
public interface Userv2Service {
    // 查询用户列表，分页
    List<Userv2VO> queryUsersByPage(Map<String, Object> param);

    // 查询用户总数
    long queryUsersByPageCount(Map<String, Object> param);

    // 新增用户
    void insertUser(Userv2Entity userv2Entity);

    // 更新用户
    void updateUser(Userv2Entity userv2Entity);

    // 删除用户
    void deleteUser(Long userId);

    // 修改密码
    void updatePassword(Long userId, String newPassword);

    // 按ID查询用户详情
    Userv2VO getUserById(Long userId);

    // 校验用户密码是否正确
    boolean verifyPassword(Long userId, String password);
}

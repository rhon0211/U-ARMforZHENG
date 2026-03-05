package ucl.group.talentManageSystem.api.config;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.stereotype.Component;
import ucl.group.talentManageSystem.api.db.dao.UserDao;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

// 复用需要修改
@Component
public class StpInterfaceImpl implements StpInterface {
    @Resource
    private UserDao userDao;

    /**
     * 返回一个用户所拥有的权限集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginKey) {
        int userId = Integer.parseInt(loginId.toString());
        ArrayList<String> list = userDao.searchUserPermissions(userId);
        return list;
    }


    /**
     * 返回一个用户所拥有的角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginKey) {
        return null;
    }

}

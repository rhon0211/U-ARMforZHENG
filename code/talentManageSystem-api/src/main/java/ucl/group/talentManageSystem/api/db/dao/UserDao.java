package ucl.group.talentManageSystem.api.db.dao;

import org.apache.ibatis.annotations.Param;
import ucl.group.talentManageSystem.api.db.pojo.UserEntity;

import java.util.ArrayList;
import java.util.Map;

@SuppressWarnings({"rawtypes"})
public interface UserDao {
    public ArrayList<String> searchUserPermissions(@Param("userId") int userId);

    public int judgeExist(Map params);

    public UserEntity login(Map params);

}

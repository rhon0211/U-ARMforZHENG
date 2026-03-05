package ucl.group.talentManageSystem.api.db.dao;

import ucl.group.talentManageSystem.api.db.pojo.UserEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"rawtypes"})
public interface UserManageDao {

    public void supadmInsertUser(UserEntity entity);

    public void otherInsertUser(UserEntity entity);

    public ArrayList<HashMap> searchUserEmail();

    public long userSearchCount(Map param);

    public ArrayList<HashMap> supadmUserSearchByPage(Map param);

    public ArrayList<HashMap> exadmUserSearchByPage(Map param);

    public HashMap searchById(int userId);

    public void supadmUpdateUser(UserEntity entity);

    public void otherUpdateUser(UserEntity entity);

    public void userDeletes(Integer[] ids);

    public void userDisable(Integer[] ids);

    public void userEnable(Integer[] ids);

    public ArrayList<HashMap> userSelect();

    public ArrayList<HashMap> userTypeSelect();

    public ArrayList<HashMap> exuserTypeSelect();
}

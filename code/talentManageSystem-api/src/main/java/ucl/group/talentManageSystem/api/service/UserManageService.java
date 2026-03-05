package ucl.group.talentManageSystem.api.service;

import ucl.group.talentManageSystem.api.common.PageUtils;
import ucl.group.talentManageSystem.api.db.pojo.UserEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface UserManageService {

    public void insertUser(UserEntity userEntity);

    public PageUtils userSearchByPage(Map param);

    public HashMap searchById(int userId);

    public void updateUser(UserEntity entity);

    public void userDeletes(Integer[] ids);

    public void userDisable(Integer[] ids);

    public void userEnable(Integer[] ids);

    public ArrayList<HashMap> userSelect();

    public ArrayList<HashMap> userTypeSelect();

    public ArrayList<HashMap> exuserTypeSelect();

    public PageUtils exuserSearchByPage(Map param);
}

package ucl.group.talentManageSystem.api.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ucl.group.talentManageSystem.api.common.PageUtils;
import ucl.group.talentManageSystem.api.common.utils.SecurityUtils;
import ucl.group.talentManageSystem.api.db.dao.UserManageDao;
import ucl.group.talentManageSystem.api.db.pojo.EmailRegisterEntity;
import ucl.group.talentManageSystem.api.db.pojo.UserEntity;
import ucl.group.talentManageSystem.api.service.EmailService;
import ucl.group.talentManageSystem.api.service.RedisService;
import ucl.group.talentManageSystem.api.service.UserManageService;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
@SuppressWarnings({"rawtypes", "unchecked"})
public class UserManageServiceImpl implements UserManageService {

    @Resource
    private UserManageDao userManageDao;

    @Resource
    private EmailService emailService;

    @Resource
    private RedisService redisService;

    @Override
    public void insertUser(UserEntity userEntity) {
        String currentName = SecurityUtils.getUsername();
        ArrayList<HashMap> emailList = userManageDao.searchUserEmail();
        EmailRegisterEntity emailRegisterEntity = new EmailRegisterEntity();
        emailRegisterEntity.setCurrentname(currentName);
        emailRegisterEntity.setUsername(userEntity.getName());
        emailRegisterEntity.setUsertype(userEntity.getType());
        userEntity.setCreateBy(currentName);
        // 给系统管理员和超级管理员发送邮件
        List<String> permissionList = StpUtil.getPermissionList();
        if (permissionList.contains("supadm_create")) {
            userManageDao.supadmInsertUser(userEntity);
            Iterator<HashMap> it = emailList.iterator();
            while (it.hasNext()) {
                HashMap one = it.next();
                if (one.get("type").equals("0")) {
                    emailRegisterEntity.setSearchname((String) one.get("name"));
                    emailService.sendRegisterEmail((String) one.get("email"), emailRegisterEntity);
                }
            }
        } else {
            userEntity.setAccount(null);
            userEntity.setPassword(null);
            userManageDao.otherInsertUser(userEntity);
            if (permissionList.contains("ordadm_create")
                    && !permissionList.contains("supadm_create")) {
                Iterator<HashMap> it = emailList.iterator();
                while (it.hasNext()) {
                    HashMap one = it.next();
                    if (one.get("type").equals("0") || one.get("type").equals("1")) {
                        emailRegisterEntity.setSearchname((String) one.get("name"));
                        emailService.sendRegisterEmail((String) one.get("email"),
                                emailRegisterEntity);
                    }
                }
            } else if (permissionList.contains("user_create")
                    && !permissionList.contains("supadm_create")
                    && !permissionList.contains("ordadm_create")) {
                Iterator<HashMap> it = emailList.iterator();
                while (it.hasNext()) {
                    HashMap one = it.next();
                    if (one.get("type").equals("0") || one.get("type").equals("1")
                            || one.get("type").equals("2")) {
                        emailRegisterEntity.setSearchname((String) one.get("name"));
                        emailService.sendRegisterEmail((String) one.get("email"),
                                emailRegisterEntity);
                    }
                }
            }

        }
        // 给注册的用户发送邮件
        emailService.sendRegisterUserEmail(userEntity.getEmail(), emailRegisterEntity);
    }

    @Override
    public PageUtils userSearchByPage(Map param) {
        ArrayList<HashMap> list = null;
        long count = userManageDao.userSearchCount(param);
        if (count > 0) {
            List<String> permissionList = StpUtil.getPermissionList();
            list = userManageDao.supadmUserSearchByPage(param);
            if (permissionList.contains("supadm_search")) {
                Iterator<HashMap> it = list.iterator();
                while (it.hasNext()) {
                    HashMap one = it.next();
                    if (one.get("type").equals("0"))
                        it.remove();
                }
            } else if (permissionList.contains("ordadm_search")) {
                Iterator<HashMap> it = list.iterator();
                while (it.hasNext()) {
                    HashMap one = it.next();
                    if (one.get("type").equals("0") || one.get("type").equals("1"))
                        it.remove();
                    one.put("account", "");
                    one.put("password", "");
                }
            } else if (permissionList.contains("user_search")) {
                Iterator<HashMap> it = list.iterator();
                while (it.hasNext()) {
                    HashMap one = it.next();
                    if (one.get("type").equals("0") || one.get("type").equals("1")
                            || one.get("type").equals("2"))
                        it.remove();
                    one.put("account", "");
                    one.put("password", "");
                }
            } else {
                return null;
            }
        } else {
            list = new ArrayList<>();
        }
        if (!isZh()) {
            for (HashMap<String, Object> map : list) {
                Object typeName = map.get("typeName");
                if (typeName != null) {
                    Object typeNameJap = map.get("typeNameJap");
                    map.put("typeName", typeNameJap);
                }
            }
        }
        int page = MapUtil.getInt(param, "page");
        int length = MapUtil.getInt(param, "length");
        PageUtils pageUtils = new PageUtils(list, count, page, length);
        return pageUtils;
    }

    @Override
    public HashMap searchById(int userId) {
        HashMap map = userManageDao.searchById(userId);
        List<String> permissionList = StpUtil.getPermissionList();
        if (!permissionList.contains("supadm_search")) {
            map.put("account", "");
            map.put("password", "");
        }
        return map;
    }

    @Override
    public void updateUser(UserEntity entity) {
        List<String> permissionList = StpUtil.getPermissionList();
        if (permissionList.contains("supadm_update")) {
            userManageDao.supadmUpdateUser(entity);
        } else {
            // entity.setAccount(null);
            // entity.setPassword(null);
            userManageDao.otherUpdateUser(entity);
        }
    }

    @Override
    public void userDeletes(Integer[] ids) {
        userManageDao.userDeletes(ids);
    }

    @Override
    public void userDisable(Integer[] ids) {
        userManageDao.userDisable(ids);
    }

    @Override
    public void userEnable(Integer[] ids) {
        userManageDao.userEnable(ids);
    }

    /**
     * 超级管理员不能查自己 普管也不能查自己，只能查用户的记录
     * 
     * @return
     */
    @Override
    public ArrayList<HashMap> userSelect() {
        ArrayList<HashMap> list = userManageDao.userSelect();
        List<String> permissionList = StpUtil.getPermissionList();
        // 系统管理员有supadm
        // 系统管理员能看到管理表格系统的日志
        if (permissionList.contains("supadm_search")) {
        } else if (permissionList.contains("ordadm_search")) {
            Iterator<HashMap> it = list.iterator();
            while (it.hasNext()) {
                HashMap one = it.next();
                if (one.get("type").equals("0") || one.get("type").equals("4")
                        || one.get("type").equals("5"))
                    it.remove();
            }
        } else if (permissionList.contains("user_search")) {
            Iterator<HashMap> it = list.iterator();
            while (it.hasNext()) {
                HashMap one = it.next();
                if (one.get("type").equals("0") || one.get("type").equals("1")
                        || one.get("type").equals("4") || one.get("type").equals("5"))
                    it.remove();
            }
        } else {
            return null;
        }
        return list;
    }

    @Override
    public ArrayList<HashMap> userTypeSelect() {
        ArrayList<HashMap> list = userManageDao.userTypeSelect();
        if (!isZh()) {
            for (HashMap<String, Object> map : list) {
                Object name = map.get("name");
                if (name != null) {
                    Object nameJap = map.get("nameJap");
                    map.put("name", nameJap);
                }
            }
        }
        List<String> permissionList = StpUtil.getPermissionList();
        if (permissionList.contains("supadm_search")) {
            Iterator<HashMap> it = list.iterator();
            while (it.hasNext()) {
                HashMap one = it.next();
                if (one.get("id").equals("0")) {
                    it.remove();
                }
            }
        } else if (permissionList.contains("ordadm_search")) {
            Iterator<HashMap> it = list.iterator();
            while (it.hasNext()) {
                HashMap one = it.next();
                if (one.get("id").equals("0") || one.get("id").equals("1")
                        || one.get("type").equals("4") || one.get("id").equals("5"))
                    it.remove();
            }
        } else if (permissionList.contains("user_search")) {
            Iterator<HashMap> it = list.iterator();
            while (it.hasNext()) {
                HashMap one = it.next();
                if (one.get("id").equals("0") || one.get("id").equals("1")
                        || one.get("id").equals("2") || one.get("id").equals("4")
                        || one.get("id").equals("5"))
                    it.remove();
            }
        } else {
            return new ArrayList<>();
        }
        return list;
    }

    private Boolean isZh() {
        String userId = SecurityUtils.getUserIdAsString();
        String langName = redisService.getCacheObject(redisService.getLangCacheKey(userId));
        if (langName.equals("zh-CN"))
            return true;
        else
            return false;
    }

    @Override
    public PageUtils exuserSearchByPage(Map param) {
        ArrayList<HashMap> list = null;
        long count = userManageDao.userSearchCount(param);
        if (count > 0) {
            List<String> permissionList = StpUtil.getPermissionList();
            list = userManageDao.exadmUserSearchByPage(param);
            if (permissionList.contains("supadm_search")) {
                Iterator<HashMap> it = list.iterator();
                while (it.hasNext()) {
                    HashMap one = it.next();
                    if (one.get("type").equals("0"))
                        it.remove();
                }
            } else if (permissionList.contains("ordadm_search")) {
                Iterator<HashMap> it = list.iterator();
                while (it.hasNext()) {
                    HashMap one = it.next();
                    if (one.get("type").equals("0") || one.get("type").equals("1"))
                        it.remove();
                    one.put("account", "");
                    one.put("password", "");
                }
            } else if (permissionList.contains("user_search")) {
                Iterator<HashMap> it = list.iterator();
                while (it.hasNext()) {
                    HashMap one = it.next();
                    if (one.get("type").equals("0") || one.get("type").equals("1")
                            || one.get("type").equals("2"))
                        it.remove();
                    one.put("account", "");
                    one.put("password", "");
                }
            } else {
                return null;
            }
        } else {
            list = new ArrayList<>();
        }
        if (!isZh()) {
            for (HashMap<String, Object> map : list) {
                Object typeName = map.get("typeName");
                if (typeName != null) {
                    Object typeNameJap = map.get("typeNameJap");
                    map.put("typeName", typeNameJap);
                }
            }
        }
        int page = MapUtil.getInt(param, "page");
        int length = MapUtil.getInt(param, "length");
        PageUtils pageUtils = new PageUtils(list, count, page, length);
        return pageUtils;
    }


    @Override
    public ArrayList<HashMap> exuserTypeSelect() {
        ArrayList<HashMap> list = userManageDao.exuserTypeSelect();
        List<String> permissionList = StpUtil.getPermissionList();
        if (permissionList.contains("supadm_search")) {
            Iterator<HashMap> it = list.iterator();
            while (it.hasNext()) {
                HashMap one = it.next();
                if (one.get("id").equals("0")) {
                    it.remove();
                }
            }
        } else if (permissionList.contains("ordadm_search")) {
            Iterator<HashMap> it = list.iterator();
            while (it.hasNext()) {
                HashMap one = it.next();
                if (one.get("id").equals("0") || one.get("id").equals("1"))
                    it.remove();
            }
        } else if (permissionList.contains("user_search")) {
            Iterator<HashMap> it = list.iterator();
            while (it.hasNext()) {
                HashMap one = it.next();
                if (one.get("id").equals("0") || one.get("id").equals("1")
                        || one.get("id").equals("2"))
                    it.remove();
            }
        } else {
            return null;
        }
        return list;
    }
}

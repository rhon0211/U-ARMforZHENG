package ucl.group.excelSystem.api.service.impl;

import org.springframework.stereotype.Service;
import ucl.group.excelSystem.api.db.dao.Userv2ManageDao;
import ucl.group.excelSystem.api.db.pojo.Userv2Entity;
import ucl.group.excelSystem.api.db.pojo.vo.Userv2VO;
import ucl.group.excelSystem.api.exception.UserNotFoundException;
import ucl.group.excelSystem.api.service.Userv2Service;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class Userv2ServiceImpl implements Userv2Service {

    private final Userv2ManageDao userv2ManageDao;

    public Userv2ServiceImpl(Userv2ManageDao userv2ManageDao) {
        this.userv2ManageDao = userv2ManageDao;
    }

    @Override
    public List<Userv2VO> queryUsersByPage(Map<String, Object> param) {
    return userv2ManageDao.selectUserByPage(param);
    }

    @Override
    public long queryUsersByPageCount(Map<String, Object> param) {
        return userv2ManageDao.selectUserByPageCount(param);
    }

    @Override
    public void insertUser(Userv2Entity userv2Entity) {
        userv2Entity.setPassword(
                Base64.getEncoder().encodeToString(userv2Entity.getPassword().getBytes()));
        userv2ManageDao.insertUser(userv2Entity);
    }

    @Override
    public void updateUser(Userv2Entity userv2Entity) {
        userv2ManageDao.updateUser(userv2Entity);
    }

    @Override
    public void deleteUser(Long userId) {
        userv2ManageDao.deleteUser(userId);
    }

    @Override
    public void updatePassword(Long userId, String newPassword) {
        String password = Base64.getEncoder().encodeToString(newPassword.getBytes());
        userv2ManageDao.updatePassword(userId, password);
    }

    @Override
    public Userv2VO getUserById(Long userId) {
        Userv2Entity userv2Entity = userv2ManageDao.searchById(userId);
        if (userv2Entity == null) {
            throw new UserNotFoundException("ユーザーが見つかりません。ID: " + userId);
        }
        return convertToUserv2VO(userv2Entity);
    }

    private Userv2VO convertToUserv2VO(Userv2Entity entity) {
        if (entity == null) {
            return null;
        }
        return new Userv2VO(entity.getUserId(), entity.getName(), entity.getKatakana(),
                entity.getEmail(), entity.getType(), entity.getCode(), entity.getPhone(),
                entity.getActive(), entity.getRemark(), entity.getAccount());
    }


    @Override
    public boolean verifyPassword(Long userId, String password) {
        String psword = Base64.getEncoder().encodeToString(password.getBytes());
        return userv2ManageDao.checkPassword(userId, psword);
    }
}

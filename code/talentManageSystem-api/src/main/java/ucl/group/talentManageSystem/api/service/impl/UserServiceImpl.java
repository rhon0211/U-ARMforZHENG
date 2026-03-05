package ucl.group.talentManageSystem.api.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.map.MapUtil;
import org.springframework.stereotype.Service;
import ucl.group.talentManageSystem.api.common.constants.CacheConstants;
import ucl.group.talentManageSystem.api.db.dao.UserDao;
import ucl.group.talentManageSystem.api.db.pojo.UserEntity;
import ucl.group.talentManageSystem.api.exception.ServiceException;
import ucl.group.talentManageSystem.api.service.RedisService;
import ucl.group.talentManageSystem.api.service.UserService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;
    @Resource
    private RedisService redisService;
    @Override
    public UserEntity login(Map param) {
        String email = MapUtil.getStr(param, "email");
        //员工编号
        String code = MapUtil.getStr(param, "code");
        String password = MapUtil.getStr(param, "password");
        // todo 解密
        int systemCode = MapUtil.getInt(param, "systemCode");
        // 使用RedisService的getCacheObject方法来获取验证码
        String correctCode = redisService.getCacheObject(getCacheKey(email));
        if (correctCode == null || !correctCode.equals(code)) {
            // 如果验证码不匹配或不存在，则返回null或自定义错误代码
            return null;
        }
        // 验证通过后，查找用户信息
        Map<String, Object> params = new HashMap<>();
        params.put("email",email);
        params.put("systemCode",systemCode);
        params.put("password",password);
        UserEntity userEntity = userDao.login(params);
        if (userEntity == null) {
            // 用户不存在
            throw new ServiceException("ユーザーが存在しません");
        }
        StpUtil.login(userEntity.getUserId());
        return userEntity;
    }
    @Override
    public Boolean judgeExist(String email, int systemCode){
        Map<String, Object> params = new HashMap<>();
        params.put("email",email);
        params.put("systemCode", systemCode);
        if(userDao.judgeExist(params)>0){
            //存在
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
    private String getCacheKey(String configKey)
    {
        return CacheConstants.EMAIL_CODE_KEY + configKey;
    }
    public ArrayList<String> searchUserPermissions(int userId) {
        return userDao.searchUserPermissions(userId);
    }
}

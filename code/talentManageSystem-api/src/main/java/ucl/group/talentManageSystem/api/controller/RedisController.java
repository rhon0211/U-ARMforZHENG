package ucl.group.talentManageSystem.api.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ucl.group.talentManageSystem.api.common.R;
import ucl.group.talentManageSystem.api.common.annotation.Log;
import ucl.group.talentManageSystem.api.common.enums.BusinessType;
import ucl.group.talentManageSystem.api.common.utils.SecurityUtils;
import ucl.group.talentManageSystem.api.exception.ServiceException;
import ucl.group.talentManageSystem.api.service.RedisService;

@RestController
@RequestMapping("/api/v1/redis")
public class RedisController {
    @Autowired
    private RedisService redisService;
    /**
     * 修改语言，根据用户id
     * 后端需要处理返回值请查看CacheConstants的Language_CH  Language_JA作为对比
     */
    @PutMapping("/{lang}")
    @SaCheckLogin
    @Log(title = "人才管理", businessType = BusinessType.UPDATE)
    public R updateLang(@PathVariable("lang") String lang){
        try {
            String userId = SecurityUtils.getUserIdAsString();
            redisService.setCacheObject(redisService.getLangCacheKey(userId), lang);
            return R.ok();
        }catch (Exception e) {
            // 如果更新语言失败，则抛出业务异常
            //更新语言失败
            throw new ServiceException("言語の更新に失敗しました "+e.getMessage());
        }

    }
}

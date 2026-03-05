package ucl.group.talentManageSystem.api.common.constants;

/*** 缓存常量信息
 * @author hejiale
 */
public class CacheConstants
{
    /**
     * 缓存有效期，默认720（分钟）
     */
    public final static long EXPIRATION = 720;

    /** 缓存刷新时间，默认120（分钟）*/
    public final static long REFRESH_TIME = 120;

    /** 登录最大错误次数*/
    public final static int LOGIN_MAX_RETRY_COUNT = 5;

    /** 权限缓存前缀*/
    public final static String LOGIN_TOKEN_KEY = "login_tokens:";

    /** 验证码 redis key*/
    public static final String EMAIL_CODE_KEY = "email_code:";

    /** 用户界面中的中日文  key*/
    public static final String Language_user_KEY = "language_user:";
    /** 字典数据  key*/
    public static final String DICT_DATA_KEY = "talent_dict_data:";
    /** 用户界面中文标志*/
    public static final String Language_CH = "zh-CN";
    /** 用户界面日文标志*/
    public static final String Language_JA ="ja-JP";
}

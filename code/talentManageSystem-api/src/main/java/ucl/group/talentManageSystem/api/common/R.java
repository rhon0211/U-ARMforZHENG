package ucl.group.talentManageSystem.api.common;

import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;


public class R extends HashMap<String, Object> {

    // 默认无参构造，执行成功，http请求状态放ok，消息是成果
    public R() {
        put("code", HttpStatus.SC_OK);
        put("msg", "success");
    }

    // R.ok()，返回成功码值和成功信息
    public static R ok() {
        return new R();
    }

    // R.ok()，成功码值，但能自定义消息
    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    // R.ok()，自定义码值和消息
    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    // 出错，自定义码值和消息
    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R error(String msg) {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }

    public static R error() {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
    }



    // 放置结果码
    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        // 那这个不是只有code或者msg了？
        return this;
    }

}

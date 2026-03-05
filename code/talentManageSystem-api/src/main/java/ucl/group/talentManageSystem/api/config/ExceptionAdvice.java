package ucl.group.talentManageSystem.api.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import cn.dev33.satoken.exception.NotLoginException;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import ucl.group.talentManageSystem.api.exception.ServiceException;

@Slf4j
@RestControllerAdvice // 处理全部异常
public class ExceptionAdvice {

    @SuppressWarnings("deprecation")
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e) {
        JSONObject json = new JSONObject();
        if (e instanceof HttpMessageNotReadableException) {
            HttpMessageNotReadableException exception = (HttpMessageNotReadableException) e;
            log.error("error", exception);
            json.set("msg", "リクエストが無効です。データが送信されていないか、誤りがあります。");
        } else if (e instanceof MissingServletRequestPartException) {
            MissingServletRequestPartException exception = (MissingServletRequestPartException) e;
            log.error("error", exception);
            json.set("msg", "リクエストの送信データに誤りがあります。");
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            HttpRequestMethodNotSupportedException exception =
                    (HttpRequestMethodNotSupportedException) e;
            log.error("error", exception);
            json.set("msg", "無効なHTTPリクエストメソッドです。");
        }
        // 处理后端验证失败产生的异常
        else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) e;
            json.set("msg", exception.getBindingResult().getFieldError().getDefaultMessage());
        }
        // 处理业务异常
        else if (e instanceof ServiceException) {
            log.error("実行エラー", e);
            ServiceException exception = (ServiceException) e;
            json.set("msg", exception.getMsg());
        }
        // 处理参数绑定异常
        else if (e instanceof BindException) {
            // StringBuilder允许在lamda表示中被修改
            StringBuilder str = new StringBuilder();
            log.error("実行エラー", e);
            ((BindException) e).getBindingResult().getAllErrors().forEach((error) -> {
                str.append(error.getDefaultMessage()).append(" ");
            });
            json.put("msg", str.toString().trim());
        }
        // 处理其余的异常
        else {
            log.error("実行エラー", e);
            json.set("msg", "実行エラー");
        }
        return json.toString();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(NotLoginException.class)
    public String unLoginHandler(Exception e) {
        JSONObject json = new JSONObject();
        json.set("error", e.getMessage());
        return json.toString();
    }

}

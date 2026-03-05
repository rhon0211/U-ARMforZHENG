package ucl.group.excelSystem.api.exception;

// 定义自定义异常类
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

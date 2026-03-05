package ucl.group.talentManageSystem.api.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceException extends RuntimeException {
    private String msg;
    private int code = 500;

    public ServiceException(Exception e) {
        super(e);
        this.msg = "実行時エラー";
        this.code = 500;
    }

    public ServiceException(String msg) {
        super(msg);
        this.msg = msg;
        this.code = 500;
    }

    public ServiceException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public ServiceException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public ServiceException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

}

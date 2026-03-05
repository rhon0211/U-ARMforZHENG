package ucl.group.talentManageSystem.api.exception.file;



/**
 * 文件信息异常类
 * 
 * @author hejiale
 */
public class FileException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public FileException(Exception e) {
        super(e);
    }

    public FileException(String msg) {
        super(msg);
    }

    public FileException(String msg, Throwable e) {
        super(msg, e);
    }

    public FileException(String msg, int code) {
        super(msg);
    }

    public FileException(String msg, int code, Throwable e) {
        super(msg, e);
    }
}

package ucl.group.talentManageSystem.api.exception.file;

/**
 * 文件名大小限制异常类
 * 
 * @author hejiale
 */
public class FileSizeLimitExceededException extends FileException {
    private static final long serialVersionUID = 1L;

    public FileSizeLimitExceededException(long defaultMaxSize) {
        super("the filesize is too large");
    }
}

package ucl.group.talentManageSystem.api.exception.file;

/**
 * 文件名称超长限制异常类
 * 
 * @author hejiale
 */
public class FileNameLengthLimitExceededException extends FileException {
    private static final long serialVersionUID = 1L;

    public FileNameLengthLimitExceededException(int defaultFileNameLength) {
        super("the filename is too long");
    }
}

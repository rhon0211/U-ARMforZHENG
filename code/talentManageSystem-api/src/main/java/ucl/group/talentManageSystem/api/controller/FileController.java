package ucl.group.talentManageSystem.api.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ucl.group.talentManageSystem.api.common.R;
import ucl.group.talentManageSystem.api.common.annotation.Log;
import ucl.group.talentManageSystem.api.common.enums.BusinessType;
import ucl.group.talentManageSystem.api.common.utils.StringUtils;
import ucl.group.talentManageSystem.api.common.utils.file.FileUtils;
import ucl.group.talentManageSystem.api.db.pojo.bo.SysFile;
import ucl.group.talentManageSystem.api.service.SysFileService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 文件请求处理
 * 处理skillSheet
 * @author ruoyi
 */
@RestController
@RequestMapping("/api/v1/file")
public class FileController
{
    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private SysFileService sysFileService;
    /**
     * 资源映射路径 前缀
     * /statics
     */
    @Value("${file.prefix}")
    public String localFilePrefix;

    /**
     * 域名或本机访问地址
     * http://127.0.0.1:9300
     */
    @Value("${file.domain}")
    public String domain;

    /**
     * 上传文件存储在本地的根路径
     * D:/hejialebishe/YiZhanShiFileData
     */
    @Value("${file.path}")
    private String localFilePath;
    /**
     * 文件上传请求
     */
    @PostMapping("/upload")
    @SaCheckLogin
    @Log(title = "文件管理", businessType = BusinessType.INSERT)
    public R upload(MultipartFile file)
    {
        try
        {
            // 上传并返回访问地址
            String url = sysFileService.uploadFile(file);
            SysFile sysFile = new SysFile();
            sysFile.setName(FileUtils.getName(url));
            sysFile.setUrl(url);
            return R.ok().put("file",sysFile);
        }
        catch (Exception e)
        {
            log.error("上传文件失败", e);
            return R.error(e.getMessage());
        }
    }

    /**
     * 文件删除
     * @param fileName
     * @return
     */
    @DeleteMapping("/deleteFile")
    @SaCheckLogin
    @Log(title = "文件管理", businessType = BusinessType.DELETE)
    public R delete(@RequestParam String fileName)
    {
        try
        {
            // 调用服务层执行删除操作
            boolean result = sysFileService.deleteFile(fileName);
            if (result) {
                return R.ok("文件删除成功");
            } else {
                return R.error("文件删除失败");
            }
        }
        catch (Exception e)
        {
            log.error("删除文件失败", e);
            return R.error(e.getMessage());
        }
    }
    /**
     * 本地资源通用下载
     * resource 文件路径
     * name 文件名
     */
    //@SaCheckLogin
    @SaCheckPermission(value = {"talent_skill_sheet"}, mode = SaMode.AND)
    @GetMapping("/download")
    public void resourceDownload( String name, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (!FileUtils.checkAllowDownload(name)) {
                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 ", name));
            }
            // 本地资源路径
            String localPath =localFilePath;
            String relativePath = name.replace(domain + localFilePrefix, "");
            // 数据库资源地址
            String downloadPath = localPath + relativePath;
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            if (!name.isEmpty() && name != null) {
                FileUtils.setAttachmentResponseHeader(response, name);
            } else {
                FileUtils.setAttachmentResponseHeader(response, downloadName);
            }
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("下载文件失败", e);

        }
    }


}
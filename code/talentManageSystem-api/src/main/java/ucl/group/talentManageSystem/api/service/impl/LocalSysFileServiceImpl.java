package ucl.group.talentManageSystem.api.service.impl;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ucl.group.talentManageSystem.api.common.utils.FileUploadUtils;
import ucl.group.talentManageSystem.api.service.SysFileService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 本地文件存储 服务器文件存储！ 使用@Primary指定使用哪一个ServiceImpl 对于小型应用或初期项目，使用本地文件存储无需额外的硬件和软件投入，可以快速部署和测试。
 * 本地文件系统的访问速度通常比远程文件系统更快，因为它避免了网络传输的延迟。 可扩展性差：当文件数量增加到一定程度时，本地文件系统的存储容量和访问性能可能成为瓶颈。
 * 高可用性和灾备难以保证：本地文件系统较难实现数据的冗余备份和故障转移，一旦服务器硬件故障，可能会导致数据丢失。
 * 文件共享和协作困难：在分布式系统或微服务架构中，本地文件存储难以满足跨服务或跨地域的文件共享需求。
 * 
 * @author hejiale
 */
@Primary
@Service
public class LocalSysFileServiceImpl implements SysFileService {
    /**
     * 资源映射路径 前缀 /statics
     */
    @Value("${file.prefix}")
    public String localFilePrefix;

    /**
     * 域名或本机访问地址 http://127.0.0.1:9300
     */
    @Value("${file.domain}")
    public String domain;

    /**
     * 上传文件存储在本地的根路径 D:/hejialebishe/YiZhanShiFileData
     */
    @Value("${file.path}")
    private String localFilePath;

    /**
     * 本地文件上传接口
     * http://127.0.0.1:9300/statics/2024/04/01/Weixin%20Image_20240201144837_20240401152509A001.jpg
     * 其实就是 /statics
     * -》D:/hejialebishe/YiZhanShiFileData/2024/04/01/Weixin%20Image_20240201144837_20240401152509A001.jpg
     * 
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        String name = FileUploadUtils.upload(localFilePath, file);
        String url = domain + localFilePrefix + name;
        return url;
    }

    @Override
    public boolean deleteFile(String fileName) throws Exception {
        // 构建完整的文件路径
        /**
         * User http://127.0.0.1:9300/statics/2024/04/01/666_20240401153852A002.jpg 变成
         * localFilePath+ /2024/04/01/666_20240401153852A002.jpg
         */
        String relativePath = fileName.replace(domain + localFilePrefix, "");
        String str = localFilePath + relativePath;
        Path file = Paths.get(str);
        try {

            // 使用Files.deleteIfExists来删除文件，这个方法如果文件存在并成功删除则返回true，否则返回false
            return Files.deleteIfExists(file);
        } catch (IOException e) {
            // 在实际应用中，这里可以具体处理不同的异常情况，例如文件不存在，权限问题等
            throw new Exception("删除文件失败: " + e.getMessage());
        }
    }
}

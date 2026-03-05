package ucl.group.talentManageSystem.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    /**
     * 上传文件存储在本地的根路径
     */
    @Value("${file.path}")
    private String localFilePath;

    /**
     * 资源映射路径 前缀
     */
    @Value("${file.prefix}")
    public String localFilePrefix;



    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {

        /** 本地文件上传路径 */
        registry.addResourceHandler(localFilePrefix + "/**")
                .addResourceLocations("file:" + localFilePath + File.separator);
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 对 /statics/** 进行CORS配置

        registry.addMapping(localFilePrefix + "/**")
                // 设置允许跨域请求的域名
                .allowedOrigins("*")
                .allowedMethods("GET");

//        // 对所有路径进行CORS配置
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH")
                .maxAge(3600);
    }

}
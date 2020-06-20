package com.example.pinche;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 路径映像
 */
@Component
@ConfigurationProperties(prefix = "file")
public class File_init {
    private String staticAccessPath;//反馈图片虚拟路径
    private String uploadFolder;//反馈图片真实路径

    private String staticAccessPath_touxiang;//头像
    private String uploadFolder_touxiang;

    private String staticAccessPath_pascard;//学号信息
    private String uploadFolder_pascard;

    private String staticAccessPath_driver;//身份证驾驶证信息
    private String uploadFolder_driver;


    public String getStaticAccessPath() {
        return staticAccessPath;
    }

    public void setStaticAccessPath(String staticAccessPath) {
        this.staticAccessPath = staticAccessPath;
    }

    public String getUploadFolder() {
        return uploadFolder;
    }

    public void setUploadFolder(String uploadFolder) {
        this.uploadFolder = uploadFolder;
    }

    public String getStaticAccessPath_touxiang() {
        return staticAccessPath_touxiang;
    }

    public void setStaticAccessPath_touxiang(String staticAccessPath_touxiang) {
        this.staticAccessPath_touxiang = staticAccessPath_touxiang;
    }

    public String getUploadFolder_touxiang() {
        return uploadFolder_touxiang;
    }

    public void setUploadFolder_touxiang(String uploadFolder_touxiang) {
        this.uploadFolder_touxiang = uploadFolder_touxiang;
    }

    public String getStaticAccessPath_pascard() {
        return staticAccessPath_pascard;
    }

    public void setStaticAccessPath_pascard(String staticAccessPath_pascard) {
        this.staticAccessPath_pascard = staticAccessPath_pascard;
    }

    public String getUploadFolder_pascard() {
        return uploadFolder_pascard;
    }

    public void setUploadFolder_pascard(String uploadFolder_pascard) {
        this.uploadFolder_pascard = uploadFolder_pascard;
    }

    public String getStaticAccessPath_driver() {
        return staticAccessPath_driver;
    }

    public void setStaticAccessPath_driver(String staticAccessPath_driver) {
        this.staticAccessPath_driver = staticAccessPath_driver;
    }

    public String getUploadFolder_driver() {
        return uploadFolder_driver;
    }

    public void setUploadFolder_driver(String uploadFolder_driver) {
        this.uploadFolder_driver = uploadFolder_driver;
    }
}

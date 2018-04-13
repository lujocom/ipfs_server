package com.blospace.ipfs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <dl>
 * <dt>ipfs_server</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: 成都微积分科技有限公司</dd>
 * <dd>CreateDate: 2018年04月13日</dd>
 * </dl>
 *
 * @author LuoHui
 */
@Component
@ConfigurationProperties("com.blospace.config")
public class IpfsConfig {

    private String filePath;

    private String fileName;

    private String contextRealPath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContextRealPath() {
        return contextRealPath;
    }

    public void setContextRealPath(String contextRealPath) {
        this.contextRealPath = contextRealPath;
    }
}
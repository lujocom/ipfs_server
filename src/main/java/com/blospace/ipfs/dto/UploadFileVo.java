package com.blospace.ipfs.dto;

import java.util.List;

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
public class UploadFileVo {

    private String hashValue;

    private String fileName;

    private String password;

    private List<String> resourceUrlList;

    private List<String> qrCodeRealPath;

    private List<String> qrCodeAbsolutePathList;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHashValue() {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }

    public List<String> getResourceUrlList() {
        return resourceUrlList;
    }

    public void setResourceUrlList(List<String> resourceUrlList) {
        this.resourceUrlList = resourceUrlList;
    }

    public List<String> getQrCodeAbsolutePathList() {
        return qrCodeAbsolutePathList;
    }

    public void setQrCodeAbsolutePathList(List<String> qrCodeAbsolutePathList) {
        this.qrCodeAbsolutePathList = qrCodeAbsolutePathList;
    }

    public List<String> getQrCodeRealPath() {
        return qrCodeRealPath;
    }

    public void setQrCodeRealPath(List<String> qrCodeRealPath) {
        this.qrCodeRealPath = qrCodeRealPath;
    }
}

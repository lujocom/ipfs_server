package com.blospace.ipfs.util.fileutil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author 陆华
 * @描述 图片上传
 * @date 16-7-4 上午9:06
 */
public class FileUpload {
    private String prePath;
    public static final String HEAD = "head";
    public static final String LOGO = "logo";

    public void setPrePath(String prePath) {
        this.prePath = prePath;
    }

    public String getPrePath() {
        return prePath;
    }

    /**
     * @param fileName
     * @param in
     * @throws DirectoryCreateException
     * @throws IOException
     * @描述: 文件上传拷贝
     */
    public String upload( String fileName, InputStream in) throws DirectoryCreateException, IOException {

        String relativePath = "/" ;
        String dirPath = prePath + relativePath;
        FileUtil.chkDirExist(dirPath);

        String fullPath = dirPath + fileName;
        relativePath += "/" + fileName;
        FileUtil.stream2File(in, fullPath);
        return relativePath;
    }

}

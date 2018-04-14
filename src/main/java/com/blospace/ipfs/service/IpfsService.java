package com.blospace.ipfs.service;

import cn.iinda.healthwallet_ipfs_api;
import com.blospace.ipfs.config.IpfsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
@Service
public class IpfsService {

    @Autowired
    private healthwallet_ipfs_api ipfs;

    public String uploadFile2IPFS(MultipartFile file, String password) throws IOException {
        return ipfs.AddStreamFileEncrypt(file.getInputStream(), file.getName(), password);
    }

   /**
     * 以hash值作为存储在本服务器上的文件名称，保证唯一性
     * @param hashValue
     * @param localPath
     * @param password
     * @throws IOException
     */
    public void getResourceUrlByHashValue(String hashValue, String localPath, String password) throws IOException {
        ipfs.GetFileEncrypt(localPath, hashValue , password);
    }

}

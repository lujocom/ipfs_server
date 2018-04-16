package com.blospace.ipfs.service;

import cn.iinda.healthwallet_ipfs_api;
import com.blospace.ipfs.config.IpfsConfig;
import com.blospace.ipfs.dto.UploadFileVo;
import com.blospace.ipfs.util.FileTypeUtil;
import com.blospace.ipfs.util.JsonToFileUtil;
import com.blospace.ipfs.util.QRCodeUtil;
import com.blospace.ipfs.util.RtnJsonUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
@Service
public class IpfsService {
    protected transient final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JsonToFileUtil jsonToFileUtil;

    @Autowired
    private IpfsConfig ipfsConfig;

    @Autowired
    private healthwallet_ipfs_api ipfs;

    public String uploadFile2IPFS(MultipartFile file, String password) throws IOException {
        return ipfs.AddStreamFileEncrypt(file.getInputStream(), file.getName(), password);
    }

    /**
     * 以hash值作为存储在本服务器上的文件名称，保证唯一性
     *
     * @param hashValue
     * @param localRealPath
     * @param password
     * @throws IOException
     */
    private String getResourceUrlByHashValue(String hashValue, String localRealPath, String password) throws IOException {

        String uploadPath = localRealPath + ipfsConfig.getResourcePath() + File.separator;
        File uploadFilePath = new File(uploadPath);
        if (!uploadFilePath.exists()) {
            uploadFilePath.mkdirs();
        }
        String fileName = System.currentTimeMillis() + "";
        InputStream fileInputStream = this.getResourceUrlByHashValue(hashValue, password);
        String fileType = FileTypeUtil.getFileType(fileInputStream);
        fileName += "." + fileType;
        ipfs.GetFileEncrypt(uploadPath + fileName, hashValue, password);
        return fileName;
    }


    /**
     * 以hash值作为存储在本服务器上的文件名称，保证唯一性
     *
     * @param hashValue
     * @param password
     * @throws IOException
     */
    private InputStream getResourceUrlByHashValue(String hashValue, String password) throws IOException {
        return ipfs.GetInStreamEncrypt(hashValue, password);
    }

    /**
     * 获取资源
     *
     * @param hashValue
     * @param password
     * @param realPath
     * @return
     * @throws Exception
     */
    public UploadFileVo getResourceFromIPFS(@PathVariable String hashValue, String password, String realPath) throws Exception {
        logger.debug("从ipfs获取资源-hashValue:{}, password:{}", hashValue, password);
        UploadFileVo resourceInfo = jsonToFileUtil.getData(ipfsConfig, hashValue);
        String fileName;
        if (ObjectUtils.equals(null, resourceInfo)) {
            logger.debug("本地无获取资源记录-创建获取记录-hashValue:{}, password:{}", hashValue, password);
            resourceInfo = new UploadFileVo();
            resourceInfo.setHashValue(hashValue);
            resourceInfo.setPassword(password);
        }
        if (StringUtils.isBlank(resourceInfo.getFileName())) {
            logger.debug("本地资源记录中无文件名称-获取资源-hashValue:{}, password:{}", hashValue, password);
            fileName = this.getResourceUrlByHashValue(hashValue, realPath, password);
            resourceInfo.setFileName(fileName);
            logger.debug("hashValue:{}, password:{}-获取资源名称:{}", hashValue, password, fileName);
        }
        File resourceFile = new File(realPath + File.separator + ipfsConfig.getResourcePath() + File.separator + resourceInfo.getFileName());
        if (!resourceFile.exists()) {
            logger.debug("本地资源记录中有文件名称但无资源-获取资源-hashValue:{}, password:{}", hashValue, password);
            fileName = this.getResourceUrlByHashValue(hashValue, realPath, password);
            resourceInfo.setFileName(fileName);
            logger.debug("hashValue:{}, password:{}-获取资源名称:{}", hashValue, password, fileName);
        }

        String url = ipfsConfig.getHostName() + "/ipfs/page/resource/" + resourceInfo.getFileName();
        resourceInfo.setResourceUrlList(Lists.newArrayList(url));
        return resourceInfo;
    }

    /**
     * 根据获取资源记录生成二维码
     *
     * @param resourceInfo
     * @param realPath
     * @param contextPath
     * @param number
     */
    public void generateQRCode(UploadFileVo resourceInfo, String realPath, String contextPath, Integer number) {
        String qrUrl = realPath + ipfsConfig.getQrTpl();
        logger.debug("二维码模板路径：{}", qrUrl);
        String destFileFolder = realPath + "qrcode" + File.separator + resourceInfo.getHashValue() + File.separator;
        String absoluteFolder = contextPath + "qrcode" + File.separator + resourceInfo.getHashValue() + File.separator;
        String destFileTemplate = "%s.png";
        List<String> qrCodePath = Lists.newArrayList();
        List<String> qrCodeRealPath = Lists.newArrayList();
        File tempFile = new File(destFileFolder);
        if (tempFile.exists()) {
            File[] files = tempFile.listFiles();
            for (File file : files)
                file.delete();
        }
        QRCodeUtil.mkdirs(destFileFolder);
        if (number == 0) {
            number = 1;
        }
        for (int i = 0; i < number; i++) {
            try {
                String destFile = String.format(destFileTemplate, i + 1);
                qrCodePath.add(absoluteFolder + destFile);
                logger.debug("路径:{}", destFileFolder + destFile);
                qrCodeRealPath.add(destFileFolder + destFile);
                QRCodeUtil.encodeByTemplate(resourceInfo.getResourceUrlList().get(0), qrUrl, 2480, 3348, 468, 817, 1554, 1554, null, destFileFolder + destFile, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        resourceInfo.setQrCodeAbsolutePathList(qrCodePath);
        resourceInfo.setQrCodeRealPath(qrCodeRealPath);
        jsonToFileUtil.saveData(ipfsConfig, resourceInfo);
    }


}

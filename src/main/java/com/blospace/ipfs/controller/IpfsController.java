package com.blospace.ipfs.controller;

import com.blospace.ipfs.base.BaseController;
import com.blospace.ipfs.dto.UploadFileVo;
import com.blospace.ipfs.service.IpfsService;
import com.blospace.ipfs.util.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * <dl>
 * <dt>ipfs_server</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: 成都微积分科技有限公司</dd>
 * <dd>CreateDate: 2018年04月12日</dd>
 * </dl>
 *
 * @author LuoHui
 */
@RestController
@RequestMapping("/ipfs")
public class IpfsController extends BaseController {

    @Autowired
    private IpfsService ipfsService;
    @Autowired
    private JsonToFileUtil jsonToFileUtil;

    @RequestMapping("/api/save/{hashValue}")
    public RtnJson saveJsonData(@PathVariable String hashValue) {
        logger.debug("getUploadData:{}", jsonToFileUtil.getData(this.getIpfsConfig(), hashValue));
        return RtnJsonUtil.success();
    }

    @RequestMapping("/api/upload-test")
    public RtnJson uploadTest() {
        UploadFileVo uploadFileVo = new UploadFileVo();
        uploadFileVo.setHashValue("wqwqwqw");
        uploadFileVo.setResourceUrlList(Lists.newArrayList("dsadsa"));
        jsonToFileUtil.saveData(this.getIpfsConfig(), uploadFileVo);
        return RtnJsonUtil.success();
    }

    @PostMapping("/api/upload")
    public RtnJson uploadImg(@RequestParam(name = "file") MultipartFile file, @RequestParam("password") String password, String name
            , HttpServletRequest request, HttpServletResponse response) {

        logger.debug("Link uploadDishImg start");
        String resourceUrl = "";
        UploadFileVo saveInfo = new UploadFileVo();
        saveInfo.setPassword(password);
        saveInfo.setFileName(file.getOriginalFilename());
        logger.info("-----文件大小:{}----- ", file.getSize());
        logger.info("-----文件类型:{}-----", file.getContentType());
        logger.info("-----表单名称:{}-----", file.getName());
        logger.info("-----文件原名:{}-----", file.getOriginalFilename());

        String hashValue = "";

        if (file.getSize() > 0) {
            try {
                hashValue = ipfsService.uploadFile2IPFS(file, password);
                /*String uploadPath = this.getServletRealPath() + "file" + File.separator;
                File uploadFilePath = new File(uploadPath);
                if (!uploadFilePath.exists()) {
                    uploadFilePath.mkdirs();
                }
                logger.debug("upload file path:{}", uploadPath);
                FileUtils.copyInputStreamToFile(file.getInputStream(),
                        new File(uploadPath, file.getOriginalFilename()));*/
            } catch (Exception e) {
                return RtnJsonUtil.error("上传失败....");
            }
        }
        Map<String, Object> resultData = Maps.newHashMap();
        resultData.put("hashValue", hashValue);

//        保存上传记录
        saveInfo.setHashValue(hashValue);
        saveInfo.setResourceUrlList(Lists.newArrayList(resourceUrl));
        jsonToFileUtil.saveData(this.getIpfsConfig(), saveInfo);

        return RtnJsonUtil.success(resultData);
    }

    @PostMapping("/api/resource/{hashValue}")
    public RtnJson showResource(@PathVariable String hashValue, String password) {
        Map<String, Object> resultData = Maps.newHashMap();

        UploadFileVo uploadFileVo = null;
        try {
            resultData.put("hash", hashValue);

//            uploadFileVo = jsonToFileUtil.getData(this.getIpfsConfig(), hashValue);
            uploadFileVo = ipfsService.getResourceFromIPFS(hashValue, password, this.getServletRealPath());
            resultData.put("resourcePath", this.getIpfsConfig().getResourcePath() + "/" + uploadFileVo.getFileName());
            String fileType = uploadFileVo.getFileName().substring(uploadFileVo.getFileName().lastIndexOf(".")).toLowerCase();
            String showType = "";
            if (getIpfsConfig().getImageType().contains(fileType)) {
                showType = "image";
            } else if (getIpfsConfig().getVideoType().contains(fileType)) {
                showType = "video";
            }
            resultData.put("showType", showType);
        } catch (Exception e) {
            e.printStackTrace();
            return RtnJsonUtil.error("获取资源失败");
        }
        return RtnJsonUtil.success(resultData);
    }

    @PostMapping("/api/qr-code/{hashValue}")
    public RtnJson showQRCode(@PathVariable String hashValue, Integer number, String password) {
        Map<String, Object> resultData = Maps.newHashMap();

        UploadFileVo uploadFileVo = null;
        try {
            uploadFileVo = ipfsService.getResourceFromIPFS(hashValue, password, this.getServletRealPath());
            ipfsService.generateQRCode(uploadFileVo, this.getServletRealPath(), this.getServletContextPath(), number);
            resultData.put("hash", hashValue);
            resultData.put("qrCodeList", uploadFileVo.getQrCodeAbsolutePathList());
        } catch (Exception e) {
            e.printStackTrace();
            return RtnJsonUtil.error("获取二维码失败");
        }
        return RtnJsonUtil.success(resultData);
    }

    @PostMapping("/api/export-qr-code/{hashValue}")
    public void exportQRCode(@PathVariable String hashValue, HttpServletResponse response) {
        logger.debug("导出二维码");
        String destZipFile = this.getServletRealPath() + "qrcode" + File.separator + hashValue + ".zip";
        String destFileFolder = this.getServletRealPath() + "qrcode" + File.separator + hashValue + File.separator;
        File tempFile = new File(destFileFolder);
        if (tempFile.exists()) {
            logger.debug("二维码zip路径:{}", destZipFile);
            ZipUtils.zipDirectory(destFileFolder, destZipFile);
            DownloadUtils.download(destZipFile, response);
        }
    }

}

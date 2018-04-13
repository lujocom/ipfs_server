package com.blospace.ipfs.controller;

import com.blospace.ipfs.base.BaseController;
import com.blospace.ipfs.dto.UploadFileVo;
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
        String resourceUrl = "dsadsadsa";

        logger.info("-----文件大小:{}----- ", file.getSize());
        logger.info("-----文件类型:{}-----", file.getContentType());
        logger.info("-----表单名称:{}-----", file.getName());
        logger.info("-----文件原名:{}-----", file.getOriginalFilename());

        if (file.getSize() > 0) {
            String uploadPath = this.getServletRealPath() + "uploadFile" + File.separator;
            File uploadFilePath = new File(uploadPath);
            if (!uploadFilePath.exists()) {
                uploadFilePath.mkdirs();
            }
            logger.debug("upload file path:{}", uploadPath);
            try {
                FileUtils.copyInputStreamToFile(file.getInputStream(),
                        new File(uploadPath, file.getOriginalFilename()));
            } catch (Exception e) {
                return RtnJsonUtil.error("上传失败....");
            }
        }
        Map<String, Object> resultData = Maps.newHashMap();
        String hashValue = "dasdadasdsacdsadas";
        resultData.put("hashValue", hashValue);

//        保存上传记录
        UploadFileVo saveInfo = new UploadFileVo();
        saveInfo.setPassword(password);
        saveInfo.setHashValue(hashValue);
        saveInfo.setResourceUrlList(Lists.newArrayList(resourceUrl));
        jsonToFileUtil.saveData(this.getIpfsConfig(), saveInfo);

        return RtnJsonUtil.success(resultData);
    }

    @PostMapping("/api/qr-code/{hashValue}")
    public RtnJson showQRCode(@PathVariable String hashValue, Integer number, String password) {

        UploadFileVo saveInfo = jsonToFileUtil.getData(this.getIpfsConfig(), hashValue);
        if (ObjectUtils.equals(null, saveInfo)) {
            return RtnJsonUtil.error("此hash码无效");
        }

        if (!StringUtils.equals(password, saveInfo.getPassword()) && StringUtils.isNotBlank(saveInfo.getPassword())) {
            return RtnJsonUtil.error("此hash码对应的资源密码错误");
        }

        String qrUrl = "";
        String url = this.getServletContextPath() + "/qrcode/";
        String destFileFolder = this.getServletRealPath() + "qrcode" + File.separator + hashValue + File.separator;
        String absoluteFolder = this.getServletContextPath() + "qrcode" + File.separator + hashValue + File.separator;
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
                QRCodeUtil.encodeByTemplate(url, qrUrl, 2480, 3508, 760, 1334, 990, 990, null, destFileFolder + destFile, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        saveInfo.setQrCodeAbsolutePathList(qrCodePath);
        saveInfo.setQrCodeRealPath(qrCodeRealPath);
        jsonToFileUtil.saveData(this.getIpfsConfig(), saveInfo);

        Map<String, Object> resultData = Maps.newHashMap();
        resultData.put("hash", hashValue);
        resultData.put("qrCodeList", qrCodePath);

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

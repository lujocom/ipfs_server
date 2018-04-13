package com.blospace.ipfs.controller;

import com.blospace.ipfs.base.BaseController;
import com.blospace.ipfs.config.IpfsConfig;
import com.blospace.ipfs.dto.UploadFileVo;
import com.blospace.ipfs.util.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.sf.json.JSONObject;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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

    /*@Autowired
    private FileUpload fileUpload;*/

    @RequestMapping("/api/save/{hashValue}")
    public RtnJson saveJsonData(@PathVariable String hashValue) {

        /*UploadFileVo uploadFileVo = new UploadFileVo();
        uploadFileVo.setHashValue(hashValue);
        uploadFileVo.setResourceUrlList(Lists.newArrayList("dsadsa"));
        uploadFileVo.setQrCodeList(Lists.newArrayList("/qrcode/2121/1.png", "/qrcode/2121/2.png"));
        jsonToFileUtil.saveData(ipfsConfig, uploadFileVo);*/
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

    @RequestMapping("/api/upload")
    public void uploadImg(@RequestParam("file") MultipartFile myfile, @RequestParam("password") String pass,
                          HttpServletRequest request, HttpServletResponse response) throws IOException {

        logger.debug("Link uploadDishImg start");
        RtnJson result;
        PrintWriter out;
        boolean flag = false;
        String resourceUrl = "dsadsadsa";

        if (myfile.getSize() > 0) {
            String uploadPath = this.getServletRealPath() + "uploadFile/";
            File file = new File(uploadPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            logger.debug("upload file path:{}", uploadPath);
            try {
                FileUtils.copyInputStreamToFile(myfile.getInputStream(),
                        new File(uploadPath, myfile.getOriginalFilename()));
                flag = true;
            } catch (Exception e) {

            }
        }
        out = response.getWriter();
        if (flag == true) {
            out.print("1");
        } else {
            out.print("2");
        }
    }

    @PostMapping("/api/qr-code/{hashValue}")
    public RtnJson showQRCode(@PathVariable String hashValue, Integer number, String password) {

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

        UploadFileVo saveInfo = jsonToFileUtil.getData(this.getIpfsConfig(), hashValue);
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

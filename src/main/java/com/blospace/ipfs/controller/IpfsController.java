package com.blospace.ipfs.controller;

import com.blospace.ipfs.base.BaseController;
import com.blospace.ipfs.util.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
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

    /*@Autowired
    private FileUpload fileUpload;*/

    @RequestMapping("/api/upload")
    public void uploadImg(@RequestParam("file") MultipartFile myfile, @RequestParam("password") String pass,
                          HttpServletRequest request, HttpServletResponse response) throws IOException {

        logger.debug("Link uploadDishImg start");
        RtnJson result;
        PrintWriter out;
        boolean flag = false;
        if (myfile.getSize() > 0) {
            String path = "/src/main/webapp/qrcode/";
            String uploadPath = request.getSession().getServletContext().getRealPath(path);
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
        String destFileFolder = this.getServletRealPath() + "/qrcode/" + hashValue + "/";
        String absoluteFolder = this.getServletContextPath() + "/qrcode/" + hashValue + "/";
        String destFileTemplate = "%s.png";
        List<String> qrCodePath = Lists.newArrayList();
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
                QRCodeUtil.encodeByTemplate(url, qrUrl, 2480, 3508, 760, 1334, 990, 990, null, destFileFolder + destFile, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Map<String, Object> resultData = Maps.newHashMap();
        resultData.put("hash", hashValue);
        resultData.put("qrCodeList", qrCodePath);
        return RtnJsonUtil.success(resultData);
    }

    @PostMapping("/api/export-qr-code/{hashValue}")
    public void exportQRCode(@PathVariable String hashValue, HttpServletResponse response) {
        logger.debug("导出二维码");
        String destZipFile = this.getServletRealPath() + "/qrcode/" + hashValue + ".zip";
        String destFileFolder = this.getServletRealPath() + "/qrcode/" + hashValue + "/";
        File tempFile = new File(destFileFolder);
        if (tempFile.exists()) {
            logger.debug("二维码zip路径:{}", destZipFile);
            ZipUtils.zipDirectory(destFileFolder, destZipFile);
            DownloadUtils.download(destZipFile, response);
        }
    }


}

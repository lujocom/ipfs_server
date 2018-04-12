package com.blospace.ipfs.controller;

import com.blospace.ipfs.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <dl>
 * <dt>ipfs_server</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: 成都微积分科技有限公司</dd>
 * <dd>CreateDate: 2018年04月11日</dd>
 * </dl>
 *
 * @author LuoHui
 */
@Controller
public class PageController extends BaseController{


    @GetMapping("/upload")
    public String uploadPage(){
        return "upload/html/upload";
    }

    @GetMapping("demo")
    public String demo() {
        System.out.println("demo");
        return "demo";
    }

    @RequestMapping("dicomviewer")
    public String dicomviewer(HttpServletRequest request, Model model) {

        String userAgent = request.getHeader("USER-AGENT").toLowerCase();
        String imageType = "dicom";
        //
        //查询判断是dicom图像还是jpg图像
        //
        String urls = request.getParameter("urls");
        String dicompage = "../../engin/ipacsdvpc.html?urls=" + urls;
        if (null == userAgent) {
            userAgent = "";
        }
        if (check(userAgent).equals("Phone") || check(userAgent).equals("Table")) {
            dicompage = "../../engin/ipacsdvtab.html?urls=" + urls;
        }
        if ("image".equals(imageType)) {
            dicompage = "../../engin/ipacsdvus.html?urls=" + urls;
        }
        model.addAttribute("type", "image");
        model.addAttribute("dicompage", dicompage);
        return "dicomviewer";
    }

    //获取访问设备类型
    public String check(String userAgent) {
        String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i|windows (phone|ce)|blackberry"
                + "|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp|laystation portable)|nokia|fennec|htc[-_]"
                + "|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
        String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
        Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);
        Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);
        if (null == userAgent) {
            userAgent = "";
        }
        // 匹配
        Matcher matcherPhone = phonePat.matcher(userAgent);
        Matcher matcherTable = tablePat.matcher(userAgent);

        if (matcherTable.find()) {
            return "Table";
        } else if (matcherPhone.find()) {
            return "Table";
        } else {
            return "PC";
        }
    }

    //获取访问者IP
    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }








}

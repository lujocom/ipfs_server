package com.blospace.ipfs.util;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;

public class DownloadUtils {

 public static boolean download(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            
            if (!file.exists()) {
            	response.sendError(404, "File not found!");
            	return false;
   	     	}
            
            // 取得文件名。
            String filename = file.getName();
            
            // 设置response的Header
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="
    				+ new String((filename).getBytes("utf-8"), "iso-8859-1"));
            
            FileInputStream inputStream = new FileInputStream(file);
            //3.通过response获取ServletOutputStream对象(out)  
            ServletOutputStream out = response.getOutputStream();
            int b = 0;
            byte[] buffer = new byte[2048];
            while ((b = inputStream.read(buffer)) != -1){  
                //4.写到输出流(out)中  
                out.write(buffer,0,b);  
            }  
            inputStream.close();  
            out.close();  
            out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return true;
    }
 
	 public static boolean download(String filePath, HttpServletResponse response, boolean isOnLine) throws Exception {
	     File f = new File(filePath);
	     if (!f.exists()) {
	         response.sendError(404, "File not found!");
	         return false;
	     }
	     BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
	     byte[] buf = new byte[1024];
	     int len = 0;
	
	     response.reset(); // 非常重要
	     if (isOnLine) { // 在线打开方式
	         URL u = new URL("file:///" + filePath);
	         response.setContentType(u.openConnection().getContentType());
	         String name = new String(f.getName().getBytes("utf-8"),"iso8859-1");
	         response.setHeader("Content-Disposition", "inline; filename=" + name);
	         // 文件名应该编码成UTF-8
	     } else { // 纯下载方式
	         response.setContentType("application/x-msdownload");
	         response.setHeader("Content-Length", "" + f.length());
	         String name = new String(f.getName().getBytes("utf-8"),"iso8859-1");
	         response.setHeader("Content-Disposition", "attachment; filename=" + name);
	     }
	     OutputStream out = response.getOutputStream();
	     while ((len = br.read(buf)) > 0)
	         out.write(buf, 0, len);
	     br.close();
	     out.close();
	     
	     return true;
	 }

}

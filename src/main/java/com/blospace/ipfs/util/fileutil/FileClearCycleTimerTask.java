package com.blospace.ipfs.util.fileutil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.TimerTask;

/**
 * @描述: 文件定时清理
 * @author 陆华
 */
public class FileClearCycleTimerTask extends TimerTask {
	private final static Logger log = LoggerFactory.getLogger(FileClearCycleTimerTask.class);
	
	private static ServletContext context ;		//web 上下文
	
	private static String tmpDirPath; 				//临时文件存放路径

	public static String getTmpDirPath() {
		return tmpDirPath;
	}

	public static void setTmpDirPath(String tmpDirPath) {
		FileClearCycleTimerTask.tmpDirPath = tmpDirPath;
	}

	public static ServletContext getContext() {
		return context;
	}

	public static void setContext(ServletContext context) {
		FileClearCycleTimerTask.context = context;
	}

	/**
	 * @描述: 文件清理
	 */
	public  void clearTempFiles() {
		if(FileClearCycleTimerTask.tmpDirPath == null ) {
			ApplicationContext cxt = WebApplicationContextUtils.getWebApplicationContext(context);
			tmpDirPath = (String) cxt.getBean("TmpDirPath");
//			tmpDirPath = InitSystem.getSystemPath() + "/" + tmpDirPath;
		}
		
		File tmpDir = new File(tmpDirPath) ;
		if(tmpDir.exists()) {
			try {
				log.info("delete tmp files ...");
				FileUtil.deleteAllFiles(tmpDir); 
				tmpDir.mkdir();
				log.info("delete tmp files success.");
			} catch (Exception ex) {
				log.error("clear qrcode file error !!", ex);
				ex.printStackTrace();
			}
		} else {
			try {
				log.info("create tmp directory ...");
				tmpDir.mkdir();
				log.info("create tmp directory success...");
			} catch (Exception e) {
				log.error("create tmp dir failured !!", e);
				e.printStackTrace();
			}
			
		}
		
	}

	@Override
	public void run() {
		clearTempFiles();
	}
}

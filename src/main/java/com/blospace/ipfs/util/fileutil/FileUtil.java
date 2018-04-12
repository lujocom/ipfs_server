package com.blospace.ipfs.util.fileutil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @描述: 文件操作
 * @author 陆华
 */
public class FileUtil {
	private final static Logger log = LoggerFactory.getLogger(FileUtil.class);
	/**
	 * 缓冲区大小，默认为8k字节
	 */
	private static final int BUFFER_SIZE = 1024*8;

	/**
	 * @描述: 检查文件是否存在，如果不存在就新建一个
	 * @param dirPath
	 * @throws DirectoryCreateException
     */
	public static void chkDirExist(String dirPath) throws DirectoryCreateException{
		//文件夹路径
		File dirFile = new File(dirPath);
        //判断是否文件夹已存在，不存在就新建
		chkDirExist(dirFile);
	}

	/**
	 * @描述: 检查文件是否存在，如果不存在就新建一个
	 * @param dirFile
	 * @throws DirectoryCreateException
     */
	public static void chkDirExist(File dirFile) throws DirectoryCreateException{
        //判断是否文件夹已存在，不存在就新建
		if ( ! (dirFile.exists())  &&   ! (dirFile.isDirectory())) {
            boolean  isCreated  =  dirFile.mkdirs();
            if (isCreated) {
               log.info("make dir success!");
            } else {
               log.error("make dir error!!!");  
               throw new DirectoryCreateException();
            }
       } 
	}

	/**
	 * @描述:  文件拷贝
	 * @param srcFile
	 * @param destFile
	 * @throws IOException
     */
	public static void file2file(String srcFile, String destFile) throws IOException {
		InputStream srcStream = new BufferedInputStream(new FileInputStream(srcFile));
        OutputStream destStream = new BufferedOutputStream(new FileOutputStream(destFile));
        stream2Stream(srcStream,destStream);
	}


    /**
	 * @描述:  文件拷贝
	 * @param srcFile
	 * @param destFile
	 * @throws IOException
     */
	public static void file2file(File srcFile, File destFile) throws IOException {
		InputStream srcStream = new BufferedInputStream(new FileInputStream(srcFile));
        OutputStream destStream = new BufferedOutputStream(new FileOutputStream(destFile));
        stream2Stream(srcStream,destStream);
	}

	/**
	 * @描述:   文件转出到输出流
	 * @param srcFile
	 * @param destStream
	 * @throws IOException
     */
	public static void file2Stream(File srcFile, OutputStream destStream) throws IOException {
		InputStream srcStream = new BufferedInputStream(new FileInputStream(srcFile));
        stream2Stream(srcStream,destStream);
	}

	/**
	 * @描述: 文件转出到输出流
	 * @param srcFile
	 * @param destStream
	 * @throws IOException
     */
	public static void file2Stream(String srcFile, OutputStream destStream) throws IOException {
		InputStream srcStream = new BufferedInputStream(new FileInputStream(srcFile));
        stream2Stream(srcStream,destStream);
	}


	/**
	 * @描述: 输入流转出到文件
	 * @param srcStream
	 * @param destFile
	 * @throws IOException
     */
	public static void stream2File(InputStream srcStream, String destFile) throws IOException {
        OutputStream destStream = new BufferedOutputStream(new FileOutputStream(destFile));
        stream2Stream(srcStream,destStream);
	}

	/**
	 * @描述: 输入流转出到文件
	 * @param srcStream
	 * @param destFile
	 * @throws IOException
     */
	public static void stream2File(InputStream srcStream, File destFile) throws IOException {
        OutputStream destStream = new BufferedOutputStream(new FileOutputStream(destFile));
        stream2Stream(srcStream,destStream);
	}


	/**
	 * @描述; 输入流转出到输出流
	 * @param srcStream
	 * @param destStream
	 * @throws IOException
     */
	public static void stream2Stream(InputStream srcStream, OutputStream destStream) throws IOException {
		try {
			byte[] buffer = new byte[BUFFER_SIZE];
			int length = 0;
			while((length = srcStream.read(buffer)) > 0){
                destStream.write(buffer,0,length);
			}
		} finally {
			srcStream.close();
			destStream.close();
		}
	}

	/**
	 * @描述  删除文件
	 * @param fileName
     */
	public static void deleteFile(String fileName){
		File file = new File(fileName);
		file.delete();
	}


	/**
	 * @描述  文件上传
	 * @param file
	 * @param path
	 * @param fileName
     * @return
     */
	public static String fileUpload(File file, String path, String fileName) {
		//目标文件
		File destFile = new File(path,fileName);
		try {
			
			//判断是否文件夹已存在，不存在就新建
			FileUtil.chkDirExist(path);
			//保存文件
			FileUtil.file2file(file, destFile);
			
		} catch (FileNotFoundException e) {
			log.info("文件没有找到！！");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			log.info("文件传送或者保存发生IO异常！！");
			e.printStackTrace();
			return null;
		} catch (DirectoryCreateException e) {
			log.info("文件夹创建失败！！");
			e.printStackTrace();
			return null;
		}
		
		//返回保存文件路径
		return destFile.getAbsolutePath()+"/"+destFile.getName();
	}

	/**
	 * @描述: 删除该文件夹及其下所有目录
	 * @param file
     */
	public static void deleteAllFiles(File file){
		if(file.isDirectory()) {
			for(File subFile : file.listFiles()){
				deleteAllFiles(subFile);
			}
		}
		file.delete();
	}

}

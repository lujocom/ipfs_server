package com.blospace.ipfs.util.fileutil;

public class DirectoryCreateException extends Exception {
	
	private static final long serialVersionUID = 4955707634562261435L;

	public DirectoryCreateException(){
		super("文件夹创建失败！");
	}
}

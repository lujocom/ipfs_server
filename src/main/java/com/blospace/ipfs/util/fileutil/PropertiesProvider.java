/**
 *@desc
 *@author Administrator
 *@date May 31, 2012
 *@path com.est.common.ext.util.excelexport.ExcelTplProvider
 *@corporation Enstrong S&T 
 */
package com.blospace.ipfs.util.fileutil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @描述: 资源文件管理  Properties 管理
 */
public class PropertiesProvider {
	private Properties pro = new Properties();
	private static PropertiesProvider provider = null;

	/**
	 * @描述: 单例生成
	 * @return
     */
	public synchronized static PropertiesProvider getInstance() {

		if (provider == null) {
			provider = new PropertiesProvider();
		}

		return provider;
	}

	private PropertiesProvider() {

	}

	/**
	 * @描述: 查找资源文件
	 * @param key
	 * @return
     */
	public String getProperty(String key) {
		return pro.getProperty(key);
	}

	/**
	 * @描述: 载入资源文件
	 * @param propertiesName
	 * @throws IOException
     */
	public void load(String propertiesName) throws IOException {

		InputStream inStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(propertiesName);

		pro.load(inStream);

	}

}

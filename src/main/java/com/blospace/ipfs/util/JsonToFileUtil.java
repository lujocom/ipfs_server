package com.blospace.ipfs.util;


import com.blospace.ipfs.config.IpfsConfig;
import com.blospace.ipfs.dto.UploadFileVo;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * <dl>
 * <dt>ipfs_server</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: 成都微积分科技有限公司</dd>
 * <dd>CreateDate: 2018年04月13日</dd>
 * </dl>
 *
 * @author LuoHui
 */
@Component
public class JsonToFileUtil {
    protected transient final Logger logger = LoggerFactory.getLogger(getClass());

    private String buildFullPath(IpfsConfig ipfsConfig) {
        return ipfsConfig.getContextRealPath() + ipfsConfig.getFilePath() + File.separator + ipfsConfig.getFileName();
    }

    public boolean write2JSONFile(IpfsConfig ipfsConfig, String jsonString) {
        // 标记文件生成是否成功
        boolean flag = true;

        // 拼接文件完整路径
        String fullPath = buildFullPath(ipfsConfig);
        logger.debug("write json file path :{}", fullPath);
        // 生成json格式文件
        try {
            // 保证创建一个新文件
            File file = new File(fullPath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) { // 如果已存在,删除旧文件
                file.createNewFile();
            }
            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(jsonString);
            write.flush();
            write.close();
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        // 返回是否成功的标记
        return flag;
    }

    public JSONObject getJsonFromFile(IpfsConfig ipfsConfig) {
        String fullPath = buildFullPath(ipfsConfig);
        logger.debug("read json file path :{}", fullPath);

        File jsonFile = new File(fullPath);
        if (!jsonFile.exists()) {
            return null;
        }
        JSONObject dataJson = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(fullPath));
            String s = null, ws = null;
            while ((s = br.readLine()) != null) {
                dataJson = JSONObject.fromObject(s);// 创建一个包含原始json串的json对象
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataJson;
    }

    public void saveData(IpfsConfig ipfsConfig, UploadFileVo uploadFileVo) {
        JSONObject jsonObject = this.getJsonFromFile(ipfsConfig);
        if (ObjectUtils.equals(null, jsonObject)) {
            jsonObject = new JSONObject();
        }
        jsonObject.put(uploadFileVo.getHashValue(), uploadFileVo);
        this.write2JSONFile(ipfsConfig, jsonObject.toString());
    }

    public UploadFileVo getData(IpfsConfig ipfsConfig, String hashValue) {
        JSONObject jsonObject = this.getJsonFromFile(ipfsConfig);
        if (ObjectUtils.equals(null, jsonObject) || !jsonObject.containsKey(hashValue)) {
            return null;
        }
        return (UploadFileVo) JSONObject.toBean(jsonObject.getJSONObject(hashValue), UploadFileVo.class);
    }


}

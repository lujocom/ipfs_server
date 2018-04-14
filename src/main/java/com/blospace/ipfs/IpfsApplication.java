package com.blospace.ipfs;

import cn.iinda.healthwallet_ipfs_api;
import com.blospace.ipfs.config.IpfsConfig;
import com.blospace.ipfs.listener.CustomMultipartResolver;
import com.blospace.ipfs.util.fileutil.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@SpringBootApplication
public class IpfsApplication {

    @Autowired
    private IpfsConfig ipfsConfig;

    public static void main(String[] args) {
        SpringApplication.run(IpfsApplication.class, args);
    }

    @Bean()
    public healthwallet_ipfs_api healthwallet_ipfs_api() {
        return new healthwallet_ipfs_api(ipfsConfig.getConnstr());
    }


}

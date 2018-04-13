package com.blospace.ipfs;

import com.blospace.ipfs.listener.CustomMultipartResolver;
import com.blospace.ipfs.util.fileutil.FileUpload;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@SpringBootApplication
public class IpfsApplication {

    public static void main(String[] args) {
        SpringApplication.run(IpfsApplication.class, args);
    }

    /*@Bean(name = "multipartResolver")
    public CustomMultipartResolver customMultipartResolver() {
        return new CustomMultipartResolver();
    }
*/
    /*@Bean
    public FileUpload fileUpload(){
        FileUpload fileUpload =  new FileUpload();
        fileUpload.setPrePath("/webapp/upload");
        return fileUpload;
    }*/

    /*@Bean(name = "multipartResolver")
    public CommonsMultipartResolver getCommonsMultipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        return multipartResolver;
    }*/

}

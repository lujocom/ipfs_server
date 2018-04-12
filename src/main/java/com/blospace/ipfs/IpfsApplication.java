package com.blospace.ipfs;

import com.blospace.ipfs.listener.CustomMultipartResolver;
import com.blospace.ipfs.util.fileutil.FileUpload;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IpfsApplication {

    public static void main(String[] args) {
        SpringApplication.run(IpfsApplication.class, args);
    }

    @Bean
    public CustomMultipartResolver customMultipartResolver() {
        return new CustomMultipartResolver();
    }

    /*@Bean
    public FileUpload fileUpload(){
        FileUpload fileUpload =  new FileUpload();
        fileUpload.setPrePath("/webapp/upload");
        return fileUpload;
    }*/

}

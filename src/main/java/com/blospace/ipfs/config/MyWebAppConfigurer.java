package com.blospace.ipfs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @version V3.0
 * @Title: MyWebAppConfigurer
 * @Company: 成都影达科技有限公司
 * @Description: 描述
 * @author: 东进
 * @date 2018-02-28 16:25
 */
@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/templates/**/*.js").addResourceLocations("classpath:/templates/");
//        registry.addResourceHandler("/templates/**/*.css").addResourceLocations("classpath:/templates/");
//        registry.addResourceHandler("/templates/**/*.jpg").addResourceLocations("classpath:/templates/");
//        registry.addResourceHandler("/templates/**/*.png").addResourceLocations("classpath:/templates/");
        registry.addResourceHandler("/src/main/webapp/**").addResourceLocations("classpath:/webapp/");

        super.addResourceHandlers(registry);
    }
}

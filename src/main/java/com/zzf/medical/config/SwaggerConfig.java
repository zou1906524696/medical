package com.zzf.medical.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zzf.medical.controller"))
                .build();
    }
    /**
     * 配置swagger信息apiInfo
     * */
    private ApiInfo apiInfo(){
        Contact contact = new Contact("邹志峰", "no", "1906524696@qq.com");
        return new ApiInfo("GoTravel Api Swagger 文档",
                "仿蚂蜂窝Api文档",
                "1.0","no",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",new ArrayList());
    }
}
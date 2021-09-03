package com.restapi.mobileappws.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


//http://localhost:2009/mobile-app-ws/swagger-ui/index.html
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    Contact contact = new Contact(
            "Bandipo Taiye",
            "http://www.baog.com",
            "bandipotaiye@gmail.com"
    );

    List<VendorExtension> vendorExtensions = new ArrayList<>();

    ApiInfo apiInfo = new ApiInfo(
            "Photo app RESTful Web Service documentation",
            "This pages documents Photo app RESTful Web Service endpoints",
            "1.0",
            "http://www.appsdeveloperblog.com/service.html",
            contact,
            "Apache 2.0",
            "http://www.apache.org/licenses/LICENSE-2.0",
            vendorExtensions);//TODO: research on how to use vendorExtensions


    @Bean
    public Docket apiDocket() {

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .protocols(new HashSet<>(Arrays.asList("HTTP","HTTPs")))
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.restapi.mobileappws"))//we provide classes which need to
                //provided in Swagger
                .paths(PathSelectors.any())//all the paths of our api are selected and automatically generated for us
                .build();

        return docket;

    }
}

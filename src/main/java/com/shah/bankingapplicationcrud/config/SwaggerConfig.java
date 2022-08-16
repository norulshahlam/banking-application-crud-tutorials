package com.shah.bankingapplicationcrud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	private final String name = "Norulshahlam";
	private final String url = "www.google.com";
	private final String email = "norulshahlam@gmail.com";
	
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
        		.apiInfo(apiInfo())
                .select()                 
                .apis(RequestHandlerSelectors.basePackage("com.shah.bankingapplicationcrud"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .globalOperationParameters(operationParameters());

    }
    
    private ApiInfo apiInfo() {
    	
    	return new ApiInfoBuilder()
    			.description("Hello World")
    			.title("REST API CRUD operations")
    			.contact(contact())
    			.license("License number WWE87ASR4GH")
    			.termsOfServiceUrl("https://swagger.io/docs/specification/api-general-info/")
    			.version("1.0")
    			.build();
    }
    
    private Contact contact() {
  		return new Contact(name, url, email);
      }
    
    private List<Parameter> operationParameters(){
    	
    	List<Parameter> headers = new ArrayList<>();
    	
    	headers.add(new ParameterBuilder()
    			.name("Content-Type")
    			.modelRef(new ModelRef("string"))
    			.parameterType("header")
    			.required(true)
    			.defaultValue("application/json")
    			.build());
    	
    	return headers;
    }
  
}

	
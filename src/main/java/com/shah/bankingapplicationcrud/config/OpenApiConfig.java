package com.shah.bankingapplicationcrud.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@Configuration
@EnableWebMvc
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(OpenApiProperties properties) {
        return new OpenAPI()
                .info(getInfo(properties)
                        .contact(getContact()))
                .servers(List.of(getServer1(), getServer2()));
    }

    private Server getServer1() {
        return new Server()
                .description("localhost")
                .url("http://localhost:9090");
    }

    private Server getServer2() {
        return new Server()
                .description("uat")
                .url("http://localhost:8082");
    }

    private Contact getContact() {
        return new Contact()
                .url("https://www.infosys.com/")
                .name("Infosys")
                .email("norulshahlam.mohsen@infosys.com");
    }

    private Info getInfo(OpenApiProperties properties) {
        return new Info()
                .title(properties.getProjectTitle())
                .description(properties.getProjectDescription())
                .version(properties.getProjectVersion())
                .license(getLicense());
    }

    private License getLicense() {
        return new License()
                .name("Licensed under Infosys")
                .url("https://www.lawinsider.com/dictionary/infosys-technologies-agreement");
    }
}
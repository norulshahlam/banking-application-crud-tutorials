package com.shah.bankingapplicationcrud.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

import static com.shah.bankingapplicationcrud.constant.CommonConstants.*;

@Configuration
@EnableWebMvc
public class OpenApiConfig {

    public static final String HEADER = "header";

    @Bean
    public OpenAPI customOpenApi(SpringDocProperties properties) {
        return new OpenAPI()
                .info(getInfo(properties)
                        .contact(getContact(properties))
                        .license(getLicense(properties))
                        )
                .servers(List.of(getServer1(), getServer2()));
    }

    /**
     * Required header input. This will automatically inject header values into your request.
     * @return
     */
    @Bean
    public OperationCustomizer customize() {

        Parameter header1 = new Parameter()
                .in(HEADER)
                .required(true)
                .description("UUID value for tracing and debugging")
                .example("35b79a56-57ce-4187-a4cc-d423895d7440")
                .name(X_CORRELATION_ID);
        Parameter header2 = new Parameter()
                .in(HEADER)
                .required(true)
                .description("Source of country request coming from")
                .example("SG")
                .name(X_SOURCE_COUNTRY);
        Parameter header3 = new Parameter()
                .in(HEADER)
                .required(true)
                .description("Date time")
                .example("1702004334")
                .name(X_SOURCE_DATE_TIME);

        return (operation, handlerMethod) -> operation
                .addParametersItem(header1)
                .addParametersItem(header2)
                .addParametersItem(header3);
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

    private Contact getContact(SpringDocProperties properties) {
        return new Contact()
                .url(properties.getUrl())
                .name(properties.getName())
                .email(properties.getEmail());
    }

    private Info getInfo(SpringDocProperties properties) {
        return new Info()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .version(properties.getVersion())
                .license(getLicense(properties));
    }

    private License getLicense(SpringDocProperties properties) {
        return new License()
                .name(properties.getLicenseName())
                .url(properties.getLicenseUrl());
    }
}
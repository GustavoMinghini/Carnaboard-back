//package com.carnaboard.Carnaboard.config;
//
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.servers.Server;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//
//import java.util.List;
//
//@Configuration
//public class SwaggerConfig {
//
//    @Value("${spring.profiles.active}")
//    private String activeProfile;
//
//    @Bean
//    public OpenAPI customOpenAPI() {
//        Server server = new Server();
//
//        if ("prod".equals(activeProfile)) {
//            server.setUrl("https://carnaboard.achimid.com.br");
//            server.setDescription("Produção");
//        } else {
//            server.setUrl("http://localhost:8080");
//            server.setDescription("Desenvolvimento");
//        }
//
//        return new OpenAPI().servers(List.of(server));
//    }
//}

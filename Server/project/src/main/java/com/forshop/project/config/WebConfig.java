package com.forshop.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 적용할 경로 지정
            .allowedOrigins("http://192.168.137.1")  // 허용할 도메인 설정
            .allowedMethods("GET", "POST", "PUT", "DELETE")  // 허용할 HTTP 메서드 설정
            .allowedHeaders("*") // 허용할 요청 헤더 설정
            .allowCredentials(true);  // 인증 정보 허용
    }
}
package com.hyk.shoppingstreet.config;

import com.hyk.shoppingstreet.interceptor.UserAuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry
            .addInterceptor(authInterceptor())
            .addPathPatterns("/**")
            .excludePathPatterns(
                "/**.html",
                "/**.js",
                "/**.css",
                "/**.jpg",
                "/**.gif",
                "/**.png",

                "/error",
                "/user/register",
                "/user/login",
                "/user/loginStatusCheck",
                "/commodity/*"
            )
        ;
    }

    @Bean
    public UserAuthInterceptor authInterceptor() {
        return new UserAuthInterceptor();
    }
}
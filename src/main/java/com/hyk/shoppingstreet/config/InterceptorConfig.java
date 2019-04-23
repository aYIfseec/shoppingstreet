//package com.hyk.shoppingstreet.config;
//
//import com.hyk.shoppingstreet.interceptor.AuthInterceptor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class InterceptorConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//
//        registry
//            .addInterceptor(authInterceptor())
//            .addPathPatterns("/**")
//            .excludePathPatterns(
//                "/error",
//                "/user/register",
//                "/user/login",
//                "/user/loginStatusCheck",
//                "/poetry/*"
//            )
//        ;
//    }
//    /*
//    "/poetry/getById",
//                "/poetry/hot",
//                "/poetry/getByAuthor",
//                "/poetry/search",
//                "/poetry/searchByTitle",
//                "/poetry/searchByAuthor",
//                "/poetry/searchByType",
//                "/poetry/searchByDynasty",
//                "/poetry/getByDynasty"
//     */
//
//    @Bean
//    public AuthInterceptor authInterceptor() {
//        return new AuthInterceptor();
//    }
//}
//package com.hyk.shoppingstreet.config;
//
//import com.google.common.collect.Lists;
//import com.hyk.shoppingstreet.common.repository.mongo.MongoConfig;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class MongoConfigs {
//
//    @Value("${mongo.poetry.dbName}")
//    private String dbName;
//
//    @Value("${mongo.poetry.password}")
//    private String password;
//
//    @Value("${mongo.poetry.userName}")
//    private String userName;
//
//    @Value("${mongo.poetry.hosts}")
//    private String[] hosts;
//
//    @Bean
//    public MongoConfig mongoConfig() {
//        return MongoConfig.builder()
//                .dbName(dbName)
//                .password(password)
//                .userName(userName)
//                .hosts(hosts)
//                .mapPackages(Lists.newArrayList("com.hyk.shoppingstreet.dataobject")).build();
//    }
//}

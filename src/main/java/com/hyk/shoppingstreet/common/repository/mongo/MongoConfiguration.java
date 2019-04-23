package com.hyk.shoppingstreet.common.repository.mongo;


import com.mongodb.MongoClient;
import javax.annotation.Resource;
import org.mongodb.morphia.Datastore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfiguration {

    @Resource
    private MongoDatastoreRegistry mongoDatastoreRegistry;

    @Bean
    public Datastore mongoDataStore() {
        return mongoDatastoreRegistry.getDatastore("default");
    }

    @Bean
    public MongoClient mongoClient() {
        return mongoDatastoreRegistry.getClient("default");
    }
}

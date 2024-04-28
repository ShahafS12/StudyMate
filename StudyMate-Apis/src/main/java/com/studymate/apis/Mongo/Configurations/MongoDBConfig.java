package com.studymate.apis.Mongo.Configurations;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;



import java.util.Collections;

@Configuration
public class MongoDBConfig extends AbstractMongoClientConfiguration{
    @Bean
    public MongoTemplate mongoTemplate() {
        ServerAddress serverAddress = new ServerAddress("localhost", 27017);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyToClusterSettings(builder -> builder.hosts(Collections.singletonList(serverAddress)))
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        return new MongoTemplate(mongoClient, "users");
    }

    @Override
    protected String getDatabaseName() {
        return "studymate";
    }
}
package com.studymate.configurations;
import com.mongodb.MongoCredential;
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
public class MongoDBConfig extends AbstractMongoClientConfiguration {
    @Bean
    public MongoTemplate mongoTemplate() {
        String username = System.getenv("MONGO_INITDB_ROOT_USERNAME");
        String password = System.getenv("MONGO_INITDB_ROOT_PASSWORD");
        String host = System.getenv("MONGO_HOST");
        String port = System.getenv("MONGO_PORT");

        if (username == null || password == null) {
            username = "root";
            password = "password";
        }

        if (host == null) {
            host = "localhost";
        }

        if (port == null) {
            port = "27017";
        }

        ServerAddress serverAddress = new ServerAddress(host, Integer.parseInt(port));
        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(MongoCredential.createCredential(username, getDatabaseName(), password.toCharArray()))
                .applyToClusterSettings(builder -> builder.hosts(Collections.singletonList(serverAddress)))
                .build();

        MongoClient mongoClient = MongoClients.create(settings);
        return new MongoTemplate(mongoClient, getDatabaseName());
    }

    @Override
    protected String getDatabaseName() {
        return "studymate";
    }
}
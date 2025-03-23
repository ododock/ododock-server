package ododock.webserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoAuditing
@Configuration
@EnableReactiveMongoRepositories(basePackages = "ododock.webserver.repository.reactive")
public class MongoConfig {

}

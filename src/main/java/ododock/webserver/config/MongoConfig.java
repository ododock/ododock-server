package ododock.webserver.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.index.IndexDefinition;
import org.springframework.data.mongodb.core.index.ReactiveIndexOperations;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Slf4j
@EnableReactiveMongoAuditing
@Configuration
@EnableReactiveMongoRepositories(basePackages = "ododock.webserver.repository.reactive")
public class MongoConfig {

    private final ReactiveMongoTemplate mongoTemplate;

    public MongoConfig(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void initIndexes() {
        Document indexDef = new Document();
        indexDef.put("title", "text");
        indexDef.put("plainText", "text");
        indexDef.put("tags.name", "text");

        Document fullIndex = new Document("key", indexDef)
                .append("default_language", "none")
                .append("name", "article_text_index");

        IndexDefinition indexDefinition = new IndexDefinition() {
            @Override
            public Document getIndexKeys() {
                return fullIndex.get("key", Document.class);
            }

            @Override
            public Document getIndexOptions() {
                Document options = new Document(fullIndex);
                options.remove("key");
                return options;
            }
        };

        ReactiveIndexOperations indexOps = mongoTemplate.indexOps("article");

        indexOps.ensureIndex(indexDefinition)
                .doOnNext(result -> log.info("Index creation result: {}", result))
                .subscribe();
    }

}

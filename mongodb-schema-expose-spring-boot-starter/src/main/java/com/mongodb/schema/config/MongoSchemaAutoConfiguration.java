package com.mongodb.schema.config;

import com.mongodb.schema.controller.CollectionSchemaController;
import com.mongodb.schema.registry.CollectionSchemaRegistry;
import com.mongodb.schema.service.SchemaReflectionService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Spring Boot auto-configuration for the MongoDB Schema Expose library.
 *
 * <p>Automatically creates the registry, reflection service, and REST controller
 * beans when the starter is on the classpath.</p>
 *
 * <p>Consumer APIs can inject {@link CollectionSchemaRegistry} and register
 * their document classes at startup.</p>
 */
@AutoConfiguration
@ConditionalOnClass(DispatcherServlet.class)
public class MongoSchemaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CollectionSchemaRegistry collectionSchemaRegistry() {
        return new CollectionSchemaRegistry();
    }

    @Bean
    @ConditionalOnMissingBean
    public SchemaReflectionService schemaReflectionService(CollectionSchemaRegistry registry) {
        return new SchemaReflectionService(registry);
    }

    @Bean
    @ConditionalOnMissingBean
    public CollectionSchemaController collectionSchemaController(SchemaReflectionService service) {
        return new CollectionSchemaController(service);
    }
}

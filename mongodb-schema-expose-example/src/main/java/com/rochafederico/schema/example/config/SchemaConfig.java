package com.rochafederico.schema.example.config;

import com.rochafederico.schema.example.document.ProductDocument;
import com.rochafederico.schema.example.document.UserDocument;
import com.rochafederico.schema.registry.CollectionSchemaRegistry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class that registers MongoDB document classes
 * with the schema registry at application startup.
 *
 * <p>Each API that uses the starter library would have a similar
 * configuration class registering its own document models.</p>
 */
@Configuration
public class SchemaConfig {

    @Bean
    public CommandLineRunner registerSchemas(CollectionSchemaRegistry registry) {
        return args -> {
            registry.register("users", UserDocument.class);
            registry.register("products", ProductDocument.class);
        };
    }
}

package com.rochafederico.schema.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

/**
 * Example Spring Boot application that demonstrates how to use
 * the MongoDB Schema Expose starter library.
 *
 * <p>Note: MongoDB auto-configuration is excluded since this example
 * only demonstrates the schema exposition, not actual database connectivity.</p>
 */
@SpringBootApplication(exclude = {
        MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class
})
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }
}

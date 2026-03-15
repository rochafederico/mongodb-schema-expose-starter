package com.mongodb.schema.controller;

import com.mongodb.schema.exception.CollectionNotFoundException;
import com.mongodb.schema.model.CollectionSchema;
import com.mongodb.schema.service.SchemaReflectionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * REST controller that exposes MongoDB collection schemas via HTTP GET endpoints.
 *
 * <ul>
 *   <li>{@code GET /api/collections/schema} - list all registered collection schemas</li>
 *   <li>{@code GET /api/collections/{collectionName}/schema} - get schema for a specific collection</li>
 * </ul>
 */
@RestController
@RequestMapping("${mongodb.schema.base-path:/api/collections}")
public class CollectionSchemaController {

    private final SchemaReflectionService schemaReflectionService;

    public CollectionSchemaController(SchemaReflectionService schemaReflectionService) {
        this.schemaReflectionService = schemaReflectionService;
    }

    /**
     * Get schemas for all registered collections.
     */
    @GetMapping("/schema")
    public ResponseEntity<List<CollectionSchema>> getAllSchemas() {
        List<CollectionSchema> schemas = schemaReflectionService.getAllSchemas();
        return ResponseEntity.ok(schemas);
    }

    /**
     * Get the schema for a specific collection by name.
     */
    @GetMapping("/{collectionName}/schema")
    public ResponseEntity<CollectionSchema> getSchema(@PathVariable("collectionName") String collectionName) {
        if (collectionName == null || collectionName.isBlank()
                || collectionName.length() > 128
                || !collectionName.matches("^[a-zA-Z0-9_\\-]+$")) {
            return ResponseEntity.badRequest().build();
        }
        CollectionSchema schema = schemaReflectionService.getSchema(collectionName);
        return ResponseEntity.ok(schema);
    }

    /**
     * Handle cases where the requested collection is not registered.
     */
    @ExceptionHandler(CollectionNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCollectionNotFound(CollectionNotFoundException ex) {
        Map<String, Object> body = Map.of(
                "error", "COLLECTION_NOT_FOUND",
                "message", ex.getMessage(),
                "collection", ex.getCollectionName()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}

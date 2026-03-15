package com.mongodb.schema.service;

import com.mongodb.schema.annotation.SchemaField;
import com.mongodb.schema.exception.CollectionNotFoundException;
import com.mongodb.schema.model.CollectionSchema;
import com.mongodb.schema.model.FieldDefinition;
import com.mongodb.schema.registry.CollectionSchemaRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SchemaReflectionServiceTest {

    private CollectionSchemaRegistry registry;
    private SchemaReflectionService service;

    @BeforeEach
    void setUp() {
        registry = new CollectionSchemaRegistry();
        service = new SchemaReflectionService(registry);
    }

    @Test
    void shouldExtractBasicFieldsFromSimpleDocument() {
        registry.register("simple", SimpleDoc.class);
        CollectionSchema schema = service.getSchema("simple");

        assertEquals("simple", schema.getCollection());
        assertNotNull(schema.getFields());
        assertFalse(schema.getFields().isEmpty());

        FieldDefinition nameField = findField(schema.getFields(), "name");
        assertNotNull(nameField);
        assertEquals("Name", nameField.getLabel());
        assertEquals("String", nameField.getType());

        FieldDefinition ageField = findField(schema.getFields(), "age");
        assertNotNull(ageField);
        assertEquals("Age", ageField.getLabel());
        assertEquals("Integer", ageField.getType());
    }

    @Test
    void shouldUseCustomLabelFromAnnotation() {
        registry.register("annotated", AnnotatedDoc.class);
        CollectionSchema schema = service.getSchema("annotated");

        FieldDefinition field = findField(schema.getFields(), "firstName");
        assertNotNull(field);
        assertEquals("Given Name", field.getLabel());
    }

    @Test
    void shouldExcludeFieldsMarkedAsExcluded() {
        registry.register("annotated", AnnotatedDoc.class);
        CollectionSchema schema = service.getSchema("annotated");

        FieldDefinition excluded = findField(schema.getFields(), "secret");
        assertNull(excluded);
    }

    @Test
    void shouldMapCollectionTypes() {
        registry.register("collections", CollectionTypesDoc.class);
        CollectionSchema schema = service.getSchema("collections");

        FieldDefinition listField = findField(schema.getFields(), "tags");
        assertNotNull(listField);
        assertEquals("Array<String>", listField.getType());

        FieldDefinition mapField = findField(schema.getFields(), "metadata");
        assertNotNull(mapField);
        assertTrue(mapField.getType().startsWith("Map<"));
    }

    @Test
    void shouldMapDateTypes() {
        registry.register("dates", DateTypesDoc.class);
        CollectionSchema schema = service.getSchema("dates");

        FieldDefinition dateField = findField(schema.getFields(), "createdAt");
        assertNotNull(dateField);
        assertEquals("Date", dateField.getType());
    }

    @Test
    void shouldThrowWhenCollectionNotRegistered() {
        assertThrows(CollectionNotFoundException.class, () -> service.getSchema("nonexistent"));
    }

    @Test
    void shouldReturnAllSchemas() {
        registry.register("col1", SimpleDoc.class);
        registry.register("col2", AnnotatedDoc.class);

        List<CollectionSchema> schemas = service.getAllSchemas();
        assertEquals(2, schemas.size());
    }

    @Test
    void shouldHumanizeCamelCaseFieldNames() {
        registry.register("camel", CamelCaseDoc.class);
        CollectionSchema schema = service.getSchema("camel");

        FieldDefinition field = findField(schema.getFields(), "firstName");
        assertNotNull(field);
        assertEquals("First Name", field.getLabel());
    }

    @Test
    void shouldMapBooleanType() {
        registry.register("simple", SimpleDoc.class);
        CollectionSchema schema = service.getSchema("simple");

        FieldDefinition field = findField(schema.getFields(), "active");
        assertNotNull(field);
        assertEquals("Boolean", field.getType());
    }

    @Test
    void shouldMapIdAnnotatedFieldToUnderscoreId() {
        registry.register("withId", IdDoc.class);
        CollectionSchema schema = service.getSchema("withId");

        FieldDefinition idField = findField(schema.getFields(), "_id");
        assertNotNull(idField, "Field with @Id should have key '_id'");
        assertEquals("String", idField.getType());

        // Ensure original field name 'id' is not present as key
        FieldDefinition rawIdField = findField(schema.getFields(), "id");
        assertNull(rawIdField, "Original field name 'id' should not appear when @Id is present");
    }

    // --- Test document classes ---

    static class SimpleDoc {
        private String name;
        private int age;
        private boolean active;
    }

    static class IdDoc {
        @Id
        private String id;
        private String name;
    }

    static class AnnotatedDoc {
        @SchemaField(label = "Given Name")
        private String firstName;

        @SchemaField(excluded = true)
        private String secret;

        private String lastName;
    }

    static class CollectionTypesDoc {
        private List<String> tags;
        private Map<String, Object> metadata;
    }

    static class DateTypesDoc {
        private LocalDateTime createdAt;
    }

    static class CamelCaseDoc {
        private String firstName;
        private String lastName;
    }

    private FieldDefinition findField(List<FieldDefinition> fields, String key) {
        return fields.stream()
                .filter(f -> f.getKey().equals(key))
                .findFirst()
                .orElse(null);
    }
}

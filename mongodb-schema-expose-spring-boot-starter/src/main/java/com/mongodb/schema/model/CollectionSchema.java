package com.mongodb.schema.model;

import java.util.List;

/**
 * Represents the full schema of a MongoDB collection,
 * including its name and all field definitions.
 */
public class CollectionSchema {

    private String collection;
    private List<FieldDefinition> fields;

    public CollectionSchema() {
    }

    public CollectionSchema(String collection, List<FieldDefinition> fields) {
        this.collection = collection;
        this.fields = fields;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public List<FieldDefinition> getFields() {
        return fields;
    }

    public void setFields(List<FieldDefinition> fields) {
        this.fields = fields;
    }
}

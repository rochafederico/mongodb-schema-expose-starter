package com.rochafederico.schema.model;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollectionSchema that = (CollectionSchema) o;
        return Objects.equals(collection, that.collection)
                && Objects.equals(fields, that.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collection, fields);
    }

    @Override
    public String toString() {
        return "CollectionSchema{" +
                "collection='" + collection + '\'' +
                ", fields=" + fields +
                '}';
    }
}

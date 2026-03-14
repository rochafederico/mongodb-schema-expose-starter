package com.mongodb.schema.model;

/**
 * Represents a single field definition within a MongoDB collection schema.
 */
public class FieldDefinition {

    private String key;
    private String label;
    private String type;

    public FieldDefinition() {
    }

    public FieldDefinition(String key, String label, String type) {
        this.key = key;
        this.label = label;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

package com.rochafederico.schema.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldDefinition that = (FieldDefinition) o;
        return Objects.equals(key, that.key)
                && Objects.equals(label, that.label)
                && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, label, type);
    }

    @Override
    public String toString() {
        return "FieldDefinition{" +
                "key='" + key + '\'' +
                ", label='" + label + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

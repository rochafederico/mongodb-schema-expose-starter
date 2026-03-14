package com.mongodb.schema.exception;

/**
 * Exception thrown when a requested collection name is not registered.
 */
public class CollectionNotFoundException extends RuntimeException {

    private final String collectionName;

    public CollectionNotFoundException(String collectionName) {
        super("Collection not found: " + collectionName);
        this.collectionName = collectionName;
    }

    public String getCollectionName() {
        return collectionName;
    }
}

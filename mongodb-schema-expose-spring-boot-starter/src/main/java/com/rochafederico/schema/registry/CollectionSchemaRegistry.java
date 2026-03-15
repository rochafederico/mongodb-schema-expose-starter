package com.rochafederico.schema.registry;

import com.rochafederico.schema.exception.CollectionNotFoundException;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry that maps MongoDB collection names to their corresponding
 * Java document classes. APIs register their document classes here
 * so the library can resolve schemas at runtime.
 */
public class CollectionSchemaRegistry {

    private final Map<String, Class<?>> registry = new ConcurrentHashMap<>();

    /**
     * Register a document class for a given collection name.
     *
     * @param collectionName the MongoDB collection name
     * @param documentClass  the Java class annotated as a MongoDB document
     */
    public void register(String collectionName, Class<?> documentClass) {
        registry.put(collectionName, documentClass);
    }

    /**
     * Retrieve the document class for a given collection name.
     *
     * @param collectionName the MongoDB collection name
     * @return the registered document class
     * @throws CollectionNotFoundException if the collection is not registered
     */
    public Class<?> getDocumentClass(String collectionName) {
        Class<?> clazz = registry.get(collectionName);
        if (clazz == null) {
            throw new CollectionNotFoundException(collectionName);
        }
        return clazz;
    }

    /**
     * @return an unmodifiable set of all registered collection names
     */
    public Set<String> getRegisteredCollections() {
        return Collections.unmodifiableSet(registry.keySet());
    }

    /**
     * @return an unmodifiable view of the full registry
     */
    public Map<String, Class<?>> getAll() {
        return Collections.unmodifiableMap(registry);
    }

    /**
     * Check if a collection name is registered.
     *
     * @param collectionName the collection name to check
     * @return true if registered
     */
    public boolean contains(String collectionName) {
        return registry.containsKey(collectionName);
    }
}

package com.rochafederico.schema.registry;

import com.rochafederico.schema.exception.CollectionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CollectionSchemaRegistryTest {

    private CollectionSchemaRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new CollectionSchemaRegistry();
    }

    @Test
    void shouldRegisterAndRetrieveDocumentClass() {
        registry.register("users", String.class);
        assertEquals(String.class, registry.getDocumentClass("users"));
    }

    @Test
    void shouldThrowWhenCollectionNotFound() {
        assertThrows(CollectionNotFoundException.class, () -> registry.getDocumentClass("nonexistent"));
    }

    @Test
    void shouldReturnRegisteredCollectionNames() {
        registry.register("users", String.class);
        registry.register("products", Integer.class);

        assertTrue(registry.getRegisteredCollections().contains("users"));
        assertTrue(registry.getRegisteredCollections().contains("products"));
        assertEquals(2, registry.getRegisteredCollections().size());
    }

    @Test
    void shouldCheckIfCollectionIsRegistered() {
        registry.register("users", String.class);

        assertTrue(registry.contains("users"));
        assertFalse(registry.contains("orders"));
    }
}

package com.mongodb.schema.controller;

import com.mongodb.schema.exception.CollectionNotFoundException;
import com.mongodb.schema.model.CollectionSchema;
import com.mongodb.schema.model.FieldDefinition;
import com.mongodb.schema.service.SchemaReflectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CollectionSchemaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SchemaReflectionService schemaReflectionService;

    @InjectMocks
    private CollectionSchemaController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void shouldReturnAllSchemas() throws Exception {
        List<FieldDefinition> fields = List.of(
                new FieldDefinition("name", "Name", "String")
        );
        CollectionSchema schema = new CollectionSchema("users", fields);
        when(schemaReflectionService.getAllSchemas()).thenReturn(List.of(schema));

        mockMvc.perform(get("/api/collections/schema"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].collection", is("users")))
                .andExpect(jsonPath("$[0].fields[0].key", is("name")));
    }

    @Test
    void shouldReturnSchemaForSpecificCollection() throws Exception {
        List<FieldDefinition> fields = List.of(
                new FieldDefinition("_id", "Identifier", "String"),
                new FieldDefinition("name", "Name", "String")
        );
        CollectionSchema schema = new CollectionSchema("users", fields);
        when(schemaReflectionService.getSchema("users")).thenReturn(schema);

        mockMvc.perform(get("/api/collections/users/schema"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.collection", is("users")))
                .andExpect(jsonPath("$.fields", hasSize(2)))
                .andExpect(jsonPath("$.fields[0].key", is("_id")));
    }

    @Test
    void shouldReturn404WhenCollectionNotFound() throws Exception {
        when(schemaReflectionService.getSchema("nonexistent"))
                .thenThrow(new CollectionNotFoundException("nonexistent"));

        mockMvc.perform(get("/api/collections/nonexistent/schema"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("COLLECTION_NOT_FOUND")))
                .andExpect(jsonPath("$.collection", is("nonexistent")));
    }

    @Test
    void shouldReturn400ForCollectionNameWithSpaces() throws Exception {
        mockMvc.perform(get("/api/collections/col name/schema"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400ForCollectionNameWithDots() throws Exception {
        mockMvc.perform(get("/api/collections/col.name/schema"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldAcceptValidCollectionNames() throws Exception {
        List<FieldDefinition> fields = List.of(
                new FieldDefinition("name", "Name", "String")
        );
        CollectionSchema schema = new CollectionSchema("my-collection_01", fields);
        when(schemaReflectionService.getSchema("my-collection_01")).thenReturn(schema);

        mockMvc.perform(get("/api/collections/my-collection_01/schema"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.collection", is("my-collection_01")));
    }
}

package com.rochafederico.schema.example.document;

import com.rochafederico.schema.annotation.SchemaField;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Example MongoDB document representing a Product.
 */
@Document(collection = "products")
public class ProductDocument {

    @Id
    @SchemaField(label = "Identifier")
    private String id;

    @SchemaField(label = "Product Name")
    private String name;

    @SchemaField(label = "Description")
    private String description;

    @SchemaField(label = "Price")
    private BigDecimal price;

    @SchemaField(label = "Stock Quantity")
    private int stockQuantity;

    @SchemaField(label = "Categories")
    private List<String> categories;

    @SchemaField(label = "Attributes")
    private Map<String, String> attributes;

    @SchemaField(label = "Available")
    private boolean available;

    @SchemaField(label = "Created At")
    private LocalDateTime createdAt;
}

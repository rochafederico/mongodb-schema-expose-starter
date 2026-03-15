package com.rochafederico.schema.example.document;

import com.rochafederico.schema.annotation.SchemaField;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Example MongoDB document representing a User.
 */
@Document(collection = "users")
public class UserDocument {

    @Id
    @SchemaField(label = "Identifier")
    private String id;

    @SchemaField(label = "First Name")
    private String firstName;

    @SchemaField(label = "Last Name")
    private String lastName;

    @Field("email_address")
    @SchemaField(label = "Email Address")
    private String email;

    private int age;

    private boolean active;

    @SchemaField(label = "Roles")
    private List<String> roles;

    @SchemaField(label = "Created At")
    private LocalDateTime createdAt;

    @SchemaField(excluded = true)
    private String internalField;
}

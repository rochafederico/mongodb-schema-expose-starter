package com.rochafederico.schema.service;

import com.rochafederico.schema.annotation.SchemaField;
import com.rochafederico.schema.model.CollectionSchema;
import com.rochafederico.schema.model.FieldDefinition;
import com.rochafederico.schema.registry.CollectionSchemaRegistry;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Service that uses Java reflection to extract field metadata
 * from MongoDB document classes registered in the {@link CollectionSchemaRegistry}.
 */
public class SchemaReflectionService {

    private final CollectionSchemaRegistry registry;

    public SchemaReflectionService(CollectionSchemaRegistry registry) {
        this.registry = registry;
    }

    /**
     * Build the schema for the given collection name by reflecting
     * on the registered document class.
     *
     * @param collectionName the MongoDB collection name
     * @return the collection schema with all field definitions
     */
    public CollectionSchema getSchema(String collectionName) {
        Class<?> documentClass = registry.getDocumentClass(collectionName);
        List<FieldDefinition> fields = extractFields(documentClass);
        return new CollectionSchema(collectionName, fields);
    }

    /**
     * Build schemas for all registered collections.
     *
     * @return list of all collection schemas
     */
    public List<CollectionSchema> getAllSchemas() {
        List<CollectionSchema> schemas = new ArrayList<>();
        for (String collectionName : registry.getRegisteredCollections()) {
            schemas.add(getSchema(collectionName));
        }
        return schemas;
    }

    private List<FieldDefinition> extractFields(Class<?> clazz) {
        List<FieldDefinition> definitions = new ArrayList<>();
        collectFields(clazz, definitions);
        return definitions;
    }

    private void collectFields(Class<?> clazz, List<FieldDefinition> definitions) {
        if (clazz == null || clazz == Object.class) {
            return;
        }

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isSynthetic()) {
                continue;
            }

            SchemaField schemaAnnotation = field.getAnnotation(SchemaField.class);

            if (schemaAnnotation != null && schemaAnnotation.excluded()) {
                continue;
            }

            String key = resolveKey(field);
            String label = resolveLabel(field, schemaAnnotation);
            String type = resolveType(field, schemaAnnotation);

            definitions.add(new FieldDefinition(key, label, type));
        }

        collectFields(clazz.getSuperclass(), definitions);
    }

    /**
     * Resolve the key for a field. Uses the MongoDB @Field annotation value
     * if present, otherwise defaults to the Java field name.
     */
    private String resolveKey(Field field) {
        // Check for @Id annotation (Spring Data) — maps to "_id" in MongoDB
        try {
            org.springframework.data.annotation.Id idAnnotation =
                    field.getAnnotation(org.springframework.data.annotation.Id.class);
            if (idAnnotation != null) {
                return "_id";
            }
        } catch (NoClassDefFoundError ignored) {
            // spring-data-commons not on classpath
        }

        // Check for @Field annotation (Spring Data MongoDB)
        try {
            org.springframework.data.mongodb.core.mapping.Field mongoField =
                    field.getAnnotation(org.springframework.data.mongodb.core.mapping.Field.class);
            if (mongoField != null && !mongoField.value().isEmpty()) {
                return mongoField.value();
            }
        } catch (NoClassDefFoundError ignored) {
            // spring-data-mongodb not on classpath
        }
        return field.getName();
    }

    /**
     * Resolve the human-readable label for a field.
     */
    private String resolveLabel(Field field, SchemaField schemaAnnotation) {
        if (schemaAnnotation != null && !schemaAnnotation.label().isEmpty()) {
            return schemaAnnotation.label();
        }
        return humanize(field.getName());
    }

    /**
     * Resolve the type description for a field.
     */
    private String resolveType(Field field, SchemaField schemaAnnotation) {
        if (schemaAnnotation != null && !schemaAnnotation.type().isEmpty()) {
            return schemaAnnotation.type();
        }
        return mapJavaType(field);
    }

    /**
     * Map a Java field type to a simplified, MongoDB-friendly type name.
     */
    private String mapJavaType(Field field) {
        Class<?> fieldType = field.getType();

        if (fieldType == String.class) {
            return "String";
        }
        if (fieldType == int.class || fieldType == Integer.class) {
            return "Integer";
        }
        if (fieldType == long.class || fieldType == Long.class) {
            return "Long";
        }
        if (fieldType == double.class || fieldType == Double.class) {
            return "Double";
        }
        if (fieldType == float.class || fieldType == Float.class) {
            return "Float";
        }
        if (fieldType == boolean.class || fieldType == Boolean.class) {
            return "Boolean";
        }
        if (fieldType == java.math.BigDecimal.class) {
            return "Decimal";
        }
        if (fieldType == java.util.Date.class
                || fieldType == java.time.LocalDate.class
                || fieldType == java.time.LocalDateTime.class
                || fieldType == java.time.Instant.class) {
            return "Date";
        }
        if (Collection.class.isAssignableFrom(fieldType)) {
            String elementType = resolveGenericType(field.getGenericType(), 0);
            return "Array<" + elementType + ">";
        }
        if (Map.class.isAssignableFrom(fieldType)) {
            String keyType = resolveGenericType(field.getGenericType(), 0);
            String valType = resolveGenericType(field.getGenericType(), 1);
            return "Map<" + keyType + ", " + valType + ">";
        }
        if (fieldType.isEnum()) {
            return "String";
        }

        return "Object";
    }

    private String resolveGenericType(Type genericType, int index) {
        if (genericType instanceof ParameterizedType parameterizedType) {
            Type[] typeArgs = parameterizedType.getActualTypeArguments();
            if (typeArgs.length > index) {
                Type arg = typeArgs[index];
                if (arg instanceof Class<?> argClass) {
                    return argClass.getSimpleName();
                }
                return arg.getTypeName();
            }
        }
        return "Object";
    }

    /**
     * Convert a camelCase field name into a human-readable label.
     * e.g., "firstName" -> "First Name"
     */
    private String humanize(String fieldName) {
        if (fieldName == null || fieldName.isEmpty()) {
            return fieldName;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toUpperCase(fieldName.charAt(0)));
        for (int i = 1; i < fieldName.length(); i++) {
            char c = fieldName.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(' ');
            }
            sb.append(c);
        }
        return sb.toString();
    }
}

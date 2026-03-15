package com.rochafederico.schema.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Optional annotation to customize the label and type description
 * of a field when exposed in the collection schema.
 *
 * <p>If not present, the label is derived from the field name
 * and the type from the Java field type.</p>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SchemaField {

    /**
     * Custom label for the field. If empty, the label is derived from the field name.
     */
    String label() default "";

    /**
     * Custom type description. If empty, the type is inferred from the Java type.
     */
    String type() default "";

    /**
     * Whether to exclude this field from the schema output.
     */
    boolean excluded() default false;
}

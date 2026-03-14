# MongoDB Schema Expose - Spring Boot Starter

Libreria reusable para Spring Boot que permite exponer la definicion estructural de colecciones MongoDB a traves de endpoints HTTP GET.

## Caracteristicas

- Extrae automaticamente metadata de campos mediante reflexion sobre clases de documento Java
- Cada campo incluye: `key`, `label` y `type`
- Soporta anotacion `@SchemaField` para personalizar labels, tipos y excluir campos
- Resuelve el key del campo desde `@Field` de Spring Data MongoDB
- Endpoint REST configurable para consultar esquemas
- Auto-configuracion de Spring Boot (solo agregar la dependencia)
- Soporta una o multiples colecciones por API

## Instalacion

Agregar la dependencia en el `pom.xml` de la API:

```xml
<dependency>
    <groupId>com.mongodb.schema</groupId>
    <artifactId>mongodb-schema-expose-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Uso

### 1. Definir documentos MongoDB

```java
@Document(collection = "users")
public class UserDocument {

    @Id
    @SchemaField(label = "Identifier")
    private String id;

    @SchemaField(label = "First Name")
    private String firstName;

    @Field("email_address")
    @SchemaField(label = "Email Address")
    private String email;

    @SchemaField(excluded = true)
    private String internalField;
}
```

### 2. Registrar colecciones

Crear una clase `@Configuration` que registre los documentos:

```java
@Configuration
public class SchemaConfig {

    @Bean
    public CommandLineRunner registerSchemas(CollectionSchemaRegistry registry) {
        return args -> {
            registry.register("users", UserDocument.class);
            registry.register("products", ProductDocument.class);
        };
    }
}
```

### 3. Consultar el endpoint

```
GET /api/collections/users/schema
```

Respuesta:
```json
{
  "collection": "users",
  "fields": [
    { "key": "id", "label": "Identifier", "type": "String" },
    { "key": "firstName", "label": "First Name", "type": "String" },
    { "key": "email_address", "label": "Email Address", "type": "String" }
  ]
}
```

Listar todos los esquemas registrados:
```
GET /api/collections/schema
```

### 4. Manejo de errores

Si se consulta una coleccion no registrada:

```
GET /api/collections/orders/schema
```

Respuesta `404 Not Found`:
```json
{
  "error": "COLLECTION_NOT_FOUND",
  "message": "Collection not found: orders",
  "collection": "orders"
}
```

## Configuracion

| Propiedad | Descripcion | Default |
|---|---|---|
| `mongodb.schema.base-path` | Path base para los endpoints | `/api/collections` |

## Anotacion @SchemaField

| Atributo | Descripcion | Default |
|---|---|---|
| `label` | Label personalizado del campo | Nombre humanizado del campo Java |
| `type` | Tipo personalizado | Inferido del tipo Java |
| `excluded` | Excluir campo del esquema | `false` |

## Tipos soportados

| Java Type | Schema Type |
|---|---|
| String | String |
| int / Integer | Integer |
| long / Long | Long |
| double / Double | Double |
| float / Float | Float |
| boolean / Boolean | Boolean |
| BigDecimal | Decimal |
| Date, LocalDate, LocalDateTime, Instant | Date |
| List\<T\>, Set\<T\> | Array\<T\> |
| Map\<K,V\> | Map\<K, V\> |
| Enum | String |
| Otros | Object |

## Build

```bash
mvn clean install
```

## Ejemplo

El modulo `mongodb-schema-expose-example` contiene una aplicacion de ejemplo completa.

```bash
cd mongodb-schema-expose-example
mvn spring-boot:run
```

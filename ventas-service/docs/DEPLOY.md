# Guía de Deploy - Ventas Service

## 🚀 Plataformas de Despliegue

### 1. Railway (Recomendado)

#### Configuración
1. Crear nuevo servicio en Railway
2. Conectar repositorio de GitHub
3. Configurar variables de entorno
4. Railway detectará automáticamente Maven/Spring Boot

#### Variables de Entorno (Railway Dashboard)
```
JWT_SECRET=MismaClavQueAuthService32CaracteresMinimo
SPRING_PROFILES_ACTIVE=prod
```

> ⚠️ **IMPORTANTE**: `JWT_SECRET` debe ser IDÉNTICO al de auth-service

### 2. Render

#### `render.yaml`
```yaml
services:
  - type: web
    name: ventas-service
    env: java
    buildCommand: ./mvnw clean package -DskipTests
    startCommand: java -jar target/ventas-service-1.0.0.jar
    envVars:
      - key: JWT_SECRET
        sync: false
      - key: SPRING_PROFILES_ACTIVE
        value: prod
```

### 3. Heroku

#### `Procfile`
```
web: java -jar target/ventas-service-1.0.0.jar --server.port=$PORT
```

#### Deploy
```bash
heroku login
heroku create ventas-service-tienda
heroku config:set JWT_SECRET=misma-clave-que-auth-service
heroku config:set SPRING_PROFILES_ACTIVE=prod
git push heroku main
```

### 4. Docker

#### `Dockerfile`
```dockerfile
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline
COPY src src
RUN ./mvnw package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/ventas-service-1.0.0.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### Docker Compose (ambos servicios)
```yaml
version: '3.8'
services:
  auth-service:
    build: ./auth-service
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - JWT_SECRET=${JWT_SECRET}
      - SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/tienda
    depends_on:
      - db
  
  ventas-service:
    build: ./ventas-service
    ports:
      - "8081:8081"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - JWT_SECRET=${JWT_SECRET}
      - SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/tienda
      - SERVER_PORT=8081
    depends_on:
      - db
  
  db:
    image: mysql:8
    environment:
      - MYSQL_ROOT_PASSWORD=secret
      - MYSQL_DATABASE=tienda
    volumes:
      - mysql_data:/var/lib/mysql

volumes:
  mysql_data:
```

## 📦 Build Local

### Comandos Maven
```bash
# Compilar
./mvnw clean compile

# Ejecutar tests
./mvnw test

# Empaquetar JAR
./mvnw package -DskipTests

# Ejecutar aplicación (puerto 8081 para evitar conflicto)
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081

# Ejecutar JAR
java -jar target/ventas-service-1.0.0.jar --server.port=8081
```

## ⚙️ Configuración de Producción

### `application-prod.properties`
```properties
# Base de datos
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USER}
spring.datasource.password=${DATABASE_PASSWORD}

# Puerto
server.port=${PORT:8081}

# JWT (DEBE ser el mismo que auth-service)
jwt.secret=${JWT_SECRET}

# Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
```

## 🔐 Variables de Entorno Requeridas

| Variable | Descripción | Nota |
|----------|-------------|------|
| `JWT_SECRET` | Clave JWT | ⚠️ IGUAL que auth-service |
| `DATABASE_URL` | URL MySQL | Misma BD que auth-service |
| `DATABASE_USER` | Usuario BD | |
| `DATABASE_PASSWORD` | Contraseña BD | |
| `PORT` | Puerto servidor | Default: 8081 |

## 📋 Checklist Pre-Deploy

- [ ] JWT_SECRET idéntico al de auth-service
- [ ] Base de datos MySQL accesible
- [ ] Profile `prod` activado
- [ ] Tests pasando
- [ ] Puerto diferente a auth-service (8081)
- [ ] CORS configurado para frontend

## 🐛 Troubleshooting

### Error: JWT validation failed
- Verificar que `JWT_SECRET` sea EXACTAMENTE igual en ambos servicios

### Error: Port 8080 already in use
- Usar puerto diferente: `--server.port=8081`

### Error: Table doesn't exist
- Hibernate con `ddl-auto=update` creará las tablas automáticamente
- Verificar conexión a BD

### Error: Foreign key constraint
- Asegurar que las categorías existan antes de crear productos

## 📊 Monitoreo

### Health Check Endpoint
```
GET /actuator/health
```

### Swagger UI (solo desarrollo)
```
http://localhost:8081/swagger-ui.html
```

## 🔄 Orden de Despliegue Recomendado

1. **MySQL** - Base de datos primero
2. **Auth Service** - Generador de tokens
3. **Ventas Service** - Validador de tokens
4. **Frontend** - Consume ambas APIs

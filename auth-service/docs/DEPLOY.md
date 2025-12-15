# Guía de Deploy - Auth Service

## 🚀 Plataformas de Despliegue

### 1. Railway (Recomendado)

#### Configuración
1. Crear nuevo proyecto en Railway
2. Conectar repositorio de GitHub
3. Configurar variables de entorno
4. Railway detectará automáticamente Maven/Spring Boot

#### Variables de Entorno (Railway Dashboard)
```
JWT_SECRET=TuClaveSecretaMuySeguraDe32CaracteresMinimo
JWT_ISSUER=tienda-app
JWT_EXP_MINUTES=60
SPRING_PROFILES_ACTIVE=prod
```

#### Base de Datos MySQL (Railway)
Railway puede provisionar MySQL automáticamente. Las credenciales se inyectan como variables de entorno.

### 2. Render

#### `render.yaml`
```yaml
services:
  - type: web
    name: auth-service
    env: java
    buildCommand: ./mvnw clean package -DskipTests
    startCommand: java -jar target/auth-service-1.0.0.jar
    envVars:
      - key: JWT_SECRET
        sync: false
      - key: SPRING_PROFILES_ACTIVE
        value: prod
```

### 3. Heroku

#### `Procfile`
```
web: java -jar target/auth-service-1.0.0.jar --server.port=$PORT
```

#### Deploy
```bash
heroku login
heroku create auth-service-tienda
heroku config:set JWT_SECRET=tu-clave-secreta-32-chars
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
COPY --from=build /app/target/auth-service-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### Docker Compose
```yaml
version: '3.8'
services:
  auth-service:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - JWT_SECRET=${JWT_SECRET}
      - SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/tienda
      - SPRING_DATASOURCE_USERNAME=root
      - SPRING_DATASOURCE_PASSWORD=secret
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

# Ejecutar aplicación
./mvnw spring-boot:run

# Ejecutar JAR
java -jar target/auth-service-1.0.0.jar
```

## ⚙️ Configuración de Producción

### `application-prod.properties`
```properties
# Base de datos (usar variables de entorno)
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USER}
spring.datasource.password=${DATABASE_PASSWORD}

# Puerto (Railway/Heroku asignan dinámicamente)
server.port=${PORT:8080}

# JWT (OBLIGATORIO en producción)
jwt.secret=${JWT_SECRET}
jwt.issuer=${JWT_ISSUER:tienda-app}
jwt.expiration-minutes=${JWT_EXP_MINUTES:60}

# Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
```

## 🔐 Variables de Entorno Requeridas

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `JWT_SECRET` | Clave secreta para firmar JWT | `MiClaveSecreta32CaracteresMinimo!` |
| `DATABASE_URL` | URL de conexión MySQL | `jdbc:mysql://host:3306/db` |
| `DATABASE_USER` | Usuario de base de datos | `root` |
| `DATABASE_PASSWORD` | Contraseña de BD | `password123` |
| `PORT` | Puerto del servidor | `8080` |

## 📋 Checklist Pre-Deploy

- [ ] JWT_SECRET configurado (mínimo 32 caracteres)
- [ ] Base de datos MySQL accesible
- [ ] Profile `prod` activado
- [ ] Tests pasando
- [ ] CORS configurado para dominio del frontend
- [ ] Swagger deshabilitado en producción (opcional)

## 🐛 Troubleshooting

### Error: JWT signature verification failed
- Verificar que `JWT_SECRET` sea el mismo en auth-service y ventas-service

### Error: Database connection refused
- Verificar credenciales y URL de base de datos
- Verificar que el firewall permita conexión al puerto de MySQL

### Error: Port already in use
- Cambiar puerto o matar proceso existente
- `netstat -ano | findstr :8080`

## 📊 Monitoreo

### Health Check Endpoint
```
GET /actuator/health
```

### Swagger UI (solo desarrollo)
```
http://localhost:8080/swagger-ui.html
```

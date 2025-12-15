# 🛒 Ventas Service - Microservicio de Ventas

## 📖 Descripción

Microservicio de gestión de ventas para el Sistema de Gestión de Tienda. Maneja:
- CRUD de productos
- Gestión de categorías
- Creación y consulta de boletas (ventas)
- Gestión de detalles de venta

## 🚀 Inicio Rápido

### Prerrequisitos
- Java 17+
- Maven 3.8+
- MySQL 8+
- Auth Service ejecutándose (para tokens JWT)

### Configuración de Base de Datos
```sql
-- Usa la misma BD que auth-service
CREATE DATABASE IF NOT EXISTS tienda;
```

### Variables de Entorno
```bash
# ⚠️ JWT_SECRET debe ser IGUAL al de auth-service
export JWT_SECRET=TuClaveSecretaDe32CaracteresMinimo
export DATABASE_URL=jdbc:mysql://localhost:3306/tienda
export DATABASE_USER=root
export DATABASE_PASSWORD=tu_password
```

### Ejecutar
```bash
# Con Maven Wrapper (puerto 8081 para evitar conflicto con auth-service)
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081

# O compilar y ejecutar JAR
./mvnw package -DskipTests
java -jar target/ventas-service-1.0.0.jar --server.port=8081
```

La API estará disponible en `http://localhost:8081`

## 📜 Comandos Maven

| Comando | Descripción |
|---------|-------------|
| `./mvnw spring-boot:run` | Ejecutar en desarrollo |
| `./mvnw test` | Ejecutar tests |
| `./mvnw package` | Generar JAR |
| `./mvnw clean install` | Build completo |

## 🏗️ Tecnologías

- **Java 17**
- **Spring Boot 3.5.5**
- **Spring Security 6**
- **Spring Data JPA**
- **MySQL 8**
- **JWT (jjwt 0.11.5)**
- **Lombok**
- **SpringDoc OpenAPI**

## 🔌 API Endpoints

### Productos
```
GET    /api/productos           - Listar (con filtros)
GET    /api/productos/{id}      - Obtener por ID
GET    /api/productos/activos   - Solo activos
POST   /api/productos           - Crear 🔐
PUT    /api/productos/{id}      - Actualizar 🔐
DELETE /api/productos/{id}      - Eliminar 🔐
```

### Categorías
```
GET    /api/categorias          - Listar
GET    /api/categorias/{id}     - Obtener por ID
POST   /api/categorias          - Crear 🔐
PUT    /api/categorias/{id}     - Actualizar 🔐
DELETE /api/categorias/{id}     - Eliminar 🔐
```

### Boletas
```
GET    /api/boletas             - Listar (con filtros)
GET    /api/boletas/{id}        - Obtener por ID
POST   /api/boletas             - Crear venta 🔐
PUT    /api/boletas/{id}        - Actualizar 🔐
DELETE /api/boletas/{id}        - Eliminar 🔐
```

> 🔐 = Requiere autenticación JWT

## 📖 Swagger UI

Disponible en: `http://localhost:8081/swagger-ui.html`

## 📚 Documentación Adicional

- [Arquitectura](./ARCHITECTURE.md)
- [API Reference](./API.md)
- [Guía de Deploy](./DEPLOY.md)

## 🔗 Microservicios Relacionados

- **Auth Service** - Autenticación y gestión de usuarios

## ⚠️ Notas Importantes

1. **JWT_SECRET**: Debe ser IDÉNTICO al configurado en auth-service
2. **Puerto**: Usar puerto diferente a auth-service (8081 vs 8080)
3. **Base de datos**: Comparte la misma BD MySQL con auth-service

## 📄 Licencia

MIT

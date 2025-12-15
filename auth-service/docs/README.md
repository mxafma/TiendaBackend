# 🔐 Auth Service - Microservicio de Autenticación

## 📖 Descripción

Microservicio de autenticación para el Sistema de Gestión de Tienda. Maneja:
- Login y registro de usuarios
- Generación de tokens JWT
- Gestión CRUD de usuarios
- Control de acceso basado en roles

## 🚀 Inicio Rápido

### Prerrequisitos
- Java 17+
- Maven 3.8+
- MySQL 8+

### Configuración de Base de Datos
```sql
CREATE DATABASE tienda_auth;
```

### Variables de Entorno
```bash
export JWT_SECRET=TuClaveSecretaDe32CaracteresMinimo
export DATABASE_URL=jdbc:mysql://localhost:3306/tienda_auth
export DATABASE_USER=root
export DATABASE_PASSWORD=tu_password
```

### Ejecutar
```bash
# Con Maven Wrapper
./mvnw spring-boot:run

# O compilar y ejecutar JAR
./mvnw package -DskipTests
java -jar target/auth-service-1.0.0.jar
```

La API estará disponible en `http://localhost:8080`

## 📜 Comandos Maven

| Comando | Descripción |
|---------|-------------|
| `./mvnw spring-boot:run` | Ejecutar en modo desarrollo |
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

### Públicos
```
POST /api/auth/login     - Iniciar sesión
POST /api/auth/register  - Registrar usuario
```

### Protegidos (JWT requerido)
```
GET    /api/usuarios       - Listar usuarios
GET    /api/usuarios/{id}  - Obtener usuario
PUT    /api/usuarios/{id}  - Actualizar usuario
DELETE /api/usuarios/{id}  - Eliminar usuario
```

## 📖 Swagger UI

Disponible en: `http://localhost:8080/swagger-ui.html`

## 🔐 Roles del Sistema

| Rol | Descripción |
|-----|-------------|
| ADMIN | Acceso completo |
| VENDEDOR | Gestión de ventas |
| CLIENTE | Usuario final |

## 📚 Documentación Adicional

- [Arquitectura](./ARCHITECTURE.md)
- [API Reference](./API.md)
- [Guía de Deploy](./DEPLOY.md)

## 🔗 Microservicios Relacionados

- **Ventas Service** - Productos, categorías y boletas

## ⚠️ Seguridad

- Nunca exponer `JWT_SECRET` en el código
- Usar HTTPS en producción
- Configurar CORS apropiadamente

## 📄 Licencia

MIT

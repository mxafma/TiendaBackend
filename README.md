# 🏪 Sistema de Gestión de Tienda - Backend

## 📖 Descripción

Backend del Sistema de Gestión de Tienda, implementado con arquitectura de **microservicios** usando Spring Boot 3.5.5 y Java 17.

## 🏗️ Arquitectura

```
┌─────────────────────────────────────────────────────────────────┐
│                         Frontend                                 │
│                    (React + TypeScript)                          │
└───────────────────────────┬─────────────────────────────────────┘
                            │
            ┌───────────────┴───────────────┐
            │                               │
            ▼                               ▼
┌───────────────────────┐       ┌───────────────────────┐
│    Auth Service       │       │   Ventas Service      │
│    (Puerto 8080)      │       │    (Puerto 8081)      │
├───────────────────────┤       ├───────────────────────┤
│ • Login/Register      │       │ • Productos           │
│ • JWT Generation      │       │ • Categorías          │
│ • Gestión Usuarios    │       │ • Boletas (Ventas)    │
│ • Control de Acceso   │       │ • JWT Validation      │
└───────────┬───────────┘       └───────────┬───────────┘
            │                               │
            └───────────────┬───────────────┘
                            │
                            ▼
                ┌───────────────────────┐
                │        MySQL          │
                │   (Base de datos)     │
                └───────────────────────┘
```

## 📁 Estructura del Proyecto

```
TiendaBackend/
├── auth-service/           # Microservicio de autenticación
│   ├── src/
│   ├── docs/               # Documentación
│   └── pom.xml
├── ventas-service/         # Microservicio de ventas
│   ├── src/
│   ├── docs/               # Documentación
│   └── pom.xml
├── mvnw                    # Maven Wrapper (Linux/Mac)
└── mvnw.cmd                # Maven Wrapper (Windows)
```

## 🚀 Inicio Rápido

### Prerrequisitos
- Java 17+
- Maven 3.8+
- MySQL 8+

### 1. Configurar Base de Datos
```sql
CREATE DATABASE tienda;
```

### 2. Variables de Entorno
```bash
export JWT_SECRET=TuClaveSecretaDe32CaracteresMinimo
export DATABASE_URL=jdbc:mysql://localhost:3306/tienda
export DATABASE_USER=root
export DATABASE_PASSWORD=tu_password
```

### 3. Ejecutar Auth Service
```bash
cd auth-service
./mvnw spring-boot:run
# Disponible en http://localhost:8080
```

### 4. Ejecutar Ventas Service
```bash
cd ventas-service
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
# Disponible en http://localhost:8081
```

## 📊 Stack Tecnológico

| Componente | Tecnología |
|------------|------------|
| Lenguaje | Java 17 |
| Framework | Spring Boot 3.5.5 |
| Seguridad | Spring Security 6 + JWT |
| ORM | Spring Data JPA |
| Base de Datos | MySQL 8 |
| Documentación | SpringDoc OpenAPI |
| Build | Maven |

## 🔐 Seguridad

- Autenticación basada en JWT
- Tokens generados por auth-service
- Tokens validados por ambos servicios
- Roles: ADMIN, VENDEDOR, CLIENTE

## 📚 Documentación de APIs

### Swagger UI
- **Auth Service**: http://localhost:8080/swagger-ui.html
- **Ventas Service**: http://localhost:8081/swagger-ui.html

### Documentación Detallada
- [Auth Service - Docs](./auth-service/docs/)
- [Ventas Service - Docs](./ventas-service/docs/)

## 🚀 Deploy

Ambos servicios están configurados para despliegue en **Railway**:

1. Conectar repositorio a Railway
2. Configurar variables de entorno
3. Railway detecta Spring Boot automáticamente

Ver guías de deploy específicas en cada servicio.

## ⚠️ Notas Importantes

1. **JWT_SECRET** debe ser IDÉNTICO en ambos servicios
2. Ambos servicios comparten la misma base de datos MySQL
3. Usar puertos diferentes en desarrollo (8080 y 8081)

## 📄 Licencia

MIT

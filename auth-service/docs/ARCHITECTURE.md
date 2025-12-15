# Arquitectura - Auth Service

## 📋 Descripción General

Microservicio de autenticación y gestión de usuarios desarrollado con **Spring Boot 3.5.5**. Responsable de:
- Autenticación de usuarios (login/register)
- Generación y validación de tokens JWT
- CRUD de usuarios
- Control de acceso basado en roles

## 🏗️ Stack Tecnológico

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 17 | Lenguaje |
| Spring Boot | 3.5.5 | Framework |
| Spring Security | 6.x | Seguridad |
| Spring Data JPA | 3.x | Persistencia |
| MySQL | 8.x | Base de datos |
| JWT (jjwt) | 0.11.5 | Tokens |
| Lombok | - | Reducción boilerplate |
| SpringDoc OpenAPI | 2.2.0 | Documentación API |

## 📁 Estructura del Proyecto

```
src/main/java/com/tienda/backend/
├── BackendApplication.java      # Clase principal
├── config/                      # Configuraciones
│   ├── CorsConfig.java          # Configuración CORS
│   └── SecurityConfig.java      # Configuración Spring Security
├── controller/                  # Controladores REST
│   ├── AuthController.java      # Endpoints de autenticación
│   └── UsuarioController.java   # CRUD de usuarios
├── dto/                         # Data Transfer Objects
│   ├── AuthResponse.java        # Respuesta de login
│   ├── LoginRequest.java        # Request de login
│   ├── UsuarioRequest.java      # Request de usuario
│   └── UsuarioResponse.java     # Response de usuario
├── model/                       # Entidades JPA
│   └── Usuario.java             # Entidad Usuario
├── repository/                  # Repositorios JPA
│   └── UsuarioRepository.java   # Repositorio de usuarios
├── security/                    # Seguridad
│   ├── JwtAuthFilter.java       # Filtro JWT
│   └── JwtService.java          # Servicio JWT
└── service/                     # Servicios de negocio
    └── UsuarioService.java      # Lógica de usuarios
```

## 🔐 Sistema de Seguridad

### Flujo de Autenticación
```
1. Cliente envía POST /api/auth/login con email + password
2. AuthController valida credenciales via UsuarioService
3. Si válido, JwtService genera token JWT
4. Token retornado junto con datos del usuario
5. Cliente incluye token en header Authorization: Bearer {token}
6. JwtAuthFilter intercepta requests y valida token
7. Si válido, SecurityContext se configura con el usuario
```

### JWT Token
- **Algoritmo**: HS256
- **Claims**: userId, email, rol
- **Expiración**: Configurable (default 60 min)

### Roles
| Rol | Descripción |
|-----|-------------|
| ADMIN | Acceso completo al sistema |
| VENDEDOR | Gestión de ventas |
| CLIENTE | Usuario final |

## 📊 Modelo de Datos

### Entidad Usuario
```
usuarios
├── id (BIGINT PK AUTO_INCREMENT)
├── nombre (VARCHAR NOT NULL)
├── apellido (VARCHAR)
├── email (VARCHAR UNIQUE NOT NULL)
├── password_hash (VARCHAR NOT NULL)
├── rol (VARCHAR NOT NULL)
├── activo (BOOLEAN DEFAULT true)
├── creado_en (DATETIME)
└── actualizado_en (DATETIME)
```

## 🔌 Endpoints API

### Autenticación (Públicos)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/auth/login` | Iniciar sesión |
| POST | `/api/auth/register` | Registrar usuario |

### Usuarios (Protegidos)
| Método | Endpoint | Descripción | Rol |
|--------|----------|-------------|-----|
| GET | `/api/usuarios` | Listar usuarios | ADMIN |
| GET | `/api/usuarios/{id}` | Obtener por ID | ADMIN |
| PUT | `/api/usuarios/{id}` | Actualizar | ADMIN |
| DELETE | `/api/usuarios/{id}` | Eliminar | ADMIN |

## 📐 Diagrama de Arquitectura

```
┌─────────────────────────────────────────────────────────────┐
│                      Auth Service                            │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐     │
│  │  Controller │───▶│   Service   │───▶│ Repository  │     │
│  └─────────────┘    └─────────────┘    └─────────────┘     │
│         │                                      │            │
│         ▼                                      ▼            │
│  ┌─────────────┐                       ┌─────────────┐     │
│  │ JwtService  │                       │    MySQL    │     │
│  └─────────────┘                       └─────────────┘     │
│         │                                                   │
│         ▼                                                   │
│  ┌─────────────┐                                           │
│  │JwtAuthFilter│                                           │
│  └─────────────┘                                           │
└─────────────────────────────────────────────────────────────┘
```

## ⚙️ Configuración

### Perfiles
- `default` - Desarrollo local
- `prod` - Producción (Railway)

### Variables de Entorno
| Variable | Descripción | Requerido |
|----------|-------------|-----------|
| JWT_SECRET | Clave secreta JWT (min 32 chars) | Sí |
| JWT_ISSUER | Emisor del token | No |
| JWT_EXP_MINUTES | Minutos de expiración | No |
| PORT | Puerto del servidor | No |

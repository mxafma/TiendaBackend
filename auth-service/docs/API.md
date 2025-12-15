# API Reference - Auth Service

## 🔗 Base URL
```
Desarrollo: http://localhost:8080
Producción: https://auth-service.railway.app
```

## 📖 Swagger UI
```
http://localhost:8080/swagger-ui.html
```

---

## 🔓 Endpoints Públicos

### POST /api/auth/login
Iniciar sesión con email y contraseña.

**Request:**
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "usuario@email.com",
  "password": "contraseña123"
}
```

**Response 200 OK:**
```json
{
  "mensaje": "Login exitoso",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "usuario": {
    "id": 1,
    "nombre": "Juan",
    "apellido": "Pérez",
    "email": "usuario@email.com",
    "rol": "CLIENTE",
    "activo": true
  }
}
```

**Response 401 Unauthorized:**
```json
{
  "mensaje": "Usuario o contraseña incorrectos",
  "token": null,
  "usuario": null
}
```

---

### POST /api/auth/register
Registrar un nuevo usuario.

**Request:**
```http
POST /api/auth/register
Content-Type: application/json

{
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "nuevo@email.com",
  "password": "contraseña123",
  "rol": "CLIENTE"
}
```

**Response 201 Created:**
```json
{
  "id": 2,
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "nuevo@email.com",
  "rol": "CLIENTE",
  "activo": true
}
```

---

## 🔐 Endpoints Protegidos

> Requieren header: `Authorization: Bearer {token}`

### GET /api/usuarios
Listar todos los usuarios.

**Request:**
```http
GET /api/usuarios
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
```

**Response 200 OK:**
```json
[
  {
    "id": 1,
    "nombre": "Admin",
    "apellido": "Sistema",
    "email": "admin@tienda.com",
    "rol": "ADMIN",
    "activo": true
  },
  {
    "id": 2,
    "nombre": "Juan",
    "apellido": "Pérez",
    "email": "juan@email.com",
    "rol": "CLIENTE",
    "activo": true
  }
]
```

---

### GET /api/usuarios/{id}
Obtener usuario por ID.

**Request:**
```http
GET /api/usuarios/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
```

**Response 200 OK:**
```json
{
  "id": 1,
  "nombre": "Admin",
  "apellido": "Sistema",
  "email": "admin@tienda.com",
  "rol": "ADMIN",
  "activo": true
}
```

**Response 404 Not Found:**
```json
{
  "error": "Usuario no encontrado"
}
```

---

### PUT /api/usuarios/{id}
Actualizar un usuario existente.

**Request:**
```http
PUT /api/usuarios/2
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
Content-Type: application/json

{
  "nombre": "Juan Carlos",
  "apellido": "Pérez García",
  "email": "juanc@email.com",
  "rol": "VENDEDOR",
  "activo": true
}
```

**Response 200 OK:**
```json
{
  "id": 2,
  "nombre": "Juan Carlos",
  "apellido": "Pérez García",
  "email": "juanc@email.com",
  "rol": "VENDEDOR",
  "activo": true
}
```

---

### DELETE /api/usuarios/{id}
Eliminar un usuario.

**Request:**
```http
DELETE /api/usuarios/2
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
```

**Response 204 No Content**

---

## 📋 Modelos

### LoginRequest
```json
{
  "email": "string (required)",
  "password": "string (required)"
}
```

### UsuarioRequest
```json
{
  "nombre": "string (required)",
  "apellido": "string (optional)",
  "email": "string (required, unique)",
  "password": "string (required for register)",
  "rol": "string (ADMIN|VENDEDOR|CLIENTE, default: CLIENTE)",
  "activo": "boolean (default: true)"
}
```

### UsuarioResponse
```json
{
  "id": "number",
  "nombre": "string",
  "apellido": "string",
  "email": "string",
  "rol": "string",
  "activo": "boolean"
}
```

### AuthResponse
```json
{
  "mensaje": "string",
  "token": "string (JWT)",
  "usuario": "UsuarioResponse"
}
```

---

## ⚠️ Códigos de Error

| Código | Descripción |
|--------|-------------|
| 200 | OK - Operación exitosa |
| 201 | Created - Recurso creado |
| 204 | No Content - Eliminado exitosamente |
| 400 | Bad Request - Datos inválidos |
| 401 | Unauthorized - Token inválido/expirado |
| 403 | Forbidden - Sin permisos |
| 404 | Not Found - Recurso no encontrado |
| 409 | Conflict - Email ya registrado |
| 500 | Internal Server Error |

---

## 🔑 JWT Token

### Estructura del Token
```
Header: { "alg": "HS256", "typ": "JWT" }
Payload: {
  "sub": "usuario@email.com",
  "userId": 1,
  "email": "usuario@email.com",
  "rol": "CLIENTE",
  "iat": 1702500000,
  "exp": 1702503600
}
```

### Expiración
Por defecto: 60 minutos (configurable via `JWT_EXP_MINUTES`)

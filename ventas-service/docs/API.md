# API Reference - Ventas Service

## 🔗 Base URL
```
Desarrollo: http://localhost:8081
Producción: https://ventas-service.railway.app
```

## 📖 Swagger UI
```
http://localhost:8081/swagger-ui.html
```

---

## 📦 Productos

### GET /api/productos
Listar todos los productos con filtros opcionales.

**Query Parameters:**
| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| categoriaId | Long | Filtrar por categoría |
| search | String | Buscar por nombre |

**Request:**
```http
GET /api/productos
GET /api/productos?categoriaId=1
GET /api/productos?search=laptop
```

**Response 200 OK:**
```json
[
  {
    "id": 1,
    "nombre": "Laptop HP",
    "descripcion": "Laptop HP 15 pulgadas",
    "codigoBarra": "7501234567890",
    "precioVenta": 999.99,
    "stockActual": 10,
    "activo": true,
    "categoria": {
      "id": 1,
      "nombre": "Electrónicos"
    },
    "creadoEn": "2024-01-15T10:30:00",
    "actualizadoEn": "2024-01-15T10:30:00"
  }
]
```

---

### GET /api/productos/{id}
Obtener producto por ID.

**Response 200 OK:**
```json
{
  "id": 1,
  "nombre": "Laptop HP",
  "descripcion": "Laptop HP 15 pulgadas",
  "precioVenta": 999.99,
  "stockActual": 10,
  "activo": true,
  "categoria": {
    "id": 1,
    "nombre": "Electrónicos"
  }
}
```

---

### GET /api/productos/activos
Obtener solo productos activos.

---

### POST /api/productos 🔐
Crear un nuevo producto.

**Request:**
```http
POST /api/productos
Authorization: Bearer {token}
Content-Type: application/json

{
  "nombre": "Mouse Gamer",
  "descripcion": "Mouse RGB 16000 DPI",
  "codigoBarra": "7501234567891",
  "precioVenta": 49.99,
  "stockActual": 50,
  "categoriaId": 1,
  "activo": true
}
```

**Response 201 Created**

---

### PUT /api/productos/{id} 🔐
Actualizar producto existente.

---

### PATCH /api/productos/{id}/stock 🔐
Actualizar solo el stock de un producto.

**Request:**
```http
PATCH /api/productos/1/stock?cantidad=5
Authorization: Bearer {token}
```

---

### DELETE /api/productos/{id} 🔐
Eliminar producto.

---

## 📁 Categorías

### GET /api/categorias
Listar todas las categorías.

**Response 200 OK:**
```json
[
  {
    "id": 1,
    "nombre": "Electrónicos",
    "descripcion": "Dispositivos electrónicos"
  },
  {
    "id": 2,
    "nombre": "Ropa",
    "descripcion": "Vestimenta y accesorios"
  }
]
```

---

### GET /api/categorias/{id}
Obtener categoría por ID.

---

### POST /api/categorias 🔐
Crear nueva categoría.

**Request:**
```http
POST /api/categorias
Authorization: Bearer {token}
Content-Type: application/json

{
  "nombre": "Hogar",
  "descripcion": "Artículos para el hogar"
}
```

---

### PUT /api/categorias/{id} 🔐
Actualizar categoría.

---

### DELETE /api/categorias/{id} 🔐
Eliminar categoría.

---

## 🧾 Boletas (Ventas)

### GET /api/boletas
Listar todas las boletas con filtros opcionales.

**Query Parameters:**
| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| usuarioId | Long | Filtrar por usuario |
| desde | Date | Fecha inicio (ISO) |
| hasta | Date | Fecha fin (ISO) |

**Request:**
```http
GET /api/boletas
GET /api/boletas?usuarioId=1
GET /api/boletas?desde=2024-01-01&hasta=2024-01-31
```

**Response 200 OK:**
```json
[
  {
    "id": 1,
    "fechaHora": "2024-01-15T14:30:00",
    "usuarioId": 1,
    "totalBruto": 1099.98,
    "totalDescuento": 0.00,
    "totalNeto": 1099.98,
    "metodoPago": "TARJETA",
    "detalles": [
      {
        "id": 1,
        "productoId": 1,
        "cantidad": 1,
        "precioUnitario": 999.99,
        "subtotal": 999.99
      },
      {
        "id": 2,
        "productoId": 5,
        "cantidad": 2,
        "precioUnitario": 49.99,
        "subtotal": 99.98
      }
    ]
  }
]
```

---

### GET /api/boletas/{id}
Obtener boleta por ID con sus detalles.

---

### POST /api/boletas 🔐
Crear nueva boleta (venta).

**Request:**
```http
POST /api/boletas
Authorization: Bearer {token}
Content-Type: application/json

{
  "usuarioId": 1,
  "totalBruto": 149.97,
  "totalDescuento": 0,
  "totalNeto": 149.97,
  "metodoPago": "EFECTIVO",
  "detalles": [
    {
      "productoId": 5,
      "cantidad": 3,
      "precioUnitario": 49.99,
      "subtotal": 149.97
    }
  ]
}
```

**Response 201 Created**

---

### PUT /api/boletas/{id} 🔐
Actualizar boleta (usar con precaución).

---

### DELETE /api/boletas/{id} 🔐
Eliminar boleta.

---

## 📋 Modelos

### ProductoDTO (Request)
```json
{
  "nombre": "string (required)",
  "descripcion": "string",
  "codigoBarra": "string",
  "precioVenta": "decimal (required)",
  "stockActual": "integer",
  "categoriaId": "long (required)",
  "activo": "boolean"
}
```

### Producto (Response)
```json
{
  "id": "long",
  "nombre": "string",
  "descripcion": "string",
  "codigoBarra": "string",
  "precioVenta": "decimal",
  "stockActual": "integer",
  "activo": "boolean",
  "categoria": "Categoria",
  "creadoEn": "datetime",
  "actualizadoEn": "datetime"
}
```

### Boleta
```json
{
  "id": "long",
  "fechaHora": "datetime",
  "usuarioId": "long",
  "totalBruto": "decimal",
  "totalDescuento": "decimal",
  "totalNeto": "decimal",
  "metodoPago": "EFECTIVO|TARJETA|TRANSFERENCIA|MIXTO",
  "detalles": "DetalleBoleta[]"
}
```

### DetalleBoleta
```json
{
  "id": "long",
  "productoId": "long",
  "cantidad": "integer",
  "precioUnitario": "decimal",
  "subtotal": "decimal"
}
```

---

## ⚠️ Códigos de Error

| Código | Descripción |
|--------|-------------|
| 200 | OK |
| 201 | Created |
| 204 | No Content |
| 400 | Bad Request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |
| 500 | Internal Server Error |

---

## 🔐 Autenticación

Endpoints marcados con 🔐 requieren:
```http
Authorization: Bearer {jwt_token}
```

El token JWT es generado por auth-service y validado por este servicio.

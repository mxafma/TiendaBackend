# Arquitectura - Ventas Service

## 📋 Descripción General

Microservicio de gestión de ventas desarrollado con **Spring Boot 3.5.5**. Responsable de:
- CRUD de productos
- Gestión de categorías
- Creación y consulta de boletas (ventas)
- Gestión de detalles de venta

## 🏗️ Stack Tecnológico

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 17 | Lenguaje |
| Spring Boot | 3.5.5 | Framework |
| Spring Security | 6.x | Seguridad |
| Spring Data JPA | 3.x | Persistencia |
| MySQL | 8.x | Base de datos |
| JWT (jjwt) | 0.11.5 | Validación tokens |
| Lombok | - | Reducción boilerplate |
| SpringDoc OpenAPI | 2.2.0 | Documentación API |

## 📁 Estructura del Proyecto

```
src/main/java/com/tienda/backend/
├── BackendApplication.java          # Clase principal
├── config/                          # Configuraciones
│   ├── CorsConfig.java              # Configuración CORS
│   └── SecurityConfig.java          # Configuración Spring Security
├── controller/                      # Controladores REST
│   ├── ProductoController.java      # CRUD de productos
│   ├── CategoriaController.java     # CRUD de categorías
│   ├── BoletaController.java        # Gestión de boletas
│   └── DetalleBoletaController.java # Detalles de venta
├── dto/                             # Data Transfer Objects
│   └── ProductoDTO.java             # DTO de producto
├── model/                           # Entidades JPA
│   ├── Producto.java                # Entidad Producto
│   ├── Categoria.java               # Entidad Categoría
│   ├── Boleta.java                  # Entidad Boleta
│   └── DetalleBoleta.java           # Entidad Detalle
├── repository/                      # Repositorios JPA
│   ├── ProductoRepository.java
│   ├── CategoriaRepository.java
│   ├── BoletaRepository.java
│   └── DetalleBoletaRepository.java
├── security/                        # Seguridad
│   ├── JwtAuthFilter.java           # Filtro JWT
│   └── JwtService.java              # Servicio JWT
└── service/                         # Servicios de negocio
    ├── ProductoService.java
    ├── CategoriaService.java
    └── BoletaService.java
```

## 📊 Modelo de Datos

### Diagrama ER
```
┌─────────────┐       ┌─────────────┐
│  Categoria  │       │   Producto  │
├─────────────┤       ├─────────────┤
│ id          │◄──────│ id          │
│ nombre      │  1:N  │ nombre      │
│ descripcion │       │ descripcion │
└─────────────┘       │ codigoBarra │
                      │ precioVenta │
                      │ stockActual │
                      │ activo      │
                      │ categoria_id│
                      │ creadoEn    │
                      │ actualizadoEn│
                      └─────────────┘
                            │
                            │ 1:N
                            ▼
┌─────────────┐       ┌───────────────┐
│   Boleta    │       │ DetalleBoleta │
├─────────────┤       ├───────────────┤
│ id          │◄──────│ id            │
│ fechaHora   │  1:N  │ boleta_id     │
│ usuarioId   │       │ producto_id   │
│ totalBruto  │       │ cantidad      │
│ totalDesc   │       │ precioUnit    │
│ totalNeto   │       │ subtotal      │
│ metodoPago  │       └───────────────┘
└─────────────┘
```

### Entidades

#### Producto
```
productos
├── id (BIGINT PK AUTO_INCREMENT)
├── nombre (VARCHAR NOT NULL)
├── descripcion (VARCHAR 1000)
├── codigo_barra (VARCHAR)
├── precio_venta (DECIMAL 10,2 NOT NULL)
├── stock_actual (INT NOT NULL DEFAULT 0)
├── activo (BOOLEAN DEFAULT true)
├── categoria_id (BIGINT FK NOT NULL)
├── creado_en (DATETIME)
└── actualizado_en (DATETIME)
```

#### Categoria
```
categorias
├── id (BIGINT PK AUTO_INCREMENT)
├── nombre (VARCHAR NOT NULL)
└── descripcion (VARCHAR)
```

#### Boleta
```
boletas
├── id (BIGINT PK AUTO_INCREMENT)
├── fecha_hora (DATETIME NOT NULL)
├── usuario_id (BIGINT NOT NULL)
├── total_bruto (DECIMAL 10,2 NOT NULL)
├── total_descuento (DECIMAL 10,2 DEFAULT 0)
├── total_neto (DECIMAL 10,2 NOT NULL)
└── metodo_pago (VARCHAR)
```

#### DetalleBoleta
```
detalle_boletas
├── id (BIGINT PK AUTO_INCREMENT)
├── boleta_id (BIGINT FK NOT NULL)
├── producto_id (BIGINT NOT NULL)
├── cantidad (INT NOT NULL)
├── precio_unitario (DECIMAL 10,2 NOT NULL)
└── subtotal (DECIMAL 10,2 NOT NULL)
```

## 🔐 Seguridad

### Validación JWT
- El servicio valida tokens JWT generados por Auth Service
- Usa la misma clave secreta (`JWT_SECRET`) que auth-service
- No genera tokens, solo los valida

### Endpoints Públicos
- `GET /api/productos` - Listar productos
- `GET /api/productos/activos` - Productos activos
- `GET /api/categorias` - Listar categorías

### Endpoints Protegidos
- Operaciones de escritura (POST, PUT, DELETE)
- Gestión de boletas
- Requieren rol ADMIN o VENDEDOR

## 📐 Diagrama de Arquitectura

```
┌─────────────────────────────────────────────────────────────┐
│                     Ventas Service                           │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌──────────────────────────────────────────────────────┐   │
│  │                   Controllers                         │   │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌─────────┐ │   │
│  │  │Producto  │ │Categoria │ │ Boleta   │ │Detalle  │ │   │
│  │  │Controller│ │Controller│ │Controller│ │Controller│ │   │
│  │  └────┬─────┘ └────┬─────┘ └────┬─────┘ └────┬────┘ │   │
│  └───────┼────────────┼────────────┼────────────┼───────┘   │
│          │            │            │            │            │
│  ┌───────▼────────────▼────────────▼────────────▼───────┐   │
│  │                    Services                           │   │
│  └───────┬────────────┬────────────┬────────────────────┘   │
│          │            │            │                         │
│  ┌───────▼────────────▼────────────▼────────────────────┐   │
│  │                  Repositories                         │   │
│  └───────┬────────────┬────────────┬────────────────────┘   │
│          │            │            │                         │
│          └────────────┼────────────┘                         │
│                       ▼                                      │
│              ┌─────────────┐                                │
│              │    MySQL    │                                │
│              └─────────────┘                                │
└─────────────────────────────────────────────────────────────┘
```

## ⚙️ Configuración

### Perfiles
- `default` - Desarrollo local
- `prod` - Producción (Railway)

### Variables de Entorno
| Variable | Descripción | Requerido |
|----------|-------------|-----------|
| JWT_SECRET | Misma clave que auth-service | Sí |
| DATABASE_URL | URL de conexión MySQL | Sí |
| PORT | Puerto del servidor | No |

## 🔗 Comunicación Entre Servicios

Este microservicio es **independiente** pero:
- Comparte la misma base de datos MySQL
- Usa la misma clave JWT para validación
- `usuarioId` en Boleta referencia usuarios de auth-service

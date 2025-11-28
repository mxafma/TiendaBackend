package com.tienda.backend.controller;

import com.tienda.backend.model.Producto;
import com.tienda.backend.dto.ProductoDTO;
import com.tienda.backend.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
@Tag(name = "Productos", description = "API de gestión de productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo producto")
    public ResponseEntity<Producto> createProducto(@RequestBody ProductoDTO productoDto) {
        Producto nuevoProducto = productoService.createProductoFromDTO(productoDto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        try {
            Producto producto = productoService.getProductoById(id);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Obtener todos los productos con filtros opcionales")
    public ResponseEntity<List<Producto>> getAllProductos(
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) String search) {
        
        List<Producto> productos;
        
        if (categoriaId != null) {
            productos = productoService.getProductosByCategoria(categoriaId);
        } else if (search != null && !search.isEmpty()) {
            productos = productoService.buscarPorNombre(search);
        } else {
            productos = productoService.getAllProductos();
        }
        
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/activos")
    @Operation(summary = "Obtener solo los productos activos")
    public ResponseEntity<List<Producto>> getProductosActivos() {
        List<Producto> productos = productoService.getProductosActivos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/codigo-barra/{codigoBarra}")
    @Operation(summary = "Buscar producto por código de barra")
    public ResponseEntity<List<Producto>> buscarPorCodigoBarra(@PathVariable String codigoBarra) {
        List<Producto> productos = productoService.buscarPorCodigoBarra(codigoBarra);
        return ResponseEntity.ok(productos);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un producto existente")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            Producto productoActualizado = productoService.updateProducto(id, producto);
            return ResponseEntity.ok(productoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/stock")
    @Operation(summary = "Actualizar solo el stock de un producto")
    public ResponseEntity<Producto> actualizarStock(
            @PathVariable Long id, 
            @RequestParam Integer nuevoStock) {
        try {
            Producto producto = productoService.actualizarStock(id, nuevoStock);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/reducir-stock")
    @Operation(summary = "Reducir stock de un producto (al vender)")
    public ResponseEntity<Producto> reducirStock(
            @PathVariable Long id, 
            @RequestParam Integer cantidad) {
        try {
            Producto producto = productoService.reducirStock(id, cantidad);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/aumentar-stock")
    @Operation(summary = "Aumentar stock de un producto (al recibir inventario)")
    public ResponseEntity<Producto> aumentarStock(
            @PathVariable Long id, 
            @RequestParam Integer cantidad) {
        try {
            Producto producto = productoService.aumentarStock(id, cantidad);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto (soft delete)")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        try {
            productoService.deleteProducto(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

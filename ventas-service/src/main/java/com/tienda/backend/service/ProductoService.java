package com.tienda.backend.service;

import com.tienda.backend.model.Producto;
import com.tienda.backend.model.Categoria;
import com.tienda.backend.dto.ProductoDTO;
import com.tienda.backend.repository.ProductoRepository;
import com.tienda.backend.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // CRUD Completo
    public Producto createProducto(Producto producto) {
        if (producto.getActivo() == null) {
            producto.setActivo(true);
        }
        if (producto.getStockActual() == null) {
            producto.setStockActual(0);
        }
        return productoRepository.save(producto);
    }

    // Crear producto a partir de DTO (mapea categoria por id)
    public Producto createProductoFromDTO(ProductoDTO dto) {
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + dto.getCategoriaId()));

        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setCodigoBarra(dto.getCodigoBarra());
        producto.setPrecioVenta(dto.getPrecioVenta());
        producto.setStockActual(dto.getStockActual() != null ? dto.getStockActual() : 0);
        producto.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
        producto.setCategoria(categoria);

        return productoRepository.save(producto);
    }

    public Producto getProductoById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    public List<Producto> getProductosActivos() {
        return productoRepository.findByActivoTrue();
    }

    public List<Producto> getProductosByCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Producto> buscarPorCodigoBarra(String codigoBarra) {
        return productoRepository.findByCodigoBarra(codigoBarra);
    }

    public Producto updateProducto(Long id, Producto productoActualizado) {
        Producto productoExistente = getProductoById(id);
        
        productoExistente.setNombre(productoActualizado.getNombre());
        productoExistente.setDescripcion(productoActualizado.getDescripcion());
        productoExistente.setCodigoBarra(productoActualizado.getCodigoBarra());
        productoExistente.setPrecioVenta(productoActualizado.getPrecioVenta());
        productoExistente.setStockActual(productoActualizado.getStockActual());
        productoExistente.setCategoria(productoActualizado.getCategoria());
        
        if (productoActualizado.getActivo() != null) {
            productoExistente.setActivo(productoActualizado.getActivo());
        }
        
        return productoRepository.save(productoExistente);
    }

    // Método específico para actualizar solo el stock
    public Producto actualizarStock(Long productoId, Integer nuevoStock) {
        Producto producto = getProductoById(productoId);
        producto.setStockActual(nuevoStock);
        return productoRepository.save(producto);
    }

    // Método para reducir stock (útil al vender)
    public Producto reducirStock(Long productoId, Integer cantidad) {
        Producto producto = getProductoById(productoId);
        
        if (producto.getStockActual() < cantidad) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + producto.getStockActual());
        }
        
        producto.setStockActual(producto.getStockActual() - cantidad);
        return productoRepository.save(producto);
    }

    // Método para aumentar stock (útil al recibir inventario)
    public Producto aumentarStock(Long productoId, Integer cantidad) {
        Producto producto = getProductoById(productoId);
        producto.setStockActual(producto.getStockActual() + cantidad);
        return productoRepository.save(producto);
    }

    // Soft delete - marca como inactivo
    public void deleteProducto(Long id) {
        Producto producto = getProductoById(id);
        producto.setActivo(false);
        productoRepository.save(producto);
    }
}

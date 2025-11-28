package com.tienda.backend.service;

import com.tienda.backend.model.DetalleBoleta;
import com.tienda.backend.model.Producto;
import com.tienda.backend.repository.DetalleBoletaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DetalleBoletaService {

    private final DetalleBoletaRepository detalleBoletaRepository;
    private final ProductoService productoService;

    public DetalleBoletaService(DetalleBoletaRepository detalleBoletaRepository, 
                                ProductoService productoService) {
        this.detalleBoletaRepository = detalleBoletaRepository;
        this.productoService = productoService;
    }

    // Crear detalle (normalmente no se usa solo, sino dentro de createBoleta)
    public DetalleBoleta createDetalle(DetalleBoleta detalle) {
        // Obtener el producto para calcular precios
        Producto producto = productoService.getProductoById(detalle.getProducto().getId());
        
        // Establecer precio unitario del producto
        detalle.setPrecioUnitario(producto.getPrecioVenta());
        
        // Calcular subtotal
        BigDecimal subtotal = producto.getPrecioVenta()
            .multiply(BigDecimal.valueOf(detalle.getCantidad()));
        detalle.setSubtotal(subtotal);
        
        // Inicializar descuento si es null
        if (detalle.getDescuentoLinea() == null) {
            detalle.setDescuentoLinea(BigDecimal.ZERO);
        }
        
        // Calcular total línea
        BigDecimal totalLinea = subtotal.subtract(detalle.getDescuentoLinea());
        detalle.setTotalLinea(totalLinea);
        
        return detalleBoletaRepository.save(detalle);
    }

    public DetalleBoleta getDetalleById(Long id) {
        return detalleBoletaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de boleta no encontrado con id: " + id));
    }

    public List<DetalleBoleta> getAllDetalles() {
        return detalleBoletaRepository.findAll();
    }

    public List<DetalleBoleta> getDetallesByBoleta(Long boletaId) {
        return detalleBoletaRepository.findByBoletaId(boletaId);
    }

    public List<DetalleBoleta> getDetallesByProducto(Long productoId) {
        return detalleBoletaRepository.findByProductoId(productoId);
    }

    public DetalleBoleta updateDetalle(Long id, DetalleBoleta detalleActualizado) {
        DetalleBoleta detalleExistente = getDetalleById(id);
        
        // Actualizar cantidad
        detalleExistente.setCantidad(detalleActualizado.getCantidad());
        
        // Recalcular subtotal
        BigDecimal subtotal = detalleExistente.getPrecioUnitario()
            .multiply(BigDecimal.valueOf(detalleActualizado.getCantidad()));
        detalleExistente.setSubtotal(subtotal);
        
        // Actualizar descuento si viene
        if (detalleActualizado.getDescuentoLinea() != null) {
            detalleExistente.setDescuentoLinea(detalleActualizado.getDescuentoLinea());
        }
        
        // Recalcular total línea
        BigDecimal totalLinea = subtotal.subtract(detalleExistente.getDescuentoLinea());
        detalleExistente.setTotalLinea(totalLinea);
        
        return detalleBoletaRepository.save(detalleExistente);
    }

    public void deleteDetalle(Long id) {
        DetalleBoleta detalle = getDetalleById(id);
        detalleBoletaRepository.delete(detalle);
    }
}

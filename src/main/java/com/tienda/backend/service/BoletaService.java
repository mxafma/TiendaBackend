package com.tienda.backend.service;

import com.tienda.backend.model.Boleta;
import com.tienda.backend.model.DetalleBoleta;
import com.tienda.backend.model.Producto;
import com.tienda.backend.repository.BoletaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class BoletaService {

    private final BoletaRepository boletaRepository;
    private final ProductoService productoService;

    public BoletaService(BoletaRepository boletaRepository, ProductoService productoService) {
        this.boletaRepository = boletaRepository;
        this.productoService = productoService;
    }

    // Crear boleta con reducción automática de stock
    @Transactional
    public Boleta createBoleta(Boleta boleta) {
        // Validar y calcular totales
        BigDecimal totalBruto = BigDecimal.ZERO;
        
        for (DetalleBoleta detalle : boleta.getDetalles()) {
            // Obtener el producto y su precio actual
            Producto producto = productoService.getProductoById(detalle.getProducto().getId());
            
            // Validar stock disponible
            if (producto.getStockActual() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre() 
                    + ". Disponible: " + producto.getStockActual());
            }
            
            // Establecer precio unitario del producto
            detalle.setPrecioUnitario(producto.getPrecioVenta());
            
            // Calcular subtotal del detalle
            BigDecimal subtotal = producto.getPrecioVenta()
                .multiply(BigDecimal.valueOf(detalle.getCantidad()));
            detalle.setSubtotal(subtotal);
            
            // Establecer el producto completo en el detalle
            detalle.setProducto(producto);
            
            // Establecer relación bidireccional
            detalle.setBoleta(boleta);
            
            // Acumular total bruto
            totalBruto = totalBruto.add(subtotal);
            
            // Reducir stock del producto
            productoService.reducirStock(producto.getId(), detalle.getCantidad());
        }
        
        // Establecer totales en la boleta
        boleta.setTotalBruto(totalBruto);
        
        if (boleta.getTotalDescuento() == null) {
            boleta.setTotalDescuento(BigDecimal.ZERO);
        }
        
        BigDecimal totalNeto = totalBruto.subtract(boleta.getTotalDescuento());
        boleta.setTotalNeto(totalNeto);
        
        // Establecer fecha/hora si no viene
        if (boleta.getFechaHora() == null) {
            boleta.setFechaHora(LocalDateTime.now());
        }
        
        return boletaRepository.save(boleta);
    }

    public Boleta getBoletaById(Long id) {
        return boletaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Boleta no encontrada con id: " + id));
    }

    public List<Boleta> getAllBoletas() {
        return boletaRepository.findAllOrderByFechaDesc();
    }

    public List<Boleta> getBoletasByUsuario(Long usuarioId) {
        return boletaRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);
    }

    public List<Boleta> getBoletasByFecha(LocalDate fechaDesde, LocalDate fechaHasta) {
        LocalDateTime inicio = fechaDesde.atStartOfDay();
        LocalDateTime fin = fechaHasta.atTime(LocalTime.MAX);
        return boletaRepository.findByFechaHoraBetween(inicio, fin);
    }

    // Actualizar boleta (usar con precaución)
    @Transactional
    public Boleta updateBoleta(Long id, Boleta boletaActualizada) {
        Boleta boletaExistente = getBoletaById(id);
        
        // Solo permitir actualizar ciertos campos
        boletaExistente.setMetodoPago(boletaActualizada.getMetodoPago());
        boletaExistente.setTotalDescuento(boletaActualizada.getTotalDescuento());
        
        // Recalcular total neto
        BigDecimal totalNeto = boletaExistente.getTotalBruto()
            .subtract(boletaActualizada.getTotalDescuento());
        boletaExistente.setTotalNeto(totalNeto);
        
        return boletaRepository.save(boletaExistente);
    }

    // Anular boleta (soft delete o marcar como anulada)
    @Transactional
    public void deleteBoleta(Long id) {
        Boleta boleta = getBoletaById(id);
        
        // Devolver stock de los productos
        for (DetalleBoleta detalle : boleta.getDetalles()) {
            productoService.aumentarStock(detalle.getProducto().getId(), detalle.getCantidad());
        }
        
        // Eliminar físicamente (o podrías agregar un campo "anulada" para soft delete)
        boletaRepository.delete(boleta);
    }
}

package com.tienda.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tienda.backend.model.Boleta;
import com.tienda.backend.model.DetalleBoleta;
import com.tienda.backend.model.Producto;
import com.tienda.backend.repository.BoletaRepository;

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

        BigDecimal totalBruto = BigDecimal.ZERO;

        for (DetalleBoleta detalle : boleta.getDetalles()) {

            Producto producto = productoService.getProductoById(detalle.getProducto().getId());

            if (producto.getStockActual() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " 
                        + producto.getNombre() + ". Disponible: " + producto.getStockActual());
            }

            detalle.setPrecioUnitario(producto.getPrecioVenta());

            BigDecimal subtotal = producto.getPrecioVenta()
                    .multiply(BigDecimal.valueOf(detalle.getCantidad()));
            detalle.setSubtotal(subtotal);

            detalle.setProducto(producto);
            detalle.setBoleta(boleta);

            totalBruto = totalBruto.add(subtotal);

            productoService.reducirStock(producto.getId(), detalle.getCantidad());
        }

        boleta.setTotalBruto(totalBruto);

        if (boleta.getTotalDescuento() == null) {
            boleta.setTotalDescuento(BigDecimal.ZERO);
        }

        BigDecimal totalNeto = totalBruto.subtract(boleta.getTotalDescuento());
        boleta.setTotalNeto(totalNeto);

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
        return boletaRepository.findAllByOrderByFechaHoraDesc();
    }

    public List<Boleta> getBoletasByUsuario(Long usuarioId) {
        return boletaRepository.findByUsuarioIdOrderByFechaHoraDesc(usuarioId);
    }

    public List<Boleta> getBoletasByFecha(LocalDate fechaDesde, LocalDate fechaHasta) {
        LocalDateTime inicio = fechaDesde.atStartOfDay();
        LocalDateTime fin = fechaHasta.atTime(LocalTime.MAX);
        return boletaRepository.findByFechaHoraBetween(inicio, fin);
    }

    @Transactional
    public Boleta updateBoleta(Long id, Boleta boletaActualizada) {

        Boleta boletaExistente = getBoletaById(id);

        boletaExistente.setMetodoPago(boletaActualizada.getMetodoPago());
        boletaExistente.setTotalDescuento(boletaActualizada.getTotalDescuento());

        BigDecimal totalNeto = boletaExistente.getTotalBruto()
                .subtract(boletaActualizada.getTotalDescuento());
        boletaExistente.setTotalNeto(totalNeto);

        return boletaRepository.save(boletaExistente);
    }

    @Transactional
    public void deleteBoleta(Long id) {

        Boleta boleta = getBoletaById(id);

        for (DetalleBoleta detalle : boleta.getDetalles()) {
            productoService.aumentarStock(detalle.getProducto().getId(), detalle.getCantidad());
        }

        boletaRepository.delete(boleta);
    }
}

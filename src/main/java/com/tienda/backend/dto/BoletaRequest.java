package com.tienda.backend.dto;

import java.math.BigDecimal;
import java.util.List;

public class BoletaRequest {
    private Long usuarioId;
    private BigDecimal totalDescuento;
    private String metodoPago;
    private List<DetalleBoletaRequest> detalles;

    // Constructores
    public BoletaRequest() {
    }

    // Getters y Setters
    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public BigDecimal getTotalDescuento() {
        return totalDescuento;
    }

    public void setTotalDescuento(BigDecimal totalDescuento) {
        this.totalDescuento = totalDescuento;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public List<DetalleBoletaRequest> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleBoletaRequest> detalles) {
        this.detalles = detalles;
    }
}

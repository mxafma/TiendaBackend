package com.tienda.backend.dto;

import java.math.BigDecimal;

public class DetalleBoletaRequest {
    private Long productoId;
    private Integer cantidad;
    private BigDecimal descuentoLinea;

    // Constructores
    public DetalleBoletaRequest() {
    }

    // Getters y Setters
    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getDescuentoLinea() {
        return descuentoLinea;
    }

    public void setDescuentoLinea(BigDecimal descuentoLinea) {
        this.descuentoLinea = descuentoLinea;
    }
}

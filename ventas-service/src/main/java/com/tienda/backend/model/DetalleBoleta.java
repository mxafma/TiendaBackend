package com.tienda.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Data;

@Entity
@Table(name = "detalle_boletas")
@Data
public class DetalleBoleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "boleta_id", nullable = false)
    @JsonBackReference
    private Boleta boleta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(precision = 10, scale = 2)
    private BigDecimal descuentoLinea = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalLinea;

    // Callback para calcular totalLinea automáticamente antes de persistir
    @PrePersist
    @PreUpdate
    protected void calcularTotalLinea() {
        if (descuentoLinea == null) {
            descuentoLinea = BigDecimal.ZERO;
        }
        totalLinea = subtotal.subtract(descuentoLinea);
    }
}

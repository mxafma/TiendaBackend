package com.tienda.backend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "boletas")
@Data
public class Boleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalBruto;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalDescuento = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalNeto;

    @Column(nullable = true)
    private String metodoPago; // EFECTIVO, TARJETA, TRANSFERENCIA, MIXTO

    @OneToMany(
        mappedBy = "boleta",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @JsonManagedReference
    private List<DetalleBoleta> detalles = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        fechaHora = LocalDateTime.now();
    }
}

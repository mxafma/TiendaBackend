package com.tienda.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Table(name = "categorias")
@Data
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Column(nullable = false)
    private Boolean activa = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creadaEn;

    @Column(nullable = false)
    private LocalDateTime actualizadaEn;

    // Callbacks de JPA para timestamps automáticos
    @PrePersist
    protected void onCreate() {
        creadaEn = LocalDateTime.now();
        actualizadaEn = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        actualizadaEn = LocalDateTime.now();
    }
}

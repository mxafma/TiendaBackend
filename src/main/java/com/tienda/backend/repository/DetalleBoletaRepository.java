package com.tienda.backend.repository;

import com.tienda.backend.model.DetalleBoleta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleBoletaRepository extends JpaRepository<DetalleBoleta, Long> {
    List<DetalleBoleta> findByBoletaId(Long boletaId);
    List<DetalleBoleta> findByProductoId(Long productoId);
}

package com.tienda.backend.repository;

import com.tienda.backend.model.Boleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BoletaRepository extends JpaRepository<Boleta, Long> {
    List<Boleta> findByUsuarioId(Long usuarioId);
    
    List<Boleta> findByFechaHoraBetween(LocalDateTime fechaDesde, LocalDateTime fechaHasta);
    
    @Query("SELECT b FROM Boleta b ORDER BY b.fechaHora DESC")
    List<Boleta> findAllOrderByFechaDesc();
    
    @Query("SELECT b FROM Boleta b WHERE b.usuario.id = :usuarioId ORDER BY b.fechaHora DESC")
    List<Boleta> findByUsuarioIdOrderByFechaDesc(@Param("usuarioId") Long usuarioId);
}

package com.tienda.backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tienda.backend.model.Boleta;

public interface BoletaRepository extends JpaRepository<Boleta, Long> {

    List<Boleta> findByUsuarioIdOrderByFechaHoraDesc(Long usuarioId);

    List<Boleta> findByUsuarioId(Long usuarioId);

    List<Boleta> findByFechaHoraBetween(LocalDateTime fechaDesde, LocalDateTime fechaHasta);

    List<Boleta> findAllByOrderByFechaHoraDesc();
}

package com.tienda.backend.repository;

import com.tienda.backend.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByActivaTrue();
    List<Categoria> findByActivaFalse();
}

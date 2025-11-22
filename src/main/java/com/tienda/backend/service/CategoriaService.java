package com.tienda.backend.service;

import com.tienda.backend.model.Categoria;
import com.tienda.backend.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // CRUD Completo
    public Categoria createCategoria(Categoria categoria) {
        if (categoria.getActiva() == null) {
            categoria.setActiva(true);
        }
        return categoriaRepository.save(categoria);
    }

    public Categoria getCategoriaById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + id));
    }

    public List<Categoria> getAllCategorias() {
        return categoriaRepository.findAll();
    }

    public List<Categoria> getCategoriasActivas() {
        return categoriaRepository.findByActivaTrue();
    }

    public Categoria updateCategoria(Long id, Categoria categoriaActualizada) {
        Categoria categoriaExistente = getCategoriaById(id);
        
        categoriaExistente.setNombre(categoriaActualizada.getNombre());
        categoriaExistente.setDescripcion(categoriaActualizada.getDescripcion());
        
        if (categoriaActualizada.getActiva() != null) {
            categoriaExistente.setActiva(categoriaActualizada.getActiva());
        }
        
        return categoriaRepository.save(categoriaExistente);
    }

    // Soft delete - marca como inactiva
    public void deleteCategoria(Long id) {
        Categoria categoria = getCategoriaById(id);
        categoria.setActiva(false);
        categoriaRepository.save(categoria);
    }
}

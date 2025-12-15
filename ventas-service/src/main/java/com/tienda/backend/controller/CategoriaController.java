package com.tienda.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tienda.backend.dto.CategoriaRequest;
import com.tienda.backend.model.Categoria;
import com.tienda.backend.service.CategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/categorias")
@Tag(name = "Categorías", description = "API de gestión de categorías")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    @Operation(summary = "Crear una nueva categoría")
    public ResponseEntity<Categoria> createCategoria(@RequestBody CategoriaRequest categoriaReq) {
        Categoria categoria = new Categoria();
        categoria.setNombre(categoriaReq.getNombre());
        categoria.setDescripcion(categoriaReq.getDescripcion());
        categoria.setActiva(categoriaReq.getActiva());
        Categoria nuevaCategoria = categoriaService.createCategoria(categoria);
        return new ResponseEntity<>(nuevaCategoria, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID")
    public ResponseEntity<Categoria> getCategoriaById(@PathVariable Long id) {
        try {
            Categoria categoria = categoriaService.getCategoriaById(id);
            return ResponseEntity.ok(categoria);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Obtener todas las categorías")
    public ResponseEntity<List<Categoria>> getAllCategorias() {
        List<Categoria> categorias = categoriaService.getAllCategorias();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/activas")
    @Operation(summary = "Obtener solo las categorías activas")
    public ResponseEntity<List<Categoria>> getCategoriasActivas() {
        List<Categoria> categorias = categoriaService.getCategoriasActivas();
        return ResponseEntity.ok(categorias);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una categoría existente")
    public ResponseEntity<Categoria> updateCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
        try {
            Categoria categoriaActualizada = categoriaService.updateCategoria(id, categoria);
            return ResponseEntity.ok(categoriaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una categoría (soft delete)")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        try {
            categoriaService.deleteCategoria(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

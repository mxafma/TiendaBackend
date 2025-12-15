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

import com.tienda.backend.model.DetalleBoleta;
import com.tienda.backend.service.DetalleBoletaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/detalles-boleta")
@Tag(name = "Detalles de Boleta", description = "API de gestión de detalles de boleta")
public class DetalleBoletaController {

    private final DetalleBoletaService detalleBoletaService;

    public DetalleBoletaController(DetalleBoletaService detalleBoletaService) {
        this.detalleBoletaService = detalleBoletaService;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo detalle de boleta")
    public ResponseEntity<DetalleBoleta> createDetalle(@RequestBody DetalleBoleta detalle) {
        try {
            DetalleBoleta nuevoDetalle = detalleBoletaService.createDetalle(detalle);
            return new ResponseEntity<>(nuevoDetalle, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle de boleta por ID")
    public ResponseEntity<DetalleBoleta> getDetalleById(@PathVariable Long id) {
        try {
            DetalleBoleta detalle = detalleBoletaService.getDetalleById(id);
            return ResponseEntity.ok(detalle);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Obtener todos los detalles de boleta")
    public ResponseEntity<List<DetalleBoleta>> getAllDetalles() {
        List<DetalleBoleta> detalles = detalleBoletaService.getAllDetalles();
        return ResponseEntity.ok(detalles);
    }

    @GetMapping("/boleta/{boletaId}")
    @Operation(summary = "Obtener detalles por boleta")
    public ResponseEntity<List<DetalleBoleta>> getDetallesByBoleta(@PathVariable Long boletaId) {
        List<DetalleBoleta> detalles = detalleBoletaService.getDetallesByBoleta(boletaId);
        return ResponseEntity.ok(detalles);
    }

    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Obtener detalles por producto")
    public ResponseEntity<List<DetalleBoleta>> getDetallesByProducto(@PathVariable Long productoId) {
        List<DetalleBoleta> detalles = detalleBoletaService.getDetallesByProducto(productoId);
        return ResponseEntity.ok(detalles);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un detalle de boleta existente")
    public ResponseEntity<DetalleBoleta> updateDetalle(@PathVariable Long id, @RequestBody DetalleBoleta detalle) {
        try {
            DetalleBoleta detalleActualizado = detalleBoletaService.updateDetalle(id, detalle);
            return ResponseEntity.ok(detalleActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un detalle de boleta")
    public ResponseEntity<Void> deleteDetalle(@PathVariable Long id) {
        try {
            detalleBoletaService.deleteDetalle(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

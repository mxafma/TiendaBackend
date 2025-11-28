package com.tienda.backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tienda.backend.model.Boleta;
import com.tienda.backend.service.BoletaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/boletas")
@CrossOrigin(origins = "*")
@Tag(name = "Boletas", description = "API de gestión de boletas de venta")
public class BoletaController {

    private final BoletaService boletaService;

    public BoletaController(BoletaService boletaService) {
        this.boletaService = boletaService;
    }

    @PostMapping
    @Operation(summary = "Crear una nueva boleta con sus detalles")
    public ResponseEntity<Boleta> createBoleta(@RequestBody Boleta boleta) {
        try {
            Boleta nuevaBoleta = boletaService.createBoleta(boleta);
            return new ResponseEntity<>(nuevaBoleta, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener boleta por ID")
    public ResponseEntity<Boleta> getBoletaById(@PathVariable Long id) {
        try {
            Boleta boleta = boletaService.getBoletaById(id);
            return ResponseEntity.ok(boleta);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Obtener todas las boletas con filtros opcionales")
    public ResponseEntity<List<Boleta>> getAllBoletas(
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        List<Boleta> boletas;

        if (usuarioId != null) {
            boletas = boletaService.getBoletasByUsuario(usuarioId);
        } else if (desde != null && hasta != null) {
            boletas = boletaService.getBoletasByFecha(desde, hasta);
        } else {
            boletas = boletaService.getAllBoletas();
        }

        return ResponseEntity.ok(boletas);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una boleta (usar con precaución)")
    public ResponseEntity<Boleta> updateBoleta(@PathVariable Long id, @RequestBody Boleta boleta) {
        try {
            Boleta boletaActualizada = boletaService.updateBoleta(id, boleta);
            return ResponseEntity.ok(boletaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Anular/eliminar una boleta (devuelve stock)")
    public ResponseEntity<Void> deleteBoleta(@PathVariable Long id) {
        try {
            boletaService.deleteBoleta(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

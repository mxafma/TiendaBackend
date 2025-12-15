// controller/AuthController.java
package com.tienda.backend.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tienda.backend.dto.AuthResponse;
import com.tienda.backend.dto.LoginRequest;
import com.tienda.backend.dto.UsuarioRequest;
import com.tienda.backend.dto.UsuarioResponse;
import com.tienda.backend.model.Usuario;
import com.tienda.backend.security.JwtService;
import com.tienda.backend.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "API de autenticación de usuarios")
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioService usuarioService, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión con email y contraseña")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        Optional<Usuario> user = usuarioService.login(request.getEmail(), request.getPassword());
        
        if (user.isPresent()) {
            Usuario u = user.get();
            String token = jwtService.generateToken(u.getId(), u.getEmail(), u.getRol());
            UsuarioResponse usuarioResponse = mapToResponse(u);
            AuthResponse response = new AuthResponse("Login exitoso", token, usuarioResponse);
            return ResponseEntity.ok(response);
        } else {
            AuthResponse response = new AuthResponse("Usuario o contraseña incorrectos", null, null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar un nuevo usuario")
    public ResponseEntity<UsuarioResponse> register(@RequestBody UsuarioRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());
        // Hash the password before saving
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            usuario.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }
        usuario.setRol(request.getRol() != null ? request.getRol() : "CLIENTE");
        usuario.setActivo(request.getActivo() != null ? request.getActivo() : true);

        Usuario nuevoUsuario = usuarioService.createUsuario(usuario);
        return new ResponseEntity<>(mapToResponse(nuevoUsuario), HttpStatus.CREATED);
    }

    private UsuarioResponse mapToResponse(Usuario u) {
        UsuarioResponse dto = new UsuarioResponse();
        dto.setId(u.getId());
        dto.setNombre(u.getNombre());
        dto.setApellido(u.getApellido());
        dto.setEmail(u.getEmail());
        dto.setRol(u.getRol());
        dto.setActivo(u.getActivo());
        dto.setCreadoEn(u.getCreadoEn());
        return dto;
    }
}

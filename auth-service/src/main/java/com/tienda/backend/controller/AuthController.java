// controller/AuthController.java
package com.tienda.backend.controller;

import com.tienda.backend.dto.AuthResponse;
import com.tienda.backend.dto.LoginRequest;
import com.tienda.backend.model.Usuario;
import com.tienda.backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Autenticación", description = "API de autenticación de usuarios")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión con email y contraseña")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        Optional<Usuario> user = usuarioService.login(request.getEmail(), request.getPassword());
        
        if (user.isPresent()) {
            AuthResponse response = new AuthResponse("Login exitoso", user.get());
            return ResponseEntity.ok(response);
        } else {
            AuthResponse response = new AuthResponse("Usuario o contraseña incorrectos", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar un nuevo usuario")
    public ResponseEntity<Usuario> register(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.createUsuario(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }
}

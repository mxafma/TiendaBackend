// controller/AuthController.java
package com.tienda.backend.controller;

import com.tienda.backend.dto.AuthResponse;
import com.tienda.backend.dto.LoginRequest;
import com.tienda.backend.dto.UsuarioResponse;
import com.tienda.backend.model.Usuario;
import com.tienda.backend.service.UsuarioService;
import com.tienda.backend.util.JwtUtil;
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
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioService usuarioService, JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión con email y contraseña")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        Optional<Usuario> user = usuarioService.login(request.getEmail(), request.getPassword());
        
        if (user.isPresent()) {
            Usuario u = user.get();
            // map to UsuarioResponse to avoid exposing passwordHash
            UsuarioResponse ur = new UsuarioResponse();
            ur.setId(u.getId());
            ur.setNombre(u.getNombre());
            ur.setApellido(u.getApellido());
            ur.setEmail(u.getEmail());
            ur.setRol(u.getRol());
            ur.setActivo(u.getActivo());
            ur.setCreadoEn(u.getCreadoEn());
            ur.setActualizadoEn(u.getActualizadoEn());

            String token = jwtUtil.generateToken(u);

            AuthResponse response = new AuthResponse("Login exitoso", ur, token);
            return ResponseEntity.ok(response);
        } else {
            AuthResponse response = new AuthResponse("Usuario o contraseña incorrectos", null, null);
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

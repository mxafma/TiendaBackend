// service/UsuarioService.java
package com.tienda.backend.service;

import com.tienda.backend.model.Usuario;
import com.tienda.backend.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Método de login: compara password plano con hash
    public Optional<Usuario> login(String email, String password) {
        return usuarioRepository.findByEmail(email)
                .filter(user -> user.getActivo() && password != null && passwordEncoder.matches(password, user.getPasswordHash()));
    }

    // CRUD Completo
    public Usuario createUsuario(Usuario usuario) {
        if (usuario.getActivo() == null) {
            usuario.setActivo(true);
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario getUsuarioById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario updateUsuario(Long id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = getUsuarioById(id);

        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setApellido(usuarioActualizado.getApellido());
        usuarioExistente.setEmail(usuarioActualizado.getEmail());

        if (usuarioActualizado.getPasswordHash() != null && !usuarioActualizado.getPasswordHash().isEmpty()) {
            usuarioExistente.setPasswordHash(usuarioActualizado.getPasswordHash());
        }

        usuarioExistente.setRol(usuarioActualizado.getRol());

        if (usuarioActualizado.getActivo() != null) {
            usuarioExistente.setActivo(usuarioActualizado.getActivo());
        }

        return usuarioRepository.save(usuarioExistente);
    }

    // Soft delete - marca como inactivo
    public void deleteUsuario(Long id) {
        Usuario usuario = getUsuarioById(id);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }
}

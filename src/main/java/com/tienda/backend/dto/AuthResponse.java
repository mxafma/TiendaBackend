package com.tienda.backend.dto;

import com.tienda.backend.model.Usuario;

public class AuthResponse {
    private String message;
    private Usuario usuario;

    public AuthResponse() {
    }

    public AuthResponse(String message, Usuario usuario) {
        this.message = message;
        this.usuario = usuario;
    }

    // Getters y Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}

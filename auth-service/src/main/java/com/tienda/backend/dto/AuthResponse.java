package com.tienda.backend.dto;

public class AuthResponse {
    private String message;
    private UsuarioResponse usuario;
    private String token;

    public AuthResponse() {
    }

    public AuthResponse(String message, UsuarioResponse usuario, String token) {
        this.message = message;
        this.usuario = usuario;
        this.token = token;
    }

    // Getters y Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UsuarioResponse getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioResponse usuario) {
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

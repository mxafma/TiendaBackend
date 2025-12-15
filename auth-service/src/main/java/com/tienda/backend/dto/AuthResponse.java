package com.tienda.backend.dto;

public class AuthResponse {
    private String message;
    private String accessToken;
    private UsuarioResponse usuario;

    public AuthResponse() {
    }

    public AuthResponse(String message, String accessToken, UsuarioResponse usuario) {
        this.message = message;
        this.accessToken = accessToken;
        this.usuario = usuario;
    }

    // Getters y Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public UsuarioResponse getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioResponse usuario) {
        this.usuario = usuario;
    }
}

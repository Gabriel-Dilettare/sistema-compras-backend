package com.dilettare.setorCompras.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados da autenticação")
public class AuthDTO {

    @Schema(description = "Token JWT gerado após autenticação bem-sucedida", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Nome de usuário autenticado", example = "usuarioTeste")
    private String username;

    public AuthDTO() {
    }

    public AuthDTO(String token, String username) {
        this.token = token;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
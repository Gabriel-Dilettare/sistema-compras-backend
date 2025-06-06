package com.dilettare.setorCompras.response;

import com.dilettare.setorCompras.dto.ApiResponseDTO;
import com.dilettare.setorCompras.dto.AuthDTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta da API para autenticação")
public class AuthResponse extends ApiResponseDTO<AuthDTO> {

    public AuthResponse(AuthDTO result) {
        super(result);
    }

    public AuthResponse(AuthDTO result, boolean success, String message) {
        super(result, success, message);
    }
}

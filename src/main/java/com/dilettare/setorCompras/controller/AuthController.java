package com.dilettare.setorCompras.controller;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dilettare.setorCompras.bridge.APIContext;
import com.dilettare.setorCompras.dto.AuthDTO;
import com.dilettare.setorCompras.response.AuthResponse;
import com.dilettare.setorCompras.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final APIContext apiContext;
    private final AuthService authService;

    @Autowired
    public AuthController(APIContext apiContext, AuthService authService) {
        this.apiContext = apiContext;
        this.authService = authService;
    }

    @PostMapping("/auth")
    @Operation(summary = "Autenticar usuário", tags = "Autenticação", description = "Autentica um usuário e retorna um token JWT", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Credenciais do usuário", required = true, content = @Content(mediaType = "application/json",

            examples = @ExampleObject(value = """
                    {
                      "username": "usuarioTeste",
                      "password": "senhaSegura123"
                    }
                    """))), responses = {
                    @ApiResponse(responseCode = "200", description = "Autenticado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class), examples = @ExampleObject(value = """
                            {
                              "result": {
                                "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                                "username": "usuarioTeste"
                              },
                              "success": true,
                              "message": "Autenticado com sucesso"
                            }
                            """))),
                    @ApiResponse(responseCode = "401", description = "Usuário ou senha inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class), examples = @ExampleObject(value = """
                            {
                              "result": null,
                              "success": false,
                              "message": "Usuário ou senha inválidos"
                            }
                            """))),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class), examples = @ExampleObject(value = """
                            {
                              "result": null,
                              "success": false,
                              "message": "Erro interno: detalhes do erro"
                            }
                            """)))
            })
    public ResponseEntity<AuthResponse> login(@RequestBody LinkedHashMap<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        try {
            if (apiContext.initializeUserConnectionPools(username, password)) {
                String token = authService.generateToken(username);
                AuthDTO authData = new AuthDTO(token, username);
                return ResponseEntity.ok(new AuthResponse(authData));
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, false, "Usuário ou senha inválidos"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse(null, false, "Erro interno: " + e.getMessage()));
        }
    }
}

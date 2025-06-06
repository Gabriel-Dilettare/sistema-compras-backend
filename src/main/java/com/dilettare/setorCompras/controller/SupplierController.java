package com.dilettare.setorCompras.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dilettare.setorCompras.bridge.APIContext;
import com.dilettare.setorCompras.dto.SupplierDTO;
import com.dilettare.setorCompras.exception.AuthorizationException;
import com.dilettare.setorCompras.response.SupplierResponse;
import com.dilettare.setorCompras.service.SupplierService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/v1/secure/suppliers")
@CrossOrigin(origins = "*")

public class SupplierController {

        private final APIContext apiContext;

        @Autowired
        public SupplierController(APIContext apiContext) {
                this.apiContext = apiContext;
        }

        @GetMapping
        @Operation(summary = "Lista de fornecedores", tags = "Fornecedores", description = "Retorna uma lista de fornecedores",

        responses = {
        @ApiResponse(responseCode = "200", description = "Lista de fornecedores recuperada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SupplierResponse.class), examples = @ExampleObject(value = """
                        {
                                "result": [
                                {
                                        "codeSupplier": 39175,
                                        "nameSupplier": "Fornecedores",
                                        "socialNameSupplier": "Fornecedores LTDA",
                                        "addressSupplier": "Rua Assis, 123",
                                        "citySupplier": "FOZ DO IGUACU",
                                        "CEPSupplier": "85859-000",
                                        "foneSupplier": "(45) 99999-9999",
                                        "neighborhoodSupplier": "Centro"
                                }
                                ],
                                "success": true,
                                "message": "Operação realizada com sucesso"
                        }
                        """))),
        @ApiResponse(responseCode = "401", description = "Usuário sem conexão ativa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SupplierResponse.class), examples = @ExampleObject(value = """
                        {
                                "result": null,
                                "success": false,
                                "message": "Usuário sem conexão ativa com o banco de dados"
                        }
                        """))),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SupplierResponse.class), examples = @ExampleObject(value = """
                        {
                                "result": null,
                                "success": false,
                                "message": "Erro interno: detalhes do erro"
                        }
                        """)))
        })
        public ResponseEntity<SupplierResponse> geAllSuppliers(Authentication authentication)
                        throws AuthorizationException {
                String username = authentication.getName();

                if (!apiContext.getFirebirdDataSources().containsKey(username)) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                        .body(new SupplierResponse(null, false,
                                                        "Usuário sem conexão ativa com o banco de dados"));
                }

                try {
                        SupplierService supplierService = new SupplierService(
                                        apiContext, username);
                        List<SupplierDTO> result = supplierService.getAllSuppliers();
                        return ResponseEntity.ok(new SupplierResponse(result));
                } catch (SQLException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(new SupplierResponse(null, false, "Erro interno: " + e.getMessage()));
                }
        }

}

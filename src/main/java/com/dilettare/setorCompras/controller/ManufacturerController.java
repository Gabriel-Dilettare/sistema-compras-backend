package com.dilettare.setorCompras.controller;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dilettare.setorCompras.bridge.APIContext;
import com.dilettare.setorCompras.dto.ManufacturerDTO;
import com.dilettare.setorCompras.exception.AuthorizationException;
import com.dilettare.setorCompras.response.ManufacturerResponse;
import com.dilettare.setorCompras.service.ManufacturerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/v1/secure/manufacturers")
@CrossOrigin(origins = "*")
public class ManufacturerController {

  private final APIContext apiContext;

  @Autowired
  public ManufacturerController(APIContext apiContext) {
    this.apiContext = apiContext;
  }

  @GetMapping
  @Operation(summary = "Buscar Fabricantes por Comprador", tags = "Fabricantes", description = "Retorna uma lista de fabricantes associados ao comprador autenticado", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cargo do usuario autenticado", required = true, content = @Content(mediaType = "application/json",

      examples = @ExampleObject(value = """
          {
            "role": "COMPRADOR/ADMIN"
          }
          """))), responses = {
          @ApiResponse(responseCode = "200", description = "Lista de fabricantes recuperada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ManufacturerResponse.class), examples = @ExampleObject(value = """
              {
                "result": [
                  { "name": "Fabricante XY" },
                  { "name": "Fabricante YXZ" }
                ],
                "success": true,
                "message": "Operação realizada com sucesso"
              }
              """))),
          @ApiResponse(responseCode = "401", description = "Usuário sem conexão ativa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ManufacturerResponse.class), examples = @ExampleObject(value = """
              {
                "result": null,
                "success": false,
                "message": "Usuário sem conexão ativa com o banco de dados"
              }
              """))),
          @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ManufacturerResponse.class), examples = @ExampleObject(value = """
              {
                "result": null,
                "success": false,
                "message": "Erro interno: detalhes do erro"
              }
              """)))
      })
  public ResponseEntity<ManufacturerResponse> getManufacturers(@RequestBody LinkedHashMap<String, String> role,
      Authentication authentication)
      throws AuthorizationException {
    String username = authentication.getName();
    String userRole = role.get("role");

    if (!apiContext.getFirebirdDataSources().containsKey(username)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new ManufacturerResponse(null, false, "Usuário sem conexão ativa com o banco de dados"));
    }

    try {
      ManufacturerService manufacturerService = new ManufacturerService(
          apiContext, username);
      List<ManufacturerDTO> result = manufacturerService.getManufacturersByBuyer(username, userRole);
      return ResponseEntity.ok(new ManufacturerResponse(result));
    } catch (SQLException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ManufacturerResponse(null, false, "Erro interno: " + e.getMessage()));
    }
  }
}
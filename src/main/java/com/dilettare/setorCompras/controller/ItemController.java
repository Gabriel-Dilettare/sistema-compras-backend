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
import com.dilettare.setorCompras.dto.ItemManufacturerDTO;
import com.dilettare.setorCompras.exception.AuthorizationException;
import com.dilettare.setorCompras.response.ItemResponse;
import com.dilettare.setorCompras.service.ItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/v1/secure/itemsManufacturer")
@CrossOrigin(origins = "*")

public class ItemController {

        private final APIContext apiContext;

        @Autowired
        public ItemController(APIContext apiContext) {
                this.apiContext = apiContext;
        }

        @GetMapping
        @Operation(summary = "Lista de itens dos fabricantes", tags = "Itens fabricante", description = "Retorna uma lista de itens por fabricante", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Fabricantes para pesquisa", required = true, content = @Content(mediaType = "application/json",

        examples = @ExampleObject(value = """
        {
                "selectedManufacturers": ["Fabricante", "Fabricante XY", "Fabricante YXZ"]
        }"""))), 

        responses = {
        @ApiResponse(responseCode = "200", description = "Lista de itens dos fabricantes", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemResponse.class), examples = @ExampleObject(value = """
        {
        "result": [
        {
                "manufacturer": {
                        "name": "Fabricante XY",
                        "deliveryTime": 0,
                        "replacementDays": 0
                },
                "itens": [
                {
                        "codeItem": 0,
                        "packagingItem": 0,
                        "weightMeasureItem": "0",
                        "descriptionItem": "Item XY",
                        "barcodeItem": "00000000000",
                        "inventory": [
                                {
                                        "codeStore": 5,
                                        "quantityItemStock": 0,
                                        "averageItemCost": 0,
                                        "itemPriceTax": 0,
                                        "quantityItemPending": 0
                                },
                                {
                                        "codeStore": 9,
                                        "quantityItemStock": 0,
                                        "averageItemCost": 0,
                                        "itemPriceTax": 0,
                                        "quantityItemPending": 0
                                },
                                {
                                        "codeStore": 11,
                                        "quantityItemStock": 0,
                                        "averageItemCost": 0,
                                        "itemPriceTax": 0,
                                        "quantityItemPending": 0
                                },
                                {
                                        "codeStore": 15,
                                        "quantityItemStock": 0,
                                        "averageItemCost": 0,
                                        "itemPriceTax": 0,
                                        "quantityItemPending": 0
                                },
                                {
                                        "codeStore": 17,
                                        "quantityItemStock": 0,
                                        "averageItemCost": 0,
                                        "itemPriceTax": 0,
                                        "quantityItemPending": 0
                                }
                        ]
                }]
        }],
        "success": true,
        "message": "Operação realizada com sucesso"
        }"""))),
        @ApiResponse(responseCode = "401", description = "Usuário sem conexão ativa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemResponse.class), examples = @ExampleObject(value = """
                        {
                                "result": null,
                                "success": false,
                                "message": "Usuário sem conexão ativa com o banco de dados"
                        }
                        """))),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemResponse.class), examples = @ExampleObject(value = """
                        {
                                "result": null,
                                "success": false,
                                "message": "Erro interno: detalhes do erro"
                        }
                        """)))
        })
        public ResponseEntity<ItemResponse> getAllItemForManufacturer(Authentication authentication,
                        @RequestBody LinkedHashMap<String, List<String>> manufacturerList)
                        throws AuthorizationException {
                String username = authentication.getName();

                if (!apiContext.getFirebirdDataSources().containsKey(username)) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                        .body(new ItemResponse(null, false,
                                                        "Usuário sem conexão ativa com o banco de dados"));
                }

                try {
                        ItemService itemService = new ItemService(
                                        apiContext, username);
                        List<String> manufacturers = manufacturerList.get("selectedManufacturers");
                        List<ItemManufacturerDTO> result = itemService.getAllItemForManufacturer(manufacturers);
                        return ResponseEntity.ok(new ItemResponse(result));
                } catch (SQLException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(new ItemResponse(null, false, "Erro interno: " + e.getMessage()));
                }
        }

}

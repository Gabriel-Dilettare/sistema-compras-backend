package com.dilettare.setorCompras.response;

import java.util.List;

import com.dilettare.setorCompras.dto.ApiResponseDTO;
import com.dilettare.setorCompras.dto.ItemManufacturerDTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta da API contendo lista de itens por fornecedor")
public class ItemResponse extends ApiResponseDTO<List<ItemManufacturerDTO>> {
    public ItemResponse(List<ItemManufacturerDTO> result) {
        super(result);
    }

    public ItemResponse(List<ItemManufacturerDTO> result, boolean success, String message) {
        super(result, success, message);
    }
}

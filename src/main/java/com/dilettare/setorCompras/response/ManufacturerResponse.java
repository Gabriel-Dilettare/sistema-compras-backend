package com.dilettare.setorCompras.response;

import java.util.List;

import com.dilettare.setorCompras.dto.ApiResponseDTO;
import com.dilettare.setorCompras.dto.ManufacturerDTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta da API contendo uma lista de fabricantes")
public class ManufacturerResponse extends ApiResponseDTO<List<ManufacturerDTO>> {
    public ManufacturerResponse(List<ManufacturerDTO> result) {
        super(result);
    }

    public ManufacturerResponse(List<ManufacturerDTO> result, boolean success, String message) {
        super(result, success, message);
    }
}

package com.dilettare.setorCompras.response;

import java.util.List;

import com.dilettare.setorCompras.dto.ApiResponseDTO;
import com.dilettare.setorCompras.dto.SupplierDTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta da API contendo uma lista de fornecedores")
public class SupplierResponse extends ApiResponseDTO<List<SupplierDTO>> {
    public SupplierResponse(List<SupplierDTO> result) {
        super(result);
    }

    public SupplierResponse(List<SupplierDTO> result, boolean success, String message) {
        super(result, success, message);
    }
}

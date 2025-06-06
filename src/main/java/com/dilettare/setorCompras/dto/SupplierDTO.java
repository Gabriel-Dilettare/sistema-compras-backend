package com.dilettare.setorCompras.dto;

import com.dilettare.setorCompras.model.Supplier;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados dos fornecedores")
public class SupplierDTO {

    @Schema(description = "Código", example = "12345")
    public Integer codeSupplier;
    @Schema(description = "Nome", example = "Forncecedor XYZ")
    public String nameSupplier;
    @Schema(description = "Nome social", example = "Forncecedor ZZZ")
    public String socialNameSupplier;
    @Schema(description = "Endereço", example = "Rua: Assis, 123")
    public String addressSupplier;
    @Schema(description = "Cidade", example = "Foz do Iguaçu")
    public String citySupplier;
    @Schema(description = "CEP", example = "85859-000")
    public String CEPSupplier;
    @Schema(description = "Telefone", example = "(45) 99999-9999")
    public String foneSupplier;
    @Schema(description = "Bairro", example = "Centro")
    public String neighborhoodSupplier;

    public SupplierDTO(Supplier supplier) {
        this.codeSupplier = supplier.getCodeSupplier();
        this.nameSupplier = supplier.getNameSupplier();
        this.socialNameSupplier = supplier.getSocialNameSupplier();
        this.addressSupplier = supplier.getAddressSupplier();
        this.citySupplier = supplier.getCitySupplier();
        this.CEPSupplier = supplier.getCEPSupplier();
        this.foneSupplier = supplier.getFoneSupplier();
        this.neighborhoodSupplier = supplier.getNeighborhoodSupplier();
    }

}

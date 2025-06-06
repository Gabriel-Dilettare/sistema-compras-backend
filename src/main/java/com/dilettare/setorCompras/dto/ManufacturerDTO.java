package com.dilettare.setorCompras.dto;



import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Dados dos fabricantes")
public class ManufacturerDTO {
     @Schema(description = "Fabricante", example = "Fabricante XYZ")
    public String name;

    @Schema(description = "Prazo de entrega", example = "7")
    public Integer deliveryTime;

    @Schema(description = "Dias reposição", example = "15")
    public Integer replacementDays;

    public ManufacturerDTO() {
    }

    public ManufacturerDTO(String name) {
        this.name = name;
    }

    public ManufacturerDTO(String name, Integer deliveryTime, Integer replacementDays) {
        this.name = name;
        this.deliveryTime = deliveryTime;
        this.replacementDays = replacementDays;
    }
}

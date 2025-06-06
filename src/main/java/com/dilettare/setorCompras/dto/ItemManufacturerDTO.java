package com.dilettare.setorCompras.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class ItemManufacturerDTO {
    @Schema(description = "Fabricante", example = "Fabricante XYZ")
    public ManufacturerDTO manufacturer;

    @Schema(description = "Itens do fabricante")
    public List<ItemInventoryDTO> itens;

    public ItemManufacturerDTO(String manufacturer, Integer deliveryTime, Integer replacementDays, List<ItemInventoryDTO> itens) {
        this.manufacturer = new ManufacturerDTO(manufacturer, deliveryTime, replacementDays);
        this.itens = itens;
    }
}

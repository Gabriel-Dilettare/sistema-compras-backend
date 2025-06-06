package com.dilettare.setorCompras.dto;

import java.util.List;

import com.dilettare.setorCompras.model.Item;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({
    "codeItem",
    "packagingItem",
    "weightMeasureItem",
    "descriptionItem",
    "barcodeItem",
    "store"
})

public class ItemInventoryDTO {
    @Schema(description = "Código", example = "12345")
    public Integer codeItem;
    @Schema(description = "Embalagem", example = "Caixa")
    public Integer packagingItem;
    @Schema(description = "Peso ou Medida", example = "1.5kg")
    public String weightMeasureItem;
    @Schema(description = "Descrição", example = "Produto de limpeza XYZ")
    public String descriptionItem;
    @Schema(description = "Código de barras", example = "1234567890123")
    public String barcodeItem;
    @Schema(description = "Lista de lojas com estoque do item")
    public List<InventoryDTO> inventory;

    public ItemInventoryDTO(Item item, List<InventoryDTO> inventory) {
        this.codeItem = item.getCodeItem();
        this.packagingItem = item.getPackagingItem();
        this.weightMeasureItem = item.getWeightMeasureItem();
        this.descriptionItem = item.getDescriptionItem();
        this.barcodeItem = item.getBarcodeItem();
        this.inventory = inventory;
    }
}
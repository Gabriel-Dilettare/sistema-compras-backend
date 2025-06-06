package com.dilettare.setorCompras.model;

public class Inventory {
    private Integer codeItem;
    private Double quantityItemStock;
    private Double averageItemCost;
    private Double itemPriceTax;
    private Integer codeStore;
    private Double quantityItemPending;

    public Integer getCodeItem() {
        return this.codeItem;
    }

    public void setCodeItem(Integer codeItem) {
        this.codeItem = codeItem;
    }

    public Double getQuantityItemStock() {
        return this.quantityItemStock;
    }

    public void setQuantityItemStock(Double quantityItemStock) {
        this.quantityItemStock = quantityItemStock;
    }

    public Double getAverageItemCost() {
        return this.averageItemCost;
    }

    public void setAverageItemCost(Double averageItemCost) {
        this.averageItemCost = averageItemCost;
    }

    public Double getItemPriceTax() {
        return this.itemPriceTax;
    }

    public void setItemPriceTax(Double itemPriceTax) {
        this.itemPriceTax = itemPriceTax;
    }

    public Integer getCodeStore() {
        return this.codeStore;
    }

    public void setCodeStore(Integer codeStore) {
        this.codeStore = codeStore;
    }

    public Double getQuantityItemPending() {
        return this.quantityItemPending;
    }

    public void setQuantityItemPending(Double quantityItemPending) {
        this.quantityItemPending = quantityItemPending;
    }
}

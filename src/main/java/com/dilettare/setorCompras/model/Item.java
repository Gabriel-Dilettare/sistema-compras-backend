package com.dilettare.setorCompras.model;

public class Item {
    private Integer codeItem;
    private String descriptionItem;
    private String barcodeItem;
    private String manufacturerItem;
    private Integer packagingItem;
    private String weightMeasureItem;

    public Item(Integer codeItem) {
        this.codeItem = codeItem;
    }

    public Integer getCodeItem() {
        return this.codeItem;
    }

    public void setCodeItem(Integer codeItem) {
        this.codeItem = codeItem;
    }

    public String getDescriptionItem() {
        return this.descriptionItem;
    }

    public void setDescriptionItem(String descriptionItem) {
        this.descriptionItem = descriptionItem;
    }

    public String getBarcodeItem() {
        return this.barcodeItem;
    }

    public void setBarcodeItem(String barcodeItem) {
        this.barcodeItem = barcodeItem;
    }

    public String getManufacturerItem() {
        return this.manufacturerItem;
    }

    public void setManufacturerItem(String manufacturerItem) {
        this.manufacturerItem = manufacturerItem;
    }

    public Integer getPackagingItem() {
        return this.packagingItem;
    }

    public void setPackagingItem(Integer packagingItem) {
        this.packagingItem = packagingItem;
    }

    public String getWeightMeasureItem() {
        return this.weightMeasureItem;
    }

    public void setWeightMeasureItem(String weightMeasureItem) {
        this.weightMeasureItem = weightMeasureItem;
    }
    
}

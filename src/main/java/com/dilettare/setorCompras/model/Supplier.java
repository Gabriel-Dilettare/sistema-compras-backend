package com.dilettare.setorCompras.model;

public class Supplier {
    private Integer codeSupplier;
    private String nameSupplier;
    private String socialNameSupplier;
    private String addressSupplier;
    private String citySupplier;
    private String CEPSupplier;
    private String foneSupplier;
    private String neighborhoodSupplier;

    public Supplier(Integer codeSupplier) {
        this.codeSupplier = codeSupplier;
    }

    public Integer getCodeSupplier() {
        return this.codeSupplier;
    }

    public void setCodeSupplier(Integer codeSupplier) {
        this.codeSupplier = codeSupplier;
    }

    public String getNameSupplier() {
        return this.nameSupplier;
    }

    public void setNameSupplier(String nameSupplier) {
        this.nameSupplier = nameSupplier;
    }

    public String getSocialNameSupplier() {
        return this.socialNameSupplier;
    }

    public void setSocialNameSupplier(String socialNameSupplier) {
        this.socialNameSupplier = socialNameSupplier;
    }

    public String getAddressSupplier() {
        return this.addressSupplier;
    }

    public void setAddressSupplier(String addressSupplier) {
        this.addressSupplier = addressSupplier;
    }

    public String getCitySupplier() {
        return this.citySupplier;
    }

    public void setCitySupplier(String citySupplier) {
        this.citySupplier = citySupplier;
    }

    public String getCEPSupplier() {
        return this.CEPSupplier;
    }

    public void setCEPSupplier(String CEPSupplier) {
        this.CEPSupplier = CEPSupplier;
    }

    public String getFoneSupplier() {
        return this.foneSupplier;
    }

    public void setFoneSupplier(String foneSupplier) {
        this.foneSupplier = foneSupplier;
    }

    public String getNeighborhoodSupplier() {
        return this.neighborhoodSupplier;
    }

    public void setNeighborhoodSupplier(String neighborhoodSupplier) {
        this.neighborhoodSupplier = neighborhoodSupplier;
    }
}

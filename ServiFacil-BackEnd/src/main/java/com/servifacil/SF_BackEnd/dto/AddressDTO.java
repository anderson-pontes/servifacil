package com.servifacil.SF_BackEnd.dto;

public class AddressDTO {
    private String zipCode;
    private String street;
    private String houseNumber;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;

    // Construtor padrão
    public AddressDTO() {
    }

    // Construtor com todos os campos
    public AddressDTO(String zipCode, String street, String houseNumber,
                      String complement, String neighborhood, String city,
                      String state) {
        this.zipCode = zipCode;
        this.street = street;
        this.houseNumber = houseNumber;
        this.complement = complement;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
    }

    // GETTERS E SETTERS
    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getHouseNumber() { return houseNumber; }
    public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }

    public String getComplement() { return complement; }
    public void setComplement(String complement) { this.complement = complement; }

    public String getNeighborhood() { return neighborhood; }
    public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
}

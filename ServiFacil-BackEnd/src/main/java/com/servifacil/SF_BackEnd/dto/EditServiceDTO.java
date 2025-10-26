package com.servifacil.SF_BackEnd.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class EditServiceDTO {

    @Column(name = "Title")
    @Size(min = 2, max = 125, message = "Título deve conter entre 2 e 125 caractéres")
    private String title;

    @Column(precision = 6, scale = 2, name = "Price")
    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
    @Digits(integer = 6, fraction = 2, message = "O preço deve ter no máximo 6 dígitos inteiros e 2 decimais")
    private BigDecimal price;

    @Column(name = "Details")
    @Size(min = 25, max = 250, message = "Os detalhes devem conter entre 25 e 250 caractéres!")
    private String details;

    @Column(name = "Service_Status")
    private String serviceStatus = "Ativo";

    @Size(min = 2, max = 80, message = "Categoria deve conter entre 2 e 80 caractéres!")
    private String category;

    // GETTERS e SETTERS

    public String getTitle(){ return this.title; }
    public void setTitle(String title){ this.title = title; }

    public BigDecimal getPrice(){ return this.price; }
    public void setPrice(BigDecimal price){ this.price = price; }

    public String getDetails(){ return this.details; }
    public void setDetails(String details){ this.details = details; }

    public String getCategory(){ return this.category; }
    public void setCategory(String category){ this.category = category; }

    public String getServiceStatus(){ return this.serviceStatus; }
    public void setServiceStatus(String serviceStatus){ this.serviceStatus = serviceStatus; }
}

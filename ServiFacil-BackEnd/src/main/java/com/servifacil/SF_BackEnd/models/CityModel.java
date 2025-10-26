package com.servifacil.SF_BackEnd.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_cities")
public class CityModel {

    @Id
    @Column(name = "City_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int cityID;

    @Column(name = "City")
    @NotBlank(message = "Cidade é obrigatório!")
    @Size(min = 2, max = 80, message = "Cidade deve conter entre 2 e 80 caractéres!")
    private String city;

    public int getCityID(){ return cityID; }
    public void setCityID(int cityID){ this.cityID = cityID; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
}

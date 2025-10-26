package com.servifacil.SF_BackEnd.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_neighborhoods")
public class NeighborhoodModel {

    @Id
    @Column(name = "Neighborhood_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int neighborhoodID;

    @Column(name = "Neighborhood")
    @NotBlank(message = "Bairro é obrigatório!")
    @Size(min = 2, max = 80, message = "Bairro deve conter entre 2 e 80 caractéres!")
    private String neighborhood;

    public int getNeighborhoodID(){ return neighborhoodID; }
    public void setNeighborhoodID(int neighborhoodID){ this.neighborhoodID = neighborhoodID; }

    public String getNeighborhood() { return neighborhood; }
    public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }
}

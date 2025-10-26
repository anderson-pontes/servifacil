package com.servifacil.SF_BackEnd.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tb_addresses")
public class AddressModel {

    @Id
    @Column(name = "Address_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int addressID;

    @Column(name = "Zip_Code")
    @NotBlank
    @Size(min = 9, max = 10, message = "CEP deve conter no mínmo 9 e no máximo 10 caractéres!")
    private String zipCode;

    @Column(name = "Street")
    @NotBlank
    @Size(min = 2, max = 100, message = "Nome da rua deve conter entre 2 e 100 caractéres!")
    private String street;

    @Column(name = "House_Number")
    @NotBlank
    @Size(min = 1, max = 10, message = "Número da casa deve conter entre 1 e 10 caractéres!")
    private String houseNumber;

    @Column(name = "Complement")
    @Size(min = 1, max = 25, message = "Complemento deve conter entre 1 e 25 caractéres!")
    private String complement;

    @OneToOne
    @JoinColumn(name = "Neighborhood_ID")
    private NeighborhoodModel neighborhoodID;

    @OneToOne
    @JoinColumn(name = "City_ID")
    private CityModel cityID;

    @OneToOne
    @JoinColumn(name = "State_ID")
    private StateModel stateID;

    @Column(name = "Created_At")
    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime createdAt;

//    @OneToOne(mappedBy = "address")
//    private UserModel user;

    // GETTERS e SETTERS

    public int getAddressID(){ return addressID; }

    public String getZipCode(){ return zipCode; }
    public void setZipCode(String zipCode){ this.zipCode = zipCode; }

    public String getStreet(){ return street; }
    public void setStreet(String street){ this.street = street; }

    public String getHouseNumber(){ return houseNumber; }
    public void setHouseNumber(String houseNumber){ this.houseNumber = houseNumber; }

    public String getComplement(){ return complement; }
    public void setComplement(String complement){ this.complement = complement; }

    public StateModel getStateID(){ return stateID; }
    public void setStateID(StateModel stateID){ this.stateID = stateID; }

    public CityModel getCityID(){ return cityID; }
    public void setCityID(CityModel cityID){ this.cityID = cityID; }

    public NeighborhoodModel getNeighborhoodID(){ return neighborhoodID; }
    public void setNeighborhoodID(NeighborhoodModel neighborhoodID){ this.neighborhoodID = neighborhoodID; }
}

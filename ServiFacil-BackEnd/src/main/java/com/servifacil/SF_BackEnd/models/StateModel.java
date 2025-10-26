package com.servifacil.SF_BackEnd.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_states")
public class StateModel {

    @Id
    @Column(name = "State_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int stateID;

    @Column(name = "State")
    @NotBlank(message = "Estado é obrigatório!")
    @Size(min = 2, max = 2, message = "Estado deve conter 2 caractéres!")
    private String state;

    public int getStateID(){ return stateID; }
    public void setStateID(int stateID){ this.stateID = stateID; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
}

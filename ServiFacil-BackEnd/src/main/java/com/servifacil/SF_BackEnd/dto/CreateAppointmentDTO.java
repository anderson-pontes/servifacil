package com.servifacil.SF_BackEnd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class CreateAppointmentDTO {


//    private ServiceModel service;
//
//    private UserModel client;

    @Column(name = "Start_Date")
    @NotNull(message = "Data e hora de início são obrigatórias!")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
    @Future(message = "Data de início deve ser futura!")
    private LocalDateTime startDate;

    @Column(name = "End_Date")
    @NotNull(message = "Data e hora de término é obrigatórias!")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
    @Future(message = "Data de término deve ser futura!")
    private LocalDateTime endDate;

    // GETTERS & SETTERS

//    public ServiceModel getService(){ return this.service; }
//    public void setService(ServiceModel service){ this.service = service; }
//
//    public UserModel getClient(){ return this.client; }
//    public void setClient(UserModel client){ this.client = client; }

    public LocalDateTime getStartDate(){ return this.startDate; }
    public void setStartDate(LocalDateTime startDate){ this.startDate = startDate; }

    public LocalDateTime getEndDate(){ return this.endDate; }
    public void setEndDate(LocalDateTime endDate){ this.endDate = endDate; }
}

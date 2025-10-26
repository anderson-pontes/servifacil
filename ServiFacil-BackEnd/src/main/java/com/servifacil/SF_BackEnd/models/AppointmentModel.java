package com.servifacil.SF_BackEnd.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.servifacil.SF_BackEnd.converters.AppointmentStatusConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_appointments")
public class AppointmentModel {

    @Id
    @Column(name = "Appointment_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int appointmentId;

    @ManyToOne
    @JoinColumn(name = "Service_ID")
    private ServiceModel service;

    @ManyToOne
    @JoinColumn(name = "Client_ID")
    private UserModel client;

    @Column(name = "Start_Date")
    @NotNull(message = "Data e hora de início são obrigatórias!")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
    @Future(message = "Insira uma data válida!")
    private LocalDateTime startDate;

    @Column(name = "End_Date")
    @NotNull(message = "Data e hora de término é obrigatórias!")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
    @Future(message = "Insira uma data válida!")
    private LocalDateTime endDate;

    @Convert(converter = AppointmentStatusConverter.class)
    @Column(name = "Ap_Status")
    private AppointmentStatus appointmentStatus = AppointmentStatus.Pendente;

    public enum AppointmentStatus {
        Pendente("Pendente"),
        Concluido("Concluido"),
        Cancelado("Cancelado");

        private final String displayName;

        AppointmentStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        // Converte texto do banco para Enum
        public static AppointmentModel.AppointmentStatus fromDisplayName(String dbValue) {
            if (dbValue == null) return null;
            for (AppointmentModel.AppointmentStatus status : values()) {
                if (status.displayName.equalsIgnoreCase(dbValue)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Status de agendamento inválido: " + dbValue);
        }
    }

    // GETTERS & SETTERS

    public int getAppointmentId(){ return this.appointmentId; }

    public ServiceModel getService(){ return this.service; }
    public void setService(ServiceModel service){ this.service = service; }

    public UserModel getClient(){ return this.client; }
    public void setClient(UserModel client){ this.client = client; }

    public LocalDateTime getStartDate(){ return this.startDate; }
    public void setStartDate(LocalDateTime startDate){ this.startDate = startDate; }

    public LocalDateTime getEndDate(){ return this.endDate; }
    public void setEndDate(LocalDateTime endDate){ this.endDate = endDate; }

    public AppointmentStatus getAppointmentStatus(){ return this.appointmentStatus; }
    public void setAppointmentStatus(AppointmentStatus appointmentStatus){ this.appointmentStatus = appointmentStatus; }
}
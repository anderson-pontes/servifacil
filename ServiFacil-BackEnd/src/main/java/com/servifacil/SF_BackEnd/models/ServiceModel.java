package com.servifacil.SF_BackEnd.models;

import com.servifacil.SF_BackEnd.converters.ServiceStatusConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "tb_services")
public class ServiceModel {

    @Id
    @Column(name = "Service_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int serviceId;

    @Column(name = "Title")
    @NotBlank(message = "Título é obrigatório!")
    @Size(min = 2, max = 125, message = "Título deve conter entre 2 e 125 caractéres")
    private String title;

    @Column(precision = 6, scale = 2, name = "Price")
    @NotNull(message = "Preço é obrigatório!")
    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
    @Digits(integer = 6, fraction = 2, message = "O preço deve ter no máximo 6 dígitos inteiros e 2 decimais")
    private BigDecimal price;

    @Column(name = "Details")
    @NotBlank(message = "Detalhes são obrigatórios!")
    @Size(min = 25, max = 250, message = "Os detalhes devem conter entre 25 e 250 caractéres!")
    private String details;

    @Convert(converter = ServiceStatusConverter.class)
    @Column(name = "Service_Status")
    private ServiceStatus serviceStatus = ServiceStatus.Ativo;

    @ManyToOne
    @JoinColumn(name = "Professional_ID")
    private UserModel professional;

    @OneToOne
    @JoinColumn(name = "Category_ID")
    private CategoryModel category;

    @OneToMany(mappedBy = "service")
    private List<AppointmentModel> appointments;

    @OneToMany(mappedBy = "service")
    private List<AssessmentModel> assessments;

    public enum ServiceStatus {
        Ativo("Ativo"),
        Inativo("Inativo");

        private final String displayName;

        ServiceStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        // Converte texto do banco para Enum
        public static ServiceModel.ServiceStatus fromDisplayName(String dbValue) {
            if (dbValue == null) return null;
            for (ServiceModel.ServiceStatus status : values()) {
                if (status.displayName.equalsIgnoreCase(dbValue)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Status de serviço inválido: " + dbValue);
        }
    }

    // GETTERS & SETTERS

    public int getServiceId(){ return this.serviceId; }

    public String getTitle(){ return this.title; }
    public void setTitle(String title){ this.title = title; }

    public BigDecimal getPrice(){ return this.price; }
    public void setPrice(BigDecimal price){ this.price = price; }

    public String getDetails(){ return this.details; }
    public void setDetails(String details){ this.details = details; }

    public UserModel getProfessional(){ return this.professional; }
    public void setProfessional(UserModel professional){ this.professional = professional; }

    public CategoryModel getCategory(){ return this.category; }
    public void setCategory(CategoryModel category){ this.category = category; }

    public ServiceStatus getServiceStatus(){ return this.serviceStatus; }
    public void setServiceStatus(ServiceStatus serviceStatus){ this.serviceStatus = serviceStatus; }
}

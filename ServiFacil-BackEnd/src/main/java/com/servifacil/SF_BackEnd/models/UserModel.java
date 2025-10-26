package com.servifacil.SF_BackEnd.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.servifacil.SF_BackEnd.converters.UserTypeConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.br.CNPJ;

import java.time.*;
import java.util.List;

@Entity
@Table(name = "tb_users")
public class UserModel {

    @Id
    @Column(name = "User_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(name = "User_Name")
    @Size(min = 2, max = 150, message = "Nome deve conter entre 2 e 100 caracteres!")
    @NotBlank(message = "Nome é obrigatório!")
    private String userName;

    @Column(name = "Email")
    @Email(message = "Insira um email válido!")
    @Size(max = 150, message = "Email deve conter no máximo 150 caracteres!")
    @NotBlank(message = "Email é obrigatório!")
    private String email;

    @Column(name = "User_Password", length = 150)
    @JsonIgnore
    @Size(min = 8, message = "A senha deve conter mínimo 8 caracteres!")
    @NotBlank(message = "Senha é obrigatória!")
    private String userPassword;

    @Column(name = "CPF")
    @Size(min = 11, max = 15, message = "CPF inválido!")
    @NotBlank(message = "CPF é obrigatório!")
    private String cpf;

    @Column(name = "CNPJ")
    @CNPJ(message = "CNPJ inválido!")
    private String cnpj;

    @Column(name = "Telephone")
    @Pattern(regexp = "^\\(?(\\d{2})\\)?[\\s-]?\\d{4,5}[\\s-]?\\d{4}$", message = "Telefone deve estar no formato (XX) XXXXX-XXXX")
    @NotBlank(message = "Número de telefone é obrigatório!")
    private String telephone;

    @Column(name = "Birth_Date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Past(message = "Insira uma data válida!")
    @NotNull(message = "Data de nascimento é obrigatória!")
    private LocalDate birthDate;

    @Column(name = "RG")
    @NotBlank(message = "RG é obrigatório!")
    private String rg;

    @Convert(converter = UserTypeConverter.class)
    @Column(name = "User_Type")
    private UserType userType = UserType.Cliente;

    @Column(name = "Profile_Photo", columnDefinition = "LONGTEXT")
    private String profilePhoto;

    @Column(name = "Profession")
    private String profession;

    @OneToOne
    @JoinColumn(name = "Address_ID")
    private AddressModel address;

    @Column(name = "Created_At")
    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "professional")
    private List<ServiceModel> services;

    @OneToMany(mappedBy = "client")
    private List<AppointmentModel> appointments;

    @OneToMany(mappedBy = "client")
    private List<AssessmentModel> assessments;

    public enum UserType {
        Cliente("Cliente"),
        Profissional("Profissional");

        private final String displayName;

        UserType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        // Converte texto do banco para Enum
        public static UserType fromDisplayName(String dbValue) {
            if (dbValue == null)
                return null;
            for (UserType type : values()) {
                if (type.displayName.equalsIgnoreCase(dbValue)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Tipo de usuário inválido: " + dbValue);
        }
    }

    // GETTERS & SETTERS

    public int getUserId() {
        return this.userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserPassword() {
        return this.userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getCpf() {
        return this.cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCnpj() {
        return this.cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getRg() {
        return this.rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public UserType getUserType() {
        return this.userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getProfession() {
        return this.profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public AddressModel getAddress() {
        return this.address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getProfilePhoto() {
        return this.profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}

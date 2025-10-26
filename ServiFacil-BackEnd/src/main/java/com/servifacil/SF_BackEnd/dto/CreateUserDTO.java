package com.servifacil.SF_BackEnd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class CreateUserDTO {
    // Dados do usuário
    @Column(name = "User_Name")
    @Size(min = 2, max = 100, message = "Nome deve conter entre 2 e 100 caracteres!")
    @NotBlank(message = "Nome é obrigatório!")
    private String userName;

    @Column(name = "Email")
    @Email(message = "Insira um email válido!")
    @Size(max = 150, message = "Email deve conter no máximo 150 caracteres!")
    @NotBlank(message = "Email é obrigatório!")
    private String email;

    @Column(name = "User_Password", length = 150)
    @Size(min = 8, message = "A senha deve conter mínimo 8 caracteres!")
    @NotBlank(message = "Senha é obrigatória!")
    private String userPassword;

    @Column(name = "CPF")
    // @CPF(message = "CPF inválido!")
    @Size(min = 11, max = 15, message = "CPF inválido!")
    @NotBlank(message = "CPF é obrigatório!")
    private String cpf;

    @Column(name = "CNPJ")
    // @CNPJ(message = "CNPJ inválido!")
    private String cnpj;

    @Column(name = "Telephone")
    @Pattern(regexp = "^\\(?(\\d{2})\\)?[\\s-]?\\d{4,5}[\\s-]?\\d{4}$", message = "Telefone deve estar no formato (XX) XXXXX-XXXX")
    @NotBlank(message = "Número de telefone é obrigatório!")
    private String telephone;

    @Column(name = "Birth_Date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Past(message = "Insira uma data válida!")
    @NotNull(message = "Data de nascimento é obrigatória!")
    private LocalDate birthDate;

    @Column(name = "RG")
    @NotBlank(message = "RG é obrigatório!")
    private String rg;

    @Column(name = "User_Type")
    private String userType = "Cliente";

    @Column(name = "Profession")
    private String profession;

    @Column(name = "Profile_Photo")
    private String profilePhoto;

    // Dados do endereço
    @Column(name = "Zip_Code")
    @NotBlank(message = "CEP é obrigatório!")
    @Size(min = 9, max = 10, message = "CEP deve conter no mínmo 9 e no máximo 10 caractéres!")
    private String zipCode;

    @Column(name = "Street")
    @NotBlank(message = "Nome da rua é obrigatório!")
    @Size(min = 2, max = 100, message = "Nome da rua deve conter entre 2 e 100 caractéres!")
    private String street;

    @Column(name = "House_Number")
    @NotBlank(message = "Número da casa é obrigatório")
    @Size(min = 1, max = 10, message = "Número da casa deve conter entre 1 e 10 caractéres!")
    private String houseNumber;

    @Column(name = "Complement")
    @NotBlank(message = "Complemento da casa é obrigatório")
    @Size(min = 1, max = 25, message = "Complemento deve conter entre 1 e 25 caractéres!")
    private String complement;

    @NotBlank(message = "Bairro é obrigatório")
    @Size(min = 2, max = 80, message = "Bairro deve conter entre 2 e 80 caractéres!")
    private String neighborhood;

    @NotBlank(message = "Cidade é obrigatória")
    @Size(min = 2, max = 80, message = "Cidade deve conter entre 2 e 80 caractéres!")
    private String city;

    @NotBlank(message = "Estado é obrigatório")
    @Size(min = 2, max = 2, message = "Estado deve ter 2 caracteres")
    private String state;

    // Construtor padrão (obrigatório para desserialização JSON)
    public CreateUserDTO() {
    }

    // Construtor com todos os campos
    public CreateUserDTO(String userName, String email, String userPassword,
            String cpf, String rg, String telephone, String cnpj,
            LocalDate birthDate, String userType, String profession,
            String zipCode, String street, String houseNumber,
            String complement, String neighborhood, String city,
            String state) {
        this.userName = userName;
        this.email = email;
        this.userPassword = userPassword;
        this.cpf = cpf;
        this.rg = rg;
        this.telephone = telephone;
        this.cnpj = cnpj;
        this.birthDate = birthDate;
        this.userType = userType;
        this.profession = profession;
        this.zipCode = zipCode;
        this.street = street;
        this.houseNumber = houseNumber;
        this.complement = complement;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
    }

    // GETTERS E SETTERS (obrigatórios sem Lombok)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}

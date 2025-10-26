package com.servifacil.SF_BackEnd.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserLoginDTO {

    @Column(name = "Email")
    @Email(message = "Insira um email válido!")
    @Size(max = 150, message = "Email deve conter no máximo 150 caracteres!")
    @NotBlank(message = "Email é obrigatório!")
    private String email;

    @Column(name = "User_Password", length = 150)
    @Size(min = 8, message = "A senha deve conter mínimo 8 caracteres!")
    @NotBlank(message = "Senha é obrigatória!")
    private String userPassword;

    public UserLoginDTO(){

    }

    public UserLoginDTO(String email, String userPassword){
        this.email = email;
        this.userPassword = userPassword;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUserPassword() { return userPassword; }
    public void setUserPassword(String userPassword) { this.userPassword = userPassword; }

}

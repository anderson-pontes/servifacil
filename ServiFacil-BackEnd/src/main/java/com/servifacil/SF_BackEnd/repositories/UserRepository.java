package com.servifacil.SF_BackEnd.repositories;

import com.servifacil.SF_BackEnd.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer> {

    Optional<UserModel> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<UserModel> findById(Integer userId);

    @Procedure(procedureName = "spInsertUser")
    void spInsertUser(
            @Param("vUser_Name") String userName,
            @Param("vEmail") String email,
            @Param("vUser_Password") String userPassword,
            @Param("vCPF") String cpf,
            @Param("vRG") String rg,
            @Param("vTelephone") String telephone,
            @Param("vCNPJ") String cnpj,
            @Param("vBirth_Date") LocalDate birthDate,
            @Param("vUser_Type") String userType,
            @Param("vProfession") String profession,
            @Param("vProfile_Photo") String profilePhoto,
            @Param("vZip_Code") String zipCode,
            @Param("vStreet") String street,
            @Param("vHouse_Number") String houseNumber,
            @Param("vComplement") String complement,
            @Param("vNeighborhood") String neighborhood,
            @Param("vCity") String city,
            @Param("vState") String state);

    @Procedure(procedureName = "spUpdateUser")
    void spUpdateUser(
            @Param("vUser_ID") int userId,
            @Param("vUser_Name") String userName,
            @Param("vEmail") String email,
            @Param("vUser_Password") String userPassword,
            @Param("vCPF") String cpf,
            @Param("vRG") String rg,
            @Param("vTelephone") String telephone,
            @Param("vCNPJ") String cnpj,
            @Param("vBirth_Date") LocalDate birthDate,
            @Param("vUser_Type") String userType,
            @Param("vProfession") String profession,
            @Param("vProfile_Photo") String profilePhoto,
            @Param("vZip_Code") String zipCode,
            @Param("vStreet") String street,
            @Param("vHouse_Number") String houseNumber,
            @Param("vComplement") String complement,
            @Param("vNeighborhood") String neighborhood,
            @Param("vCity") String city,
            @Param("vState") String state);
}

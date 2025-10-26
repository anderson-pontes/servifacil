package com.servifacil.SF_BackEnd.repositories;

import com.servifacil.SF_BackEnd.models.ServiceModel;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceModel, Integer> {

    @Override
    List<ServiceModel> findAll();

    List<ServiceModel> findByCategoryCategory(String category);

    @Override
    Optional<ServiceModel> findById(Integer serviceId);

    @Override
    void deleteById(Integer serviceId);

    @Procedure(procedureName = "spInsertService")
    Integer spInsertService(
            @Param("vProfessional_ID") int professionalId,
            @Param("vTitle") String title,
            @Param("vPrice") BigDecimal price,
            @Param("vDetails") String details,
            @Param("vService_Status") String serviceStatus,
            @Param("vCategory") String category
    );

    @Procedure(procedureName = "spUpdateService")
    void spUpdateService(
            @Param("vProfessional_ID") int professionalId,
            @Param("vService_ID") int serviceId,
            @Param("vTitle") String title,
            @Param("vPrice") BigDecimal price,
            @Param("vDetails") String details,
            @Param("vService_Status") String serviceStatus,
            @Param("vCategory") String category
    );
}

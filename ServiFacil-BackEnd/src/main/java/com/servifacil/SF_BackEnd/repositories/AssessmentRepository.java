package com.servifacil.SF_BackEnd.repositories;

import com.servifacil.SF_BackEnd.models.AssessmentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentRepository extends JpaRepository<AssessmentModel, Integer> {

    @Override
    Optional<AssessmentModel> findById(Integer assessmentId);

    List<AssessmentModel> findAllByServiceServiceId(int serviceId);

    @Override
    void deleteById(Integer assessmentId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE tb_assessments SET Score = COALESCE(:score, Score), Comments = COALESCE(:comments, Comments) WHERE Assessment_ID = :assessmentId AND Service_ID = :serviceId AND Client_ID = :clientId", nativeQuery = true)
    int updateAssessment(@Param("assessmentId") int assessmentId, @Param("serviceId") int serviceId, @Param("clientId") int clientId, @Param("score") int score, @Param("comments") String comments);

    @Procedure(procedureName = "spInsertAssessment")
    void spInsertAssessment(
            @Param("vService_ID") int serviceId,
            @Param("vClient_ID") int clientId,
            @Param("vScore") int score,
            @Param("vComments") String comments
    );
}

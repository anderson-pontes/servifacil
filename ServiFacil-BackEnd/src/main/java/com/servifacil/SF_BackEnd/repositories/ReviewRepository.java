package com.servifacil.SF_BackEnd.repositories;

import com.servifacil.SF_BackEnd.models.ReviewModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewModel, Integer> {

    @Modifying
    @Query(value = "CALL sp_create_review(:appointmentId, :clientId, :rating, :comment)", nativeQuery = true)
    void createReview(
            @Param("appointmentId") Integer appointmentId,
            @Param("clientId") Integer clientId,
            @Param("rating") Integer rating,
            @Param("comment") String comment);

    @Query(value = "CALL sp_get_professional_reviews(:professionalId)", nativeQuery = true)
    List<Map<String, Object>> getProfessionalReviews(@Param("professionalId") Integer professionalId);

    @Query(value = "CALL sp_get_service_reviews(:serviceId)", nativeQuery = true)
    List<Map<String, Object>> getServiceReviews(@Param("serviceId") Integer serviceId);

    @Query(value = "CALL sp_get_service_rating(:serviceId)", nativeQuery = true)
    Map<String, Object> getServiceRating(@Param("serviceId") Integer serviceId);

    @Query(value = "CALL sp_can_review_appointment(:appointmentId, :clientId)", nativeQuery = true)
    Map<String, Object> canReviewAppointment(
            @Param("appointmentId") Integer appointmentId,
            @Param("clientId") Integer clientId);

    @Query("SELECT r FROM ReviewModel r WHERE r.appointmentId = :appointmentId")
    ReviewModel findByAppointmentId(@Param("appointmentId") Integer appointmentId);

    @Query(value = "SELECT r.* FROM reviews r WHERE r.Appointment_ID = :appointmentId", nativeQuery = true)
    ReviewModel findByAppointmentIdNative(@Param("appointmentId") Integer appointmentId);
}

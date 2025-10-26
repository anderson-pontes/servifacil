package com.servifacil.SF_BackEnd.services;

import com.servifacil.SF_BackEnd.dtos.ReviewDTO;
import com.servifacil.SF_BackEnd.exceptions.ApiException;
import com.servifacil.SF_BackEnd.models.ReviewModel;
import com.servifacil.SF_BackEnd.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Transactional
    public ReviewModel createReview(Integer appointmentId, Integer clientId, Integer rating, String comment) {
        try {
            // Validar rating
            if (rating < 1 || rating > 5) {
                throw new ApiException("Avaliação deve ser entre 1 e 5 estrelas", HttpStatus.BAD_REQUEST);
            }

            // Chamar stored procedure
            reviewRepository.createReview(appointmentId, clientId, rating, comment);

            // Buscar avaliação criada
            ReviewModel review = reviewRepository.findByAppointmentIdNative(appointmentId);

            if (review == null) {
                throw new ApiException("Erro ao criar avaliação", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return review;

        } catch (Exception e) {
            if (e instanceof ApiException) {
                throw e;
            }
            throw new ApiException("Erro ao criar avaliação: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional(readOnly = true)
    public List<ReviewDTO> getProfessionalReviews(Integer professionalId) {
        try {
            List<Map<String, Object>> results = reviewRepository.getProfessionalReviews(professionalId);
            List<ReviewDTO> reviews = new ArrayList<>();

            for (Map<String, Object> row : results) {
                ReviewDTO dto = new ReviewDTO();
                dto.setReviewId(getInteger(row, "review_id"));
                dto.setAppointmentId(getInteger(row, "appointment_id"));
                dto.setRating(getInteger(row, "rating"));
                dto.setComment(getString(row, "comment"));
                dto.setClientName(getString(row, "client_name"));
                dto.setServiceName(getString(row, "service_name"));
                dto.setAppointmentDate(getString(row, "appointment_date"));
                dto.setAppointmentTime(getString(row, "appointment_time"));
                dto.setCreatedAt(getLocalDateTime(row, "created_at"));
                reviews.add(dto);
            }

            return reviews;

        } catch (Exception e) {
            throw new ApiException("Erro ao buscar avaliações: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public List<ReviewDTO> getServiceReviews(Integer serviceId) {
        try {
            List<Map<String, Object>> results = reviewRepository.getServiceReviews(serviceId);
            List<ReviewDTO> reviews = new ArrayList<>();

            for (Map<String, Object> row : results) {
                ReviewDTO dto = new ReviewDTO();
                dto.setReviewId(getInteger(row, "review_id"));
                dto.setRating(getInteger(row, "rating"));
                dto.setComment(getString(row, "comment"));
                dto.setClientName(getString(row, "client_name"));
                dto.setCreatedAt(getLocalDateTime(row, "created_at"));
                reviews.add(dto);
            }

            return reviews;

        } catch (Exception e) {
            throw new ApiException("Erro ao buscar avaliações do serviço: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getServiceRating(Integer serviceId) {
        try {
            return reviewRepository.getServiceRating(serviceId);
        } catch (Exception e) {
            throw new ApiException("Erro ao buscar média de avaliações: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public Map<String, Object> canReviewAppointment(Integer appointmentId, Integer clientId) {
        try {
            return reviewRepository.canReviewAppointment(appointmentId, clientId);
        } catch (Exception e) {
            throw new ApiException("Erro ao verificar permissão de avaliação: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Helper methods
    private Integer getInteger(Map<String, Object> row, String key) {
        Object value = row.get(key);
        if (value == null)
            return null;
        if (value instanceof Integer)
            return (Integer) value;
        if (value instanceof Long)
            return ((Long) value).intValue();
        if (value instanceof String)
            return Integer.parseInt((String) value);
        return null;
    }

    private String getString(Map<String, Object> row, String key) {
        Object value = row.get(key);
        return value != null ? value.toString() : null;
    }

    private LocalDateTime getLocalDateTime(Map<String, Object> row, String key) {
        Object value = row.get(key);
        if (value == null)
            return null;
        if (value instanceof LocalDateTime)
            return (LocalDateTime) value;
        if (value instanceof java.sql.Timestamp) {
            return ((java.sql.Timestamp) value).toLocalDateTime();
        }
        return null;
    }
}

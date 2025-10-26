package com.servifacil.SF_BackEnd.controllers;

import com.servifacil.SF_BackEnd.dtos.ReviewDTO;
import com.servifacil.SF_BackEnd.models.ReviewModel;
import com.servifacil.SF_BackEnd.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:5173")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createReview(@RequestBody Map<String, Object> reviewData) {
        try {
            Integer appointmentId = (Integer) reviewData.get("appointmentId");
            Integer clientId = (Integer) reviewData.get("clientId");
            Integer rating = (Integer) reviewData.get("rating");
            String comment = (String) reviewData.get("comment");

            ReviewModel review = reviewService.createReview(appointmentId, clientId, rating, comment);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Avaliação criada com sucesso!");
            response.put("data", review);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/professional/{professionalId}")
    public ResponseEntity<Map<String, Object>> getProfessionalReviews(@PathVariable Integer professionalId) {
        try {
            List<ReviewDTO> reviews = reviewService.getProfessionalReviews(professionalId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", reviews);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<Map<String, Object>> getServiceReviews(@PathVariable Integer serviceId) {
        try {
            List<ReviewDTO> reviews = reviewService.getServiceReviews(serviceId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", reviews);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/service/{serviceId}/rating")
    public ResponseEntity<Map<String, Object>> getServiceRating(@PathVariable Integer serviceId) {
        try {
            Map<String, Object> rating = reviewService.getServiceRating(serviceId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", rating);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/can-review/{appointmentId}/{clientId}")
    public ResponseEntity<Map<String, Object>> canReviewAppointment(
            @PathVariable Integer appointmentId,
            @PathVariable Integer clientId) {
        try {
            Map<String, Object> result = reviewService.canReviewAppointment(appointmentId, clientId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", result);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}

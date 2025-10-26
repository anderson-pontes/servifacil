package com.servifacil.SF_BackEnd.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "tb_assessments")
public class AssessmentModel {

    @Id
    @Column(name = "Assessment_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int assessmentId;

    @ManyToOne
    @JoinColumn(name = "Service_ID")
    private ServiceModel service;

    @ManyToOne
    @JoinColumn(name = "Client_ID")
    private UserModel client;

    @Column(name = "Score")
    @NotNull(message = "A pontuação deve ser fornecida para a avaliação!")
    @Min(value = 1, message = "A pontuação precisa ser no minímo 1 estrela!")
    @Max(value = 5, message = "A avaliação pode ter no máximo 5 estrelas!")
    @Positive
    private int score;

    @Column(name = "Comments")
    @Size(max = 200, message = "O comentário da avaliação pode ter no máximo 200 caractéres!")
    private String comments;

    // GETTERS & SETTERS

    public int getAssessmentId(){ return this.assessmentId; }

    public ServiceModel getService(){ return this.service; }
    public void setService(ServiceModel service){ this.service = service; }

    public UserModel getClient(){ return this.client; }
    public void setClient(UserModel client){ this.client = client; }

    public int getScore(){ return this.score; }
    public void setScore(int score){ this.score = score; }

    public String getComments(){ return this.comments; }
    public void setComments(String comments){ this.comments = comments; }
}

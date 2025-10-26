package com.servifacil.SF_BackEnd.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class EditAssessmentDTO {

    @Column(name = "Score")
    @Min(value = 1, message = "A pontuação precisa ser no minímo 1 estrela!")
    @Max(value = 5, message = "A avaliação pode ter no máximo 5 estrelas!")
    private int score;

    @Column(name = "Comments")
    @Size(max = 200, message = "O comentário da avaliação pode ter no máximo 200 caractéres!")
    private String comments;

    // GETTERS & SETTERS

    public int getScore(){ return this.score; }
    public void setScore(int score){ this.score = score; }

    public String getComments(){ return this.comments; }
    public void setComments(String comments){ this.comments = comments; }
}

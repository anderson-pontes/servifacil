package com.servifacil.SF_BackEnd.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "tb_categories")
public class CategoryModel {

    @Id
    @Column(name = "Category_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int categoryId;

    @Column(name = "Category")
    @NotBlank(message = "Categoria é obrigatória!")
    @Size(min = 2, max = 80, message = "Categoria deve conter entre 2 e 80 caractéres!")
    private String category;

    @Column(name = "Details")
    @Size(min = 2, max = 150, message = "detalhes da categoria deve conter entre 2 e 150 caractéres!")
    private String details;

    @OneToMany(mappedBy = "category")
    private List<ServiceModel> services;

    // GETTERS & SETTERS

    public int getCategoryId(){ return this.categoryId; }

    public String getCategory(){ return this.category; }
    public void setCategory(String category){ this.category = category; }

    public String getDetails(){ return this.details; }
    public void setDetails(String details){ this.details = details; }
}

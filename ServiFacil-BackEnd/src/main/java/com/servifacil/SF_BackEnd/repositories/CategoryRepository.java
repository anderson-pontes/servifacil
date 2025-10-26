package com.servifacil.SF_BackEnd.repositories;

import com.servifacil.SF_BackEnd.models.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryModel, Integer> {

    Optional<CategoryModel> findByCategory(String category);
}
package com.servifacil.SF_BackEnd.services;

import com.servifacil.SF_BackEnd.dto.EditServiceDTO;
import com.servifacil.SF_BackEnd.dto.CreateServiceDTO;
import com.servifacil.SF_BackEnd.exceptions.ApiException;
import com.servifacil.SF_BackEnd.models.CategoryModel;
import com.servifacil.SF_BackEnd.models.ServiceModel;
import com.servifacil.SF_BackEnd.models.UserModel;
import com.servifacil.SF_BackEnd.repositories.CategoryRepository;
import com.servifacil.SF_BackEnd.repositories.ServiceRepository;
import com.servifacil.SF_BackEnd.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public List<ServiceModel> listAllServices() {

        List<ServiceModel> services = serviceRepository.findAll();
        if (services == null || services.isEmpty()) {
            throw new ApiException("Nenhum serviço foi encontrado!", HttpStatus.NOT_FOUND);
        }

        return services;
    }

    @Transactional
    public List<ServiceModel> filterByCategory(String category) {

        List<ServiceModel> services = serviceRepository.findByCategoryCategory(category);
        if (services == null || services.isEmpty()) {
            throw new ApiException("Nenhum serviço encontrado nesta categoria!", HttpStatus.NOT_FOUND);
        }

        return services;
    }

    @Transactional
    public ServiceModel getServiceById(int serviceId) {

        ServiceModel service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ApiException("Serviço não encontrado!", HttpStatus.NOT_FOUND));

        return service;
    }

    @Transactional
    public int createService(int userId, CreateServiceDTO request) {

        int newService = serviceRepository.spInsertService(
                userId,
                request.getTitle(),
                request.getPrice(),
                request.getDetails(),
                request.getServiceStatus(),
                request.getCategory());

        return newService;
    }

    @Transactional
    public void editService(int professionalId, int serviceId, EditServiceDTO request) {

        ServiceModel existingService = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ApiException("Serviço não encontrado!", HttpStatus.NOT_FOUND));

        UserModel professional = existingService.getProfessional();

        if (professional == null) {
            throw new ApiException("Este serviço não tem profissional atribuído!", HttpStatus.NOT_FOUND);
        }

        if (professional.getUserId() != professionalId) {
            throw new ApiException("Este serviço não pertence a este profissional!", HttpStatus.NOT_FOUND);
        }

        CategoryModel existingCategory = categoryRepository.findByCategory(request.getCategory())
                .orElseThrow(() -> new ApiException("Categoria não existe no sistema!", HttpStatus.NOT_FOUND));

        if (request == null) {
            throw new ApiException("Ao menos um campo deve ser preenchido!", HttpStatus.BAD_REQUEST);
        }

        serviceRepository.spUpdateService(
                professionalId,
                serviceId,
                request.getTitle(),
                request.getPrice(),
                request.getDetails(),
                request.getServiceStatus(),
                request.getCategory());
    }

    @Transactional
    public void deleteService(int professionalId, int serviceId) {

        ServiceModel existingService = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ApiException("Serviço não encontrado!", HttpStatus.NOT_FOUND));

        UserModel professional = existingService.getProfessional();

        if (professional == null) {
            throw new ApiException("Este serviço não tem profissional atribuído!", HttpStatus.NOT_FOUND);
        }

        if (professional.getUserId() != professionalId) {
            throw new ApiException("Este serviço não pertence a este profissional!", HttpStatus.NOT_FOUND);
        }

        serviceRepository.deleteById(serviceId);
    }
    // public List<ServiceModel> getServiceByName(String name) {
    //
    // }
    //
}
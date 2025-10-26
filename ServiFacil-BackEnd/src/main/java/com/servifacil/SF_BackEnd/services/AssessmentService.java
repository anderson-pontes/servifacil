package com.servifacil.SF_BackEnd.services;

import com.servifacil.SF_BackEnd.dto.CreateAssessmentDTO;
import com.servifacil.SF_BackEnd.dto.EditAssessmentDTO;
import com.servifacil.SF_BackEnd.exceptions.ApiException;
import com.servifacil.SF_BackEnd.models.AssessmentModel;
import com.servifacil.SF_BackEnd.models.ServiceModel;
import com.servifacil.SF_BackEnd.models.UserModel;
import com.servifacil.SF_BackEnd.repositories.AssessmentRepository;
import com.servifacil.SF_BackEnd.repositories.ServiceRepository;
import com.servifacil.SF_BackEnd.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private ServiceRepository serviceRepository; // vem da PR

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void createAssessment(int clientId, int serviceId, CreateAssessmentDTO request) {

        UserModel existingUser = userRepository.findById(clientId)
                .orElseThrow(() -> new ApiException("Usuário não encontrado!", HttpStatus.NOT_FOUND));


        ServiceModel existingService = serviceRepository.findById(serviceId)
                .filter(s -> "Ativo".equalsIgnoreCase(s.getServiceStatus().getDisplayName().trim()))
                .orElseThrow(() -> new ApiException("Serviço não encontrado ou inativo!", HttpStatus.NOT_FOUND));

        assessmentRepository.spInsertAssessment(
                existingService.getServiceId(),
                existingUser.getUserId(),
                request.getScore(),
                request.getComments()
        );
    }

    @Transactional(readOnly = true)
    public List<AssessmentModel> listAssessmentsByService(int clientId, int serviceId) {

        UserModel existingUser = userRepository.findById(clientId)
                .orElseThrow(() -> new ApiException("Usuário não encontrado!", HttpStatus.NOT_FOUND));


        ServiceModel existingService = serviceRepository.findById(serviceId)
                .filter(s -> "Ativo".equalsIgnoreCase(s.getServiceStatus().getDisplayName().trim()))
                .orElseThrow(() -> new ApiException("Serviço não encontrado ou inativo!", HttpStatus.NOT_FOUND));

        List<AssessmentModel> assessments = assessmentRepository.findAllByServiceServiceId(serviceId);

        if(assessments.isEmpty() || assessments == null){
            throw new ApiException("Este serviço ainda não possui avaliações!", HttpStatus.NOT_FOUND);
        }

        return assessments;
    }

    @Transactional
    public void editAssessment(int clientId, int serviceId, int assessmentId, EditAssessmentDTO request) {

        UserModel existingUser = userRepository.findById(clientId)
                .orElseThrow(() -> new ApiException("Usuário não encontrado!", HttpStatus.NOT_FOUND));

        ServiceModel existingService = serviceRepository.findById(serviceId)
                .filter(s -> "Ativo".equalsIgnoreCase(s.getServiceStatus().getDisplayName().trim()))
                .orElseThrow(() -> new ApiException("Serviço não encontrado ou inativo!", HttpStatus.NOT_FOUND));

        AssessmentModel existingAssessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new ApiException("Avaliação não encontrada!", HttpStatus.NOT_FOUND));

        if(existingAssessment.getService().getServiceId() != existingService.getServiceId()){
            throw new ApiException("Esta avaliação não pertence a este serviço!", HttpStatus.CONFLICT);
        }

        if(existingAssessment.getClient().getUserId() != existingUser.getUserId()){
            throw new ApiException("Esta avaliação não pertence a você!", HttpStatus.CONFLICT);
        }

        assessmentRepository.updateAssessment(
                existingAssessment.getAssessmentId(),
                existingAssessment.getService().getServiceId(),
                existingAssessment.getClient().getUserId(),
                request.getScore(),
                request.getComments()
        );
    }

    @Transactional
    public void deleteAssessment(int clientId, int serviceId, int assessmentId) {

        UserModel existingUser = userRepository.findById(clientId)
                .orElseThrow(() -> new ApiException("Usuário não encontrado!", HttpStatus.NOT_FOUND));

        ServiceModel existingService = serviceRepository.findById(serviceId)
                .filter(s -> "Ativo".equalsIgnoreCase(s.getServiceStatus().getDisplayName().trim()))
                .orElseThrow(() -> new ApiException("Serviço não encontrado ou inativo!", HttpStatus.NOT_FOUND));

        AssessmentModel existingAssessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new ApiException("Avaliação não encontrada!", HttpStatus.NOT_FOUND));

        if(existingAssessment.getService().getServiceId() != existingService.getServiceId()){
            throw new ApiException("Esta avaliação não pertence a este serviço!", HttpStatus.CONFLICT);
        }

        if(existingAssessment.getClient().getUserId() != existingUser.getUserId()){
            throw new ApiException("Esta avaliação não pertence a você!", HttpStatus.CONFLICT);
        }

        assessmentRepository.deleteById(assessmentId);
    }
}
package com.servifacil.SF_BackEnd.controllers;

import com.servifacil.SF_BackEnd.dto.CreateAssessmentDTO;
import com.servifacil.SF_BackEnd.dto.EditAssessmentDTO;
import com.servifacil.SF_BackEnd.exceptions.ApiException;
import com.servifacil.SF_BackEnd.models.AssessmentModel;
import com.servifacil.SF_BackEnd.responses.EntityResponse;
import com.servifacil.SF_BackEnd.services.AssessmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/assessments")
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;


    @PostMapping("/{id}/{serviceId}")
    public ResponseEntity<EntityResponse<?>> createAssessment(@PathVariable int id,
                                              @PathVariable int serviceId,
                                              @Valid @RequestBody CreateAssessmentDTO request,
                                              BindingResult bindingResult,
                                              HttpServletRequest servletReq) {

        // Se houver erros de validação, lança ApiException
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            throw new ApiException(errors, HttpStatus.BAD_REQUEST);
        }

        Integer idFromToken = (Integer) servletReq.getAttribute("userId");

        if(idFromToken == null){
            throw new ApiException("Usuário não autenticado ou token inválido!", HttpStatus.UNAUTHORIZED);
        }

        if(id != idFromToken){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new EntityResponse<>(false, "Você não tem permissão para avaliar serviços por outro usuário!", null));
        }

        assessmentService.createAssessment(id, serviceId, request);

        EntityResponse<?> createResponse = new EntityResponse<>(
                true,
                "Avaliação criada com sucesso!",
                null
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(createResponse);
    }

    @GetMapping("/service/{id}/{serviceId}")
    public ResponseEntity<EntityResponse<?>> listAssessmentsByService(@PathVariable int id,
                                                                      @PathVariable int serviceId,
                                                                      HttpServletRequest servletReq) {

        Integer idFromToken = (Integer) servletReq.getAttribute("userId");

        if(idFromToken == null){
            throw new ApiException("Usuário não autenticado ou token inválido!", HttpStatus.UNAUTHORIZED);
        }

        if(id != idFromToken){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new EntityResponse<>(false, "Você não tem permissão para listar avaliações de serviços por outro usuário!", null));
        }

        List<AssessmentModel> assessments = assessmentService.listAssessmentsByService(id, serviceId);

        EntityResponse<?> getResponse = new EntityResponse<>(
                true,
                "Avaliações do serviço: " + serviceId + " listadas com sucesso!",
                assessments
        );

        return ResponseEntity.status(HttpStatus.OK).body(getResponse);
    }

    @PatchMapping("/{id}/{serviceId}/{assessmentId}")
    public ResponseEntity<EntityResponse<?>> editAssessment(@PathVariable int id,
                                            @PathVariable int serviceId,
                                            @PathVariable int assessmentId,
                                            @Valid @RequestBody EditAssessmentDTO request,
                                            BindingResult bindingResult,
                                            HttpServletRequest servletReq) {

        // Se houver erros de validação, lança ApiException
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            throw new ApiException(errors, HttpStatus.BAD_REQUEST);
        }

        Integer idFromToken = (Integer) servletReq.getAttribute("userId");

        if(idFromToken == null){
            throw new ApiException("Usuário não autenticado ou token inválido!", HttpStatus.UNAUTHORIZED);
        }

        if(id != idFromToken){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new EntityResponse<>(false, "Você não tem permissão para atualizar avaliações de outro usuário!", null));
        }

        assessmentService.editAssessment(id, serviceId, assessmentId, request);

        EntityResponse<?> editResponse = new EntityResponse<>(
                true,
                "Avaliação atualizada com sucesso!",
                null
        );

        return ResponseEntity.status(HttpStatus.OK).body(editResponse);
    }

    @DeleteMapping("/{id}/{serviceId}/{assessmentId}")
    public ResponseEntity<EntityResponse<?>> deleteAssessment(@PathVariable int id,
                                                              @PathVariable int serviceId,
                                                              @PathVariable int assessmentId,
                                                              HttpServletRequest servletReq) {

        Integer idFromToken = (Integer) servletReq.getAttribute("userId");

        if(idFromToken == null){
            throw new ApiException("Usuário não autenticado ou token inválido!", HttpStatus.UNAUTHORIZED);
        }

        if(id != idFromToken){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new EntityResponse<>(false, "Você não tem permissão para apagar avaliação de serviços por outro usuário!", null));
        }

        assessmentService.deleteAssessment(id, serviceId, assessmentId);

        EntityResponse<?> deleteResponse = new EntityResponse<>(
                true,
                "Avaliação " + assessmentId + " apagada com sucesso!",
                null
        );

        return ResponseEntity.status(HttpStatus.OK).body(deleteResponse);
    }
}
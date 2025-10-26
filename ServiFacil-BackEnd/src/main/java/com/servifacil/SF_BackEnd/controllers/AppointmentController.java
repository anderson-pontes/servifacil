package com.servifacil.SF_BackEnd.controllers;

import com.servifacil.SF_BackEnd.dto.CreateAppointmentDTO;
import com.servifacil.SF_BackEnd.exceptions.ApiException;
import com.servifacil.SF_BackEnd.models.AppointmentModel;
import com.servifacil.SF_BackEnd.responses.EntityResponse;
import com.servifacil.SF_BackEnd.services.AppointmentService;
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
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // Criar agendamento
    @PostMapping("/{id}/{serviceId}")
    public ResponseEntity<EntityResponse<?>> createAppointment(@PathVariable int id,
            @PathVariable int serviceId,
            @Valid @RequestBody CreateAppointmentDTO request,
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

        if (idFromToken == null) {
            throw new ApiException("Usuário não autenticado ou token inválido!", HttpStatus.UNAUTHORIZED);
        }

        if (id != idFromToken) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new EntityResponse<>(false, "Você não tem permissão para agendar serviços por outro usuário!",
                            null));
        }

        int newAppointment = appointmentService.createAppointment(id, serviceId, request);

        EntityResponse<Integer> createResponse = new EntityResponse<>(
                true,
                "Agendamento criado com sucesso!",
                newAppointment);

        return ResponseEntity.status(HttpStatus.CREATED).body(createResponse);
    }

    // Buscar todos os agendamentos
    @GetMapping("/user/{id}/{apStatus}")
    public ResponseEntity<EntityResponse<?>> getAppointmentsByUser(@PathVariable int id,
            @PathVariable String apStatus,
            HttpServletRequest servletReq) {

        Integer idFromToken = (Integer) servletReq.getAttribute("userId");

        if (idFromToken == null) {
            throw new ApiException("Usuário não autenticado ou token inválido!", HttpStatus.UNAUTHORIZED);
        }

        if (id != idFromToken) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new EntityResponse<>(false, "Você não tem permissão para ver agendamentos de outro usuário!",
                            null));
        }

        List<AppointmentModel> userAppointments = appointmentService.getAppointmentsByUser(id, apStatus);

        EntityResponse<List<AppointmentModel>> getResponse = new EntityResponse<>(
                true,
                "Agendamentos do usuário: " + id + "carregados com sucesso!",
                userAppointments);

        return ResponseEntity.status(HttpStatus.OK).body(getResponse);
    }

    // Buscar agendamentos por serviço
    @GetMapping("/service/{id}/{serviceId}/{apStatus}")
    public ResponseEntity<EntityResponse<?>> getAppointmentsByService(@PathVariable int id,
            @PathVariable int serviceId,
            @PathVariable String apStatus,
            HttpServletRequest servletReq) {

        Integer idFromToken = (Integer) servletReq.getAttribute("userId");

        if (idFromToken == null) {
            throw new ApiException("Usuário não autenticado ou token inválido!", HttpStatus.UNAUTHORIZED);
        }

        if (id != idFromToken) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new EntityResponse<>(false,
                            "Você não tem permissão para ver agendamentos de serviços outro profissional!", null));
        }

        List<AppointmentModel> serviceAppointments = appointmentService.getAppointmentsByService(id, serviceId,
                apStatus);

        EntityResponse<List<AppointmentModel>> getResponse = new EntityResponse<>(
                true,
                "Agendamentos do serviço: " + id + "carregados com sucesso!",
                serviceAppointments);

        return ResponseEntity.status(HttpStatus.OK).body(getResponse);
    }

    // Cancelar agendamento
    @PatchMapping("/cancel/{id}/{appointmentId}")
    public ResponseEntity<EntityResponse<?>> cancelAppointment(@PathVariable int id,
            @PathVariable int appointmentId,
            HttpServletRequest servletReq) {

        Integer idFromToken = (Integer) servletReq.getAttribute("userId");

        if (idFromToken == null) {
            throw new ApiException("Usuário não autenticado ou token inválido!", HttpStatus.UNAUTHORIZED);
        }

        if (id != idFromToken) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new EntityResponse<>(false,
                            "Você não tem permissão para cancelar agendamentos de outro usuário!", null));
        }

        int canceledAppointment = appointmentService.cancelAppointment(id, appointmentId);

        EntityResponse<Integer> cancelResponse = new EntityResponse<>(
                true,
                "Agendamento: " + canceledAppointment + " cancelado com sucesso!",
                canceledAppointment);

        return ResponseEntity.status(HttpStatus.OK).body(cancelResponse);
    }

    // Completar agendamento
    @PatchMapping("/conclude/{id}/{appointmentId}")
    public ResponseEntity<EntityResponse<?>> concludeAppointment(@PathVariable int id,
            @PathVariable int appointmentId,
            HttpServletRequest servletReq) {

        Integer idFromToken = (Integer) servletReq.getAttribute("userId");

        if (idFromToken == null) {
            throw new ApiException("Usuário não autenticado ou token inválido!", HttpStatus.UNAUTHORIZED);
        }

        // Permitir que tanto o cliente quanto o profissional (dono do serviço) possam
        // concluir
        // Se o id do path não é o mesmo do token, verificar se é o profissional do
        // serviço
        if (id != idFromToken) {
            // Verificar se quem está concluindo é o profissional dono do serviço
            boolean isProfessionalOwner = appointmentService.isProfessionalOwner(idFromToken, appointmentId);

            if (!isProfessionalOwner) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new EntityResponse<>(false, "Você não tem permissão para concluir este agendamento!",
                                null));
            }
        }

        int concludedAppointment = appointmentService.concludeAppointment(id, appointmentId);

        EntityResponse<Integer> cancelResponse = new EntityResponse<>(
                true,
                "Agendamento: " + concludedAppointment + " concluído com sucesso!",
                concludedAppointment);

        return ResponseEntity.status(HttpStatus.OK).body(cancelResponse);
    }
    //
    // // Atualizar agendamento
    // @PutMapping("/{id}")
    // public ResponseEntity<?> updateAppointment(@PathVariable int id, @RequestBody
    // AppointmentDTO appointmentDTO) {
    // try {
    // AppointmentModel appointment = appointmentService.updateAppointment(id,
    // appointmentDTO);
    // return ResponseEntity.ok(appointment);
    // } catch (RuntimeException e) {
    // return ResponseEntity.badRequest().body(e.getMessage());
    // }
    // }
}
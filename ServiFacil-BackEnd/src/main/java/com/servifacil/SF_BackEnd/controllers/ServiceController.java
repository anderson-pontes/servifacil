package com.servifacil.SF_BackEnd.controllers;

import com.servifacil.SF_BackEnd.dto.EditServiceDTO;
import com.servifacil.SF_BackEnd.dto.CreateServiceDTO;
import com.servifacil.SF_BackEnd.exceptions.ApiException;
import com.servifacil.SF_BackEnd.models.ServiceModel;
import com.servifacil.SF_BackEnd.responses.EntityResponse;
import com.servifacil.SF_BackEnd.services.ServiceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @GetMapping("/getall")
    public ResponseEntity<EntityResponse<?>> listAllServices() {

        List<ServiceModel> services = serviceService.listAllServices();

        EntityResponse<?> getResponse = new EntityResponse<>(
                true,
                "Todos os serviços foram carregados com sucesso!",
                services);

        return ResponseEntity.status(HttpStatus.OK).body(getResponse);
    }

    @GetMapping("/filter/{category}")
    public ResponseEntity<EntityResponse<?>> filterByCategory(@PathVariable String category) {

        List<ServiceModel> filteredServices = serviceService.filterByCategory(category);

        EntityResponse<?> getResponse = new EntityResponse<>(
                true,
                "Todos os serviços filtrados peça categoria " + category + " foram carregados com sucesso!",
                filteredServices);

        return ResponseEntity.status(HttpStatus.OK).body(getResponse);
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<EntityResponse<?>> getServiceById(@PathVariable int serviceId) {

        ServiceModel service = serviceService.getServiceById(serviceId);

        EntityResponse<?> getResponse = new EntityResponse<>(
                true,
                "Serviço carregado com sucesso!",
                service);

        return ResponseEntity.status(HttpStatus.OK).body(getResponse);
    }

    @PreAuthorize("hasRole('PROFISSIONAL')")
    @PostMapping("/{id}")
    public ResponseEntity<EntityResponse<?>> createService(@PathVariable int id,
            @Valid @RequestBody CreateServiceDTO request,
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
                    .body(new EntityResponse<>(false, "Você não tem permissão para criar serviços em outro usuário!",
                            null));
        }

        int newServiceId = serviceService.createService(id, request);

        EntityResponse<?> createResponse = new EntityResponse<>(
                true,
                "Serviço criado com sucesso!",
                newServiceId);

        return ResponseEntity.status(HttpStatus.CREATED).body(createResponse);
    }

    @PatchMapping("/{id}/{serviceId}")
    public ResponseEntity<EntityResponse<?>> editService(@PathVariable int id,
            @PathVariable int serviceId,
            @Valid @RequestBody EditServiceDTO request,
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
                    .body(new EntityResponse<>(false,
                            "Você não tem permissão para atualizar serviços de outro usuário!", null));
        }

        serviceService.editService(id, serviceId, request);

        EntityResponse<ServiceModel> editResponse = new EntityResponse<>(
                true,
                "Serviço do ID: " + serviceId + " Pertencente ao profissional do ID: " + id
                        + " Atualizado com sucesso!",
                null);

        return ResponseEntity.status(HttpStatus.OK).body(editResponse);
    }

    @DeleteMapping("/{id}/{serviceId}")
    public ResponseEntity<EntityResponse<?>> deleteService(@PathVariable int id,
            @PathVariable int serviceId,
            HttpServletRequest servletReq) {

        Integer idFromToken = (Integer) servletReq.getAttribute("userId");

        if (idFromToken == null) {
            throw new ApiException("Usuário não autenticado ou token inválido!", HttpStatus.UNAUTHORIZED);
        }

        if (id != idFromToken) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new EntityResponse<>(false, "Você não tem permissão para excluir serviços de outro usuário!",
                            null));
        }

        serviceService.deleteService(id, serviceId);

        EntityResponse<?> deleteResponse = new EntityResponse<>(
                true,
                "Serviço do ID: " + serviceId + "excluído com sucesso!",
                null);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(deleteResponse);
    }

    //
    // @GetMapping("/buscar/nome/{nome}")
    // public List<ServiceModel> buscarPorNome(@PathVariable String name) {
    // return service.buscarPorNome(name);
    // }
}
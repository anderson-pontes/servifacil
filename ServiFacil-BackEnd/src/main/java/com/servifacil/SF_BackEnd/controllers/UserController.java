package com.servifacil.SF_BackEnd.controllers;

import com.servifacil.SF_BackEnd.dto.CreateUserDTO;
import com.servifacil.SF_BackEnd.dto.UserLoginDTO;
import com.servifacil.SF_BackEnd.dto.EditUserDTO;
import com.servifacil.SF_BackEnd.models.UserModel;
import com.servifacil.SF_BackEnd.responses.LoginResponse;
import com.servifacil.SF_BackEnd.security.JwtUtil;
import com.servifacil.SF_BackEnd.services.UserService;
import com.servifacil.SF_BackEnd.exceptions.ApiException;
import com.servifacil.SF_BackEnd.responses.EntityResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<EntityResponse<?>> createUser(@Valid @RequestBody CreateUserDTO request,
                                                          BindingResult bindingResult) {

        // Se houver erros de validação, lança ApiException
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            throw new ApiException(errors, HttpStatus.BAD_REQUEST);
        }

        userService.createUser(request);

        EntityResponse<?> createResponse = new EntityResponse<>(
                true,
                "Usuário cadastrado com sucesso!",
                request.getEmail()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(createResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse<UserModel>> userLogin(@Valid @RequestBody UserLoginDTO request,
                                                         BindingResult bindingResult) {

        // Se houver erros de validação, lança ApiException
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            throw new ApiException(errors, HttpStatus.BAD_REQUEST);
        }

        UserModel user = userService.userLogin(request);

        String token = jwtUtil.generateToken(user.getUserId(), user.getUserType(), user.getEmail());

        LoginResponse<UserModel> response = new LoginResponse<>(
                true,
                "Login realizado com sucesso!",
                token
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityResponse<?>> getUser(@PathVariable int id,
//                                                     BindingResult bindingResult,
                                                     HttpServletRequest servletReq){

//        if (bindingResult.hasErrors()) {
//            String errors = bindingResult.getFieldErrors()
//                    .stream()
//                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
//                    .collect(Collectors.joining(", "));
//            throw new ApiException(errors, HttpStatus.BAD_REQUEST);
//        }

        Integer idFromToken = (Integer) servletReq.getAttribute("userId");

        if (idFromToken == null) {
            throw new ApiException("Usuário não autenticado ou token inválido!", HttpStatus.UNAUTHORIZED);
        }

        if(id != idFromToken){
            EntityResponse<?> invalidId = new EntityResponse<>(
                    false,
                    "Você não tem permissão para buscar outro usuário!",
                    null
            );
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(invalidId);
        }

        UserModel user = userService.getUser(id);

        EntityResponse<UserModel> response = new EntityResponse<>(
                true,
                "Usuário carregado com sucesso!",
                user
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityResponse<?>> userUpdate(@PathVariable int id,
                                                        @Valid @RequestBody EditUserDTO request,
                                                        BindingResult bindingResult,
                                                        HttpServletRequest servletReq){

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

        if(id != idFromToken){
            EntityResponse<?> invalidId = new EntityResponse<>(
                    false,
                    "Você não tem permissão para atualizar outro usuário!",
                    null
            );
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(invalidId);
        }

        userService.updateUser(id, request);

        EntityResponse<?> response = new EntityResponse<>(
                true,
                "Dados atualizados com sucesso!",
                null
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
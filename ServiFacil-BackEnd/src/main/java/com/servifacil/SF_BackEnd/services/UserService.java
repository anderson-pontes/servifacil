package com.servifacil.SF_BackEnd.services;

import com.servifacil.SF_BackEnd.dto.CreateUserDTO;
import com.servifacil.SF_BackEnd.dto.UserLoginDTO;
import com.servifacil.SF_BackEnd.dto.EditUserDTO;
import com.servifacil.SF_BackEnd.models.UserModel;
import com.servifacil.SF_BackEnd.repositories.UserRepository;
import com.servifacil.SF_BackEnd.exceptions.ApiException;
import com.servifacil.SF_BackEnd.utils.MergeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void createUser(CreateUserDTO request) {

        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new ApiException("Email já cadastrado!", HttpStatus.CONFLICT);
        }

        if (!MergeUtils.validBirthDate(request.getBirthDate())) {
            throw new ApiException("É necessário ser maior de idade!", HttpStatus.BAD_REQUEST);
        }

        // CRIPTOGRAFIA DA SENHA
        request.setUserPassword(passwordEncoder.encode(request.getUserPassword()));

        userRepository.spInsertUser(
                request.getUserName(),
                request.getEmail(),
                request.getUserPassword(),
                request.getCpf(),
                request.getRg(),
                request.getTelephone(),
                request.getCnpj(),
                request.getBirthDate(),
                request.getUserType(),
                request.getProfession(),
                request.getProfilePhoto(),
                request.getZipCode(),
                request.getStreet(),
                request.getHouseNumber(),
                request.getComplement(),
                request.getNeighborhood(),
                request.getCity(),
                request.getState());
    }

    @Transactional
    public UserModel userLogin(UserLoginDTO request) {

        String normalizedEmail = request.getEmail().trim().toLowerCase();

        UserModel user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException("Usuário não encontrado!", HttpStatus.NOT_FOUND));
        System.out.println("Usuário encontrado no banco: " + user.getUserId() + " - " + user.getEmail());

        if (!passwordEncoder.matches(request.getUserPassword(), user.getUserPassword())) {
            throw new ApiException("Senha incorreta!", HttpStatus.BAD_REQUEST);
        }

        return user;
    }

    @Transactional
    public UserModel getUser(int userId) {

        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("Usuário não encontrado!", HttpStatus.NOT_FOUND));

        return user;
    }

    @Transactional
    public void updateUser(int userId, EditUserDTO request) {

        UserModel existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("Usuário não encontrado!", HttpStatus.NOT_FOUND));

        String email = request.getEmail().trim().toLowerCase();

        // Verificar se o email já existe, mas excluir o próprio usuário da verificação
        UserModel userWithEmail = userRepository.findByEmail(email).orElse(null);
        if (userWithEmail != null && userWithEmail.getUserId() != userId) {
            throw new ApiException("Email indisponível, use outro!", HttpStatus.CONFLICT);
        }

        if (request == null) {
            throw new ApiException("Ao menos um campo deve ser preenchido!", HttpStatus.BAD_REQUEST);
        }

        // Validar data de nascimento apenas se foi fornecida
        if (request.getBirthDate() != null && !MergeUtils.validBirthDate(request.getBirthDate())) {
            throw new ApiException("É necessário ser maior de idade!", HttpStatus.BAD_REQUEST);
        }

        // Só criptografar senha se foi fornecida (não vazia)
        String passwordToSave = existingUser.getUserPassword(); // Manter senha atual por padrão
        if (request.getUserPassword() != null && !request.getUserPassword().trim().isEmpty()) {
            passwordToSave = passwordEncoder.encode(request.getUserPassword());
        }
        request.setUserPassword(passwordToSave);

        try {
            userRepository.spUpdateUser(
                    userId,
                    request.getUserName(),
                    request.getEmail(),
                    request.getUserPassword(),
                    request.getCpf(),
                    request.getRg(),
                    request.getTelephone(),
                    request.getCnpj(),
                    request.getBirthDate(),
                    request.getUserType(),
                    request.getProfession(),
                    request.getProfilePhoto(),
                    request.getZipCode(),
                    request.getStreet(),
                    request.getHouseNumber(),
                    request.getComplement(),
                    request.getNeighborhood(),
                    request.getCity(),
                    request.getState());
        } catch (Exception e) {
            System.err.println("Erro ao chamar spUpdateUser: " + e.getMessage());
            e.printStackTrace();
            throw new ApiException("Erro ao atualizar usuário: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
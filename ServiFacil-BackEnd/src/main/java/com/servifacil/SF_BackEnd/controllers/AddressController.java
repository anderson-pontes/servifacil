package com.servifacil.SF_BackEnd.controllers;

import com.servifacil.SF_BackEnd.dto.AddressDTO;
import com.servifacil.SF_BackEnd.services.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    public final AddressService addressService;

    public AddressController(AddressService addressService){
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<String> createAddress(@RequestBody AddressDTO dto) {
        int newId = addressService.createAddress(dto);
        return ResponseEntity.ok("Endereço criado com ID: " + newId);
    }
}
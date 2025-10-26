package com.servifacil.SF_BackEnd.services;

import com.servifacil.SF_BackEnd.dto.AddressDTO;
import com.servifacil.SF_BackEnd.repositories.AddressRepository;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    public final AddressRepository addressRepository;
    public AddressService(AddressRepository addressRepository){
        this.addressRepository = addressRepository;
    }

    public int createAddress(AddressDTO dto) { // ← Deve receber apenas o DTO
        System.out.println("DADOS CHEGANDO: " + dto.getZipCode());

        Integer newAddressId = addressRepository.spInsertAddress(
                dto.getZipCode(),
                dto.getStreet(),
                dto.getHouseNumber(),
                dto.getComplement(),
                dto.getNeighborhood(),  // ← Corrigi: era getWeightMethod()
                dto.getCity(),
                dto.getState()
        );

        System.out.println("NOVO ID DO ENDEREÇO: " + newAddressId);
        return newAddressId != null ? newAddressId : 0;
    }
}

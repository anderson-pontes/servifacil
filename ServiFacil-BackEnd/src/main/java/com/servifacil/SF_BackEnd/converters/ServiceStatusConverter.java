package com.servifacil.SF_BackEnd.converters;

import com.servifacil.SF_BackEnd.models.ServiceModel;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ServiceStatusConverter implements AttributeConverter<ServiceModel.ServiceStatus, String> {

    @Override
    public String convertToDatabaseColumn(ServiceModel.ServiceStatus serviceStatus) {
        return (serviceStatus != null) ? serviceStatus.getDisplayName() : null;
    }

    @Override
    public ServiceModel.ServiceStatus convertToEntityAttribute(String dbValue) {
        return (dbValue != null) ? ServiceModel.ServiceStatus.fromDisplayName(dbValue) : null;
    }
}
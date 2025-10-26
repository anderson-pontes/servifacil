package com.servifacil.SF_BackEnd.converters;

import com.servifacil.SF_BackEnd.models.AppointmentModel;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AppointmentStatusConverter implements AttributeConverter<AppointmentModel.AppointmentStatus, String> {

    @Override
    public String convertToDatabaseColumn(AppointmentModel.AppointmentStatus appointmentStatus) {
        return (appointmentStatus != null) ? appointmentStatus.getDisplayName() : null;
    }

    @Override
    public AppointmentModel.AppointmentStatus convertToEntityAttribute(String dbValue) {
        return (dbValue != null) ? AppointmentModel.AppointmentStatus.fromDisplayName(dbValue) : null;
    }
}
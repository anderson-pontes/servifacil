package com.servifacil.SF_BackEnd.converters;

import com.servifacil.SF_BackEnd.models.UserModel;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserTypeConverter implements AttributeConverter<UserModel.UserType, String> {

    @Override
    public String convertToDatabaseColumn(UserModel.UserType userType) {
        return (userType != null) ? userType.getDisplayName() : null;
    }

    @Override
    public UserModel.UserType convertToEntityAttribute(String dbValue) {
        return (dbValue != null) ? UserModel.UserType.fromDisplayName(dbValue) : null;
    }
}
package com.trainshier.converter;

import com.trainshier.enums.UserRole;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserRoleConverter implements AttributeConverter<UserRole, String> {

    @Override
    public String convertToDatabaseColumn(UserRole attribute) {
        if (attribute == null) return null;
        switch (attribute) {
            case APPRENTICE: return "aprendiz";
            case INSTRUCTOR: return "instructor";
            case ADMINISTRATOR: return "administrador";
            default: throw new IllegalArgumentException("Unknown role: " + attribute);
        }
    }

    @Override
    public UserRole convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        switch (dbData.toLowerCase()) {
            case "aprendiz": return UserRole.APPRENTICE;
            case "instructor": return UserRole.INSTRUCTOR;
            case "administrador": return UserRole.ADMINISTRATOR;
            default: throw new IllegalArgumentException("Unknown db role: " + dbData);
        }
    }
}

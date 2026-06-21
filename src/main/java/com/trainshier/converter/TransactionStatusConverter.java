package com.trainshier.converter;

import com.trainshier.enums.TransactionStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TransactionStatusConverter implements AttributeConverter<TransactionStatus, String> {

    @Override
    public String convertToDatabaseColumn(TransactionStatus attribute) {
        if (attribute == null) return null;
        switch (attribute) {
            case COMPLETED: return "COMPLETADA";
            case FAILED: return "FALLIDA";
            case PENDING: return "PENDIENTE";
            default: throw new IllegalArgumentException("Unknown status: " + attribute);
        }
    }

    @Override
    public TransactionStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        switch (dbData.toUpperCase()) {
            case "COMPLETADA": return TransactionStatus.COMPLETED;
            case "FALLIDA": return TransactionStatus.FAILED;
            case "PENDIENTE": return TransactionStatus.PENDING;
            default: throw new IllegalArgumentException("Unknown db status: " + dbData);
        }
    }
}

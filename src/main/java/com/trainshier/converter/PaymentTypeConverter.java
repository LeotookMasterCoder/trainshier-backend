package com.trainshier.converter;

import com.trainshier.enums.PaymentType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PaymentTypeConverter implements AttributeConverter<PaymentType, String> {

    @Override
    public String convertToDatabaseColumn(PaymentType attribute) {
        if (attribute == null) return null;
        switch (attribute) {
            case CASH: return "EFECTIVO";
            case CARD: return "TARJETA";
            default: throw new IllegalArgumentException("Unknown payment type: " + attribute);
        }
    }

    @Override
    public PaymentType convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        switch (dbData.toUpperCase()) {
            case "EFECTIVO": return PaymentType.CASH;
            case "TARJETA": return PaymentType.CARD;
            default: throw new IllegalArgumentException("Unknown db payment type: " + dbData);
        }
    }
}

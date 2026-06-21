package com.trainshier.converter;

import com.trainshier.enums.PromotionType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PromotionTypeConverter implements AttributeConverter<PromotionType, String> {

    @Override
    public String convertToDatabaseColumn(PromotionType attribute) {
        if (attribute == null) return null;
        switch (attribute) {
            case PRODUCT: return "PRODUCTO";
            case TOTAL: return "TOTAL";
            default: throw new IllegalArgumentException("Unknown promotion type: " + attribute);
        }
    }

    @Override
    public PromotionType convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        switch (dbData.toUpperCase()) {
            case "PRODUCTO": return PromotionType.PRODUCT;
            case "TOTAL": return PromotionType.TOTAL;
            default: throw new IllegalArgumentException("Unknown db promotion type: " + dbData);
        }
    }
}

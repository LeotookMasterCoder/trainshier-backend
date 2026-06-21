package com.trainshier.converter;

import com.trainshier.enums.InventoryMovementType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class InventoryMovementTypeConverter implements AttributeConverter<InventoryMovementType, String> {

    @Override
    public String convertToDatabaseColumn(InventoryMovementType attribute) {
        if (attribute == null) return null;
        switch (attribute) {
            case ENTRY: return "ENTRADA";
            case EXIT: return "SALIDA";
            case ADJUSTMENT: return "AJUSTE";
            default: throw new IllegalArgumentException("Unknown movement type: " + attribute);
        }
    }

    @Override
    public InventoryMovementType convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        switch (dbData.toUpperCase()) {
            case "ENTRADA": return InventoryMovementType.ENTRY;
            case "SALIDA": return InventoryMovementType.EXIT;
            case "AJUSTE": return InventoryMovementType.ADJUSTMENT;
            default: throw new IllegalArgumentException("Unknown db movement type: " + dbData);
        }
    }
}

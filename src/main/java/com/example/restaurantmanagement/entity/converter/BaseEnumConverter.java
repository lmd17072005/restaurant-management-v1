package com.example.restaurantmanagement.entity.converter;

import com.example.restaurantmanagement.entity.enums.DbValueEnum;
import jakarta.persistence.AttributeConverter;

public abstract class BaseEnumConverter<E extends Enum<E> & DbValueEnum>
        implements AttributeConverter<E, String> {

    private final Class<E> enumClass;

    protected BaseEnumConverter(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public String convertToDatabaseColumn(E attribute) {
        return attribute == null ? null : attribute.getDbValue();
    }

    @Override
    public E convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        for (E e : enumClass.getEnumConstants()) {
            if (e.getDbValue().equals(dbData)) return e;
        }
        throw new IllegalArgumentException(
                "Unknown DB value: '" + dbData + "' for enum " + enumClass.getSimpleName()
        );
    }
}
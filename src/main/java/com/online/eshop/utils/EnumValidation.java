package com.online.eshop.utils;

import java.util.Set;

public class EnumValidation {
    public static <E extends Enum<E>> boolean isValidLabel(Set<String> labels, Class<E> enumClass) throws IllegalArgumentException {
        return labels.stream().allMatch(s -> Enum.valueOf(enumClass, s.toUpperCase()) != null);
    }
}

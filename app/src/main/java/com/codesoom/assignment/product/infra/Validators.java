package com.codesoom.assignment.product.infra;

import java.util.Objects;

public interface Validators {

    static boolean isValidString(String str) {
        return Objects.nonNull(str) && !str.isBlank();
    }

    static boolean isValidNumber(Number number) {
        return Objects.nonNull(number) && number.doubleValue() >= 0;
    }
}

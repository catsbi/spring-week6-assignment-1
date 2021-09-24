package com.codesoom.assignment.product.provider;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

import static com.codesoom.assignment.product.ConstantsForProductTest.PRODUCT_IMAGE_URL;
import static com.codesoom.assignment.product.ConstantsForProductTest.PRODUCT_MAKER;
import static com.codesoom.assignment.product.ConstantsForProductTest.PRODUCT_NAME;
import static com.codesoom.assignment.product.ConstantsForProductTest.PRODUCT_PRICE;

public class ProvideInvalidProductArguments implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {

        return Stream.of(
                Arguments.of("", PRODUCT_MAKER, PRODUCT_PRICE, PRODUCT_IMAGE_URL),
                Arguments.of(null, PRODUCT_MAKER, PRODUCT_PRICE, PRODUCT_IMAGE_URL),
                Arguments.of(PRODUCT_NAME, "", PRODUCT_PRICE, PRODUCT_IMAGE_URL),
                Arguments.of(PRODUCT_NAME, null, PRODUCT_PRICE, PRODUCT_IMAGE_URL),
                Arguments.of(PRODUCT_NAME, PRODUCT_MAKER, -3000, PRODUCT_IMAGE_URL),
                Arguments.of(PRODUCT_NAME, PRODUCT_MAKER, PRODUCT_PRICE, ""),
                Arguments.of(PRODUCT_NAME, PRODUCT_MAKER, PRODUCT_PRICE, null)
        );
    }
}

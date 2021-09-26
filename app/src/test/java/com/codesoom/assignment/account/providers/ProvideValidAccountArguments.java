package com.codesoom.assignment.account.providers;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class ProvideValidAccountArguments implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of("catsbi@email.com", "catsbi", "q1w2e3"),
                Arguments.of("crong@email.com", "crong", "123411"),
                Arguments.of("nana@email.com", "nana", "p1p1p1"),
                Arguments.of("maxtill@email.com", "maxtill", "maxtill22!"),
                Arguments.of("codesoom@email.com", "codesoom", "CodeSoom112!")
        );
    }
}

package com.codesoom.assignment.account.providers;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class ProvideInvalidAccountArguments implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of("", "catsbi", "q1w2e3"),
                Arguments.of(null, "catsbi", "q1w2e3"),
                Arguments.of("crong@email.com", "", "123411"),
                Arguments.of("crong@email.com", null, "123411"),
                Arguments.of("nana@email.com", "nana", ""),
                Arguments.of("nana@email.com", "nana", null)
        );
    }
}

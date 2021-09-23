package com.codesoom.assignment.todo.providers;

import com.codesoom.assignment.todo.domain.Task;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class provideVariousTypesTaskList implements ArgumentsProvider {
    private final List<Task> tasks = Arrays.asList(
            Task.from("test1"),
            Task.from("test2"),
            Task.from("test3")
    );

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of(ArrayListProvider()),
                Arguments.of(HashSetProvider()),
                Arguments.of(HashMapProvider())
        );
    }

    private Collection<Task> HashMapProvider() {
        final Map<String, Task> collect = tasks.stream()
                .collect(Collectors.toMap(Task::getTitle, task -> task));

        return collect.values();
    }

    private HashSet<Task> HashSetProvider() {
        return new HashSet<>(tasks);
    }

    private List<Task> ArrayListProvider() {
        return new ArrayList<>(tasks);
    }


}

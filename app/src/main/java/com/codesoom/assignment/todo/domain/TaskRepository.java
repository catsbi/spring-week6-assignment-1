package com.codesoom.assignment.todo.domain;

import java.util.List;
import java.util.Optional;

/**
 * 할 일을 보관한다.
 */
public interface TaskRepository {
    List<Task> findAll();

    Optional<Task> findById(Long id);

    <S extends Task> List<S> saveAll(Iterable<S> entities);

    void deleteAll();

    void delete(Task task);

    Task save(Task task);
}

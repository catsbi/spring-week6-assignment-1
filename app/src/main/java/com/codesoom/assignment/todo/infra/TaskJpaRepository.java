package com.codesoom.assignment.todo.infra;

import com.codesoom.assignment.todo.domain.Task;
import com.codesoom.assignment.todo.domain.TaskRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Primary
public interface TaskJpaRepository extends JpaRepository<Task, Long>, TaskRepository {
    List<Task> findAll();

    Optional<Task> findById(Long id);
}

package com.codesoom.assignment.todo.application;


import com.codesoom.assignment.common.convertors.EntitySupplier;
import com.codesoom.assignment.todo.domain.Task;
import com.codesoom.assignment.todo.domain.TaskRepository;
import com.codesoom.assignment.todo.errors.InvalidTaskTitleException;
import com.codesoom.assignment.todo.errors.TaskNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 할 일을 관리한다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {
    private final TaskRepository taskRepository;

    /**
     * 할 일 목록을 조회한다.
     *
     * @return 할 일 목록
     */
    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * 할 일 상세 정보를 조회한다.
     *
     * @param id 할 일의 식별자
     * @return 할 일 상세 정보
     * @throws TaskNotFoundException 해당 식별자 할 일이 없는 경우
     */
    public Task findTask(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    /**
     * 할 일을 등록한다.
     *
     * @param supplier 등록 할 할 일 정보
     * @return 등록된 할 일 상세정보
     * @throws InvalidTaskTitleException 할 일 내용이 유효하지 않는 경우
     */
    public Task createTask(EntitySupplier<Task> supplier) {
        Task newTask = supplier.toEntity();

        return taskRepository.save(newTask);
    }

    /**
     * 할 일 내용을 수정한다.
     *
     * @param supplier 수정 할 할 일 정보
     * @return 수정된 할 일 상세정보
     * @throws InvalidTaskTitleException 할 일 내용이 유효하지 않는 경우
     */
    @Transactional
    public Task updateTask(Long id, EntitySupplier<Task> supplier) {
        final Task foundTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        final Task targetTask = supplier.toEntity();

        foundTask.update(targetTask);

        return foundTask;
    }

    /**
     * 등록된 할 일을 삭제한다.
     *
     * @param id 삭제할 할 일 식별자
     * @return 삭제된 할 일 상세정보
     * @throws TaskNotFoundException 해당 식별자 할 일이 없는 경우
     */
    public Task deleteTask(Long id) {
        final Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        taskRepository.delete(task);

        return task;
    }
}

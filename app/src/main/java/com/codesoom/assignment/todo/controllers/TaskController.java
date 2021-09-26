package com.codesoom.assignment.todo.controllers;

import com.codesoom.assignment.todo.application.TaskService;
import com.codesoom.assignment.todo.domain.Task;
import com.codesoom.assignment.todo.dto.TaskData;
import com.codesoom.assignment.todo.errors.InvalidTaskTitleException;
import com.codesoom.assignment.todo.errors.TaskNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 할 일에 대한 HTTP Request 요청을 처리한다.
 */
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;


    /**
     * 모든 할 일 목록을 조회한다.
     *
     * @return 할 일 목록
     */
    @GetMapping
    public List<TaskData> findAllTask() {
        final List<Task> foundTasks = taskService.findAllTasks();

        if (foundTasks.isEmpty()) {
            return Collections.emptyList();
        }

        return taskService.findAllTasks().stream()
                .map(TaskData::of)
                .collect(Collectors.toList());
    }

    /**
     * 할 일을 조회한다.
     *
     * @param id 할 일 식별자
     * @return 할 일 상세정보
     * @throws TaskNotFoundException 할 일을 찾을 수 없는 경우
     */
    @GetMapping("{id}")
    public TaskData findTask(@PathVariable long id) {
        final Task foundTask = taskService.findTask(id);

        return TaskData.of(foundTask);
    }

    /**
     * 할 일을 등록한다.
     *
     * @param taskData 할 일 정보
     * @return 등록된 할 일 정보
     * @throws InvalidTaskTitleException 할 일 정보가 유효하지 않은 경우
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskData createTask(@RequestBody TaskData taskData) {
        final Task savedTask = taskService.createTask(taskData);

        return TaskData.of(savedTask);
    }

    /**
     * 할 일을 수정한다.
     *
     * @param id       수정 할 할일 식별자
     * @param taskData 할 일
     * @return 수정된 할 일 정보
     * @throws TaskNotFoundException     할 일을 찾을 수 없는 경우
     * @throws InvalidTaskTitleException 할 일 정보가 유효하지 않은 경우
     */
    @RequestMapping(path = "{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public TaskData updateTask(@PathVariable long id, @RequestBody TaskData taskData) {
        final Task updatedTask = taskService.updateTask(id, taskData);
        return TaskData.of(updatedTask);
    }

    /**
     * 할 일을 삭제한다.
     *
     * @param id 삭제 할 할 일 식별자
     * @throws TaskNotFoundException 할 일을 찾을 수 없는 경우
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable long id) {
        taskService.deleteTask(id);
    }
}

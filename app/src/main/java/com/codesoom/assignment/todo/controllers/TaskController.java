package com.codesoom.assignment.todo.controllers;

import com.codesoom.assignment.todo.application.TaskService;
import com.codesoom.assignment.todo.domain.Task;
import com.codesoom.assignment.todo.dto.TaskData;
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

    @GetMapping("{id}")
    public TaskData findTask(@PathVariable long id) {
        final Task foundTask = taskService.findTask(id);

        return TaskData.of(foundTask);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskData createTask(@RequestBody TaskData taskData) {
        final Task savedTask = taskService.createTask(taskData);

        return TaskData.of(savedTask);
    }

    @RequestMapping(path = "{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public TaskData updateTask(@PathVariable long id, @RequestBody TaskData taskData) {
        final Task updatedTask = taskService.updateTask(id, taskData);
        return TaskData.of(updatedTask);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable long id) {
        taskService.deleteTask(id);
    }
}

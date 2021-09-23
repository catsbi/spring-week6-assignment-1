package com.codesoom.assignment.todo.dto;

import com.codesoom.assignment.common.convertors.EntitySupplier;
import com.codesoom.assignment.todo.domain.Task;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class TaskData implements EntitySupplier<Task> {

    private Long id;

    private String title;

    public TaskData(String title) {
        this(null, title);
    }

    public TaskData(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public static TaskData of(Task task) {
        return new TaskData(task.getId(), task.getTitle());
    }

    @Override
    public Task toEntity() {
        return Task.from(title);
    }
}

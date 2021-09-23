package com.codesoom.assignment.todo.dto;

import com.codesoom.assignment.common.convertors.EntitySupplier;
import com.codesoom.assignment.todo.domain.Task;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class TaskData implements EntitySupplier<Task> {
    private String title;

    public TaskData(String title) {
        this.title = title;
    }

    @Override
    public Task toEntity() {
        return Task.from(title);
    }
}

package com.codesoom.assignment.todo.domain;

import com.codesoom.assignment.todo.errors.InvalidTaskTitleException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

/**
 * 할 일 정보
 */
@Entity
@Getter
@NoArgsConstructor
public class Task {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    public Task(String title) {
        if (isInvalidTitle(title)) {
            throw new InvalidTaskTitleException();
        }
        this.title = title;
    }

    public Task(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public static Task from(String title) {
        if (isInvalidTitle(title)) {
            throw new InvalidTaskTitleException();
        }

        return new Task(title);
    }

    private static boolean isInvalidTitle(String title) {
        return Objects.isNull(title) || title.isBlank();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(title, task.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    public void update(Task target) {
        if (isInvalidTitle(target.getTitle())) {
            throw new InvalidTaskTitleException();
        }

        title = target.title;
    }
}

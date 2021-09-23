package com.codesoom.assignment.todo.domain;

import com.codesoom.assignment.todo.errors.InvalidTaskTitleException;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    public Task(String title) {
        this(null, title);
    }

    public Task(Long id, String title) {
        if (isInvalidTitle(title)) {
            throw new InvalidTaskTitleException();
        }

        this.id = id;
        this.title = title;
    }

    public static Task from(String title) {
        return new Task(null, title);
    }

    private static boolean isInvalidTitle(String title) {
        return Objects.isNull(title) || title.isBlank();
    }

    public void update(Task target) {
        if (isInvalidTitle(target.getTitle())) {
            throw new InvalidTaskTitleException();
        }

        title = target.title;
    }
}

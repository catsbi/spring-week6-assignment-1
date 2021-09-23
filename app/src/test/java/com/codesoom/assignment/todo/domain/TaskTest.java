package com.codesoom.assignment.todo.domain;

import com.codesoom.assignment.todo.errors.InvalidTaskTitleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Task 클래스")
class TaskTest {

    @Nested
    @DisplayName("생성자는")
    class Describe_constructor {
        @DisplayName("인자가 정상적인 경우, Task 객체가 생성된다.")
        @ParameterizedTest
        @ValueSource(strings = {"test", "test1", "catsbi", "crong", "밥 먹고, 공부하기", "복습한 뒤 포스팅하기",
                "밥먹고 공부하다가, 모르는 내용은 구글링 해본 뒤, 톡방에 물어보고 그래도 답이 안나오면 리뷰어분들에게 요청해본 뒤 정리"})
        void createTaskWithValidTitle(String title) {
            final Task newTask = new Task(title);

            assertThat(newTask.getTitle()).isEqualTo(title);
            assertThat(newTask).isEqualTo(new Task(title));
        }

        @DisplayName("인자가 비정상인 경우, 예외를 던진다.")
        @ParameterizedTest
        @NullAndEmptySource
        void createTaskWithInvalidTitle(String invalidTitle) {
            assertThatThrownBy(() -> new Task(invalidTitle))
                    .isInstanceOf(InvalidTaskTitleException.class);
        }
    }

    @Nested
    @DisplayName("from 메서드는")
    class Describe_from {
        @DisplayName("인자가 정상적인 경우, Task 객체가 생성된다.")
        @ParameterizedTest
        @ValueSource(strings = {"test", "test1", "catsbi", "crong", "밥 먹고, 공부하기", "복습한 뒤 포스팅하기",
                "밥먹고 공부하다가, 모르는 내용은 구글링 해본 뒤, 톡방에 물어보고 그래도 답이 안나오면 리뷰어분들에게 요청해본 뒤 정리"})
        void createTaskWithValidTitle(String title) {
            final Task newTask = Task.from(title);

            assertThat(newTask.getTitle()).isEqualTo(title);
            assertThat(newTask).isEqualTo(Task.from(title));
        }

        @DisplayName("인자가 비정상인 경우, 예외를 던진다.")
        @ParameterizedTest
        @NullAndEmptySource
        void createTaskWithInvalidTitle(String invalidTitle) {
            assertThatThrownBy(() -> new Task(invalidTitle))
                    .isInstanceOf(InvalidTaskTitleException.class);
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Describe_update {
        private static final String TITLE = "밥먹기";

        private Task originTask;

        private List<Task> validTasks;
        private Task invalidTask;

        @BeforeEach
        void setUp() {
            originTask = Task.from(TITLE);

            validTasks = Arrays.asList(
                    Task.from("test"),
                    Task.from("test1"),
                    Task.from("catsbi"),
                    Task.from("crong"),
                    Task.from("밥 먹고, 공부하기"),
                    Task.from("복습한 뒤 포스팅하기"),
                    Task.from("밥먹고 공부하다가, 모르는 내용은 구글링 해본 뒤, 톡방에 물어보고 그래도 답이 안나오면 리뷰어분들에게 요청해본 뒤 정리")
            );

            invalidTask = new Task();
        }

        @DisplayName("인자가 유효하면, 업데이트를 수행한다.")
        @Test
        void updateTaskWithValidTitle() {
            for (Task validTask : validTasks) {
                originTask.update(validTask);

                assertThat(originTask.getTitle()).isEqualTo(validTask.getTitle());
            }
        }

        @DisplayName("인자가 유효하지 않으면, 예외를 던진다")
        @ParameterizedTest
        @NullAndEmptySource
        void updateTaskWithInvalidTitle(String invalidTitle) {
            ReflectionTestUtils.setField(invalidTask, "title", invalidTitle);

            assertThatThrownBy(()-> originTask.update(invalidTask))
                    .isInstanceOf(InvalidTaskTitleException.class);
        }
    }

}

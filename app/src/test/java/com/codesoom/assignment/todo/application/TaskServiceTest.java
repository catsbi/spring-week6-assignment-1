package com.codesoom.assignment.todo.application;

import com.codesoom.assignment.todo.domain.Task;
import com.codesoom.assignment.todo.domain.TaskRepository;
import com.codesoom.assignment.todo.dto.TaskData;
import com.codesoom.assignment.todo.errors.InvalidTaskTitleException;
import com.codesoom.assignment.todo.errors.TaskNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TaskService 클래스")
@DataJpaTest
class TaskServiceTest {

    @Autowired
    private TaskRepository taskRepository;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService(taskRepository);
    }

    @DisplayName("findAllTasks 메서드는")
    @Nested
    class Describe_findAllTasks {
        @DisplayName("할 일이 존재 한다면")
        @Nested
        class Context_with_exists_tasks {
            private List<Task> tasks;

            @BeforeEach
            void setUp() {
                tasks = IntStream.rangeClosed(1, 10)
                        .mapToObj(index -> Task.from("할 일" + index))
                        .collect(Collectors.toList());

                tasks = taskRepository.saveAll(tasks);
            }

            @DisplayName("할 일들이 담긴 목록을 반환 한다.")
            @Test
            void findAllTasksWithExistsTasks() {
                final List<Task> foundTasks = taskService.findAllTasks();

                assertThat(foundTasks.size()).isEqualTo(tasks.size());
            }
        }

        @DisplayName("할 일이 존재하지 않는다면")
        @Nested
        class Context_without_tasks {
            @BeforeEach
            void setUp() {
                taskRepository.deleteAll();
            }

            @DisplayName("비어어있는 목록을 반환한다.")
            @Test
            void findAllTasksWithoutTasks() {
                final List<Task> foundTasks = taskService.findAllTasks();

                assertThat(foundTasks).isEmpty();
            }
        }

    }

    @DisplayName("findTask 메서드는")
    @Nested
    class Describe_findTask {
        private Task task;

        @BeforeEach
        void setUp() {
            task = taskRepository.save(Task.from("할 일1"));
        }

        @DisplayName("존재하는 할 일의 식별자를 조회하면, 할 일 상세정보를 조회한다.")
        @Test
        void findTaskWithExistsId() {
            final Task foundTask = taskService.findTask(task.getId());

            assertThat(foundTask.getId()).isEqualTo(task.getId());
            assertThat(foundTask.getTitle()).isEqualTo(task.getTitle());
        }

        @DisplayName("존재하지 않는 할 일의 식별자를 조회하면, 예외를 던진다.")
        @Test
        void findTaskWithNotExistsId() {
            assertThatThrownBy(() -> taskService.findTask(100L))
                    .isInstanceOf(TaskNotFoundException.class);
        }
    }

    @DisplayName("createTask 메서드는")
    @Nested
    class Describe_createTask {

        @DisplayName("등록 하고자 하는 할 일 내용이 유효한 경우, 저장되며 저장된 할 일을 반환한다.")
        @ParameterizedTest
        @ValueSource(strings = {"할 일1", "할 일2", "할 일3", "이것은 아주 길고 긴 할 일 할 일 할 일 할 일 4"})
        void createValidTask(String title) {
            final Task savedTask = taskService.createTask(new TaskData(title));

            assertThat(savedTask.getTitle()).isEqualTo(title);

        }

        @DisplayName("등록 하고자 하는 할 일 내용이 유효하지 않은 경우, 예외를 던진다.")
        @ParameterizedTest
        @NullAndEmptySource
        void createInvalidTask(String invalidTitle) {
            final TaskData taskData = new TaskData(invalidTitle);

            assertThatThrownBy(() -> taskService.createTask(taskData))
                    .isInstanceOf(InvalidTaskTitleException.class);
        }
    }

    @DisplayName("updateTask 메서드는")
    @Nested
    class Describe_updateTask {

        @DisplayName("수정 하고자 하는 할 일의 식별자가 존재할 경우")
        @Nested
        class Context_with_exists_task {
            private Task task;

            @BeforeEach
            void setUp() {
                task = taskRepository.save(Task.from("할 일1"));
            }

            @DisplayName("수정할 내용이 유효 하면, 할 일의 내용이 수정 된다.")
            @ParameterizedTest
            @ValueSource(strings = {"할 일1", "할 일2", "할 일3", "이것은 아주 길고 긴 할 일 할 일 할 일 할 일 4"})
            void updateWithValidData(String title) {
                final TaskData taskData = new TaskData(title);

                final Task updatedTask = taskService.updateTask(this.task.getId(), taskData);

                assertThat(updatedTask.getId()).isEqualTo(task.getId());
                assertThat(updatedTask.getTitle()).isEqualTo(title);

            }

            @DisplayName("수정할 내용이 유효 하지 않다면, 예외를 던진다.")
            @ParameterizedTest
            @NullAndEmptySource
            void updateWithInvalidData(String invalidTitle) {
                final TaskData taskData = new TaskData(invalidTitle);

                assertThatThrownBy(() -> taskService.updateTask(task.getId(), taskData))
                        .isInstanceOf(InvalidTaskTitleException.class);
            }

        }

        @DisplayName("수정 하고자 하는 할 일의 식별자가 존재 하지 않는 경우")
        @Nested
        class Context_with_not_exists_task {
            @DisplayName("예외를 던진다.")
            @Test
            void updateWithNotExistsId() {

                assertThatThrownBy(() -> taskService.updateTask(100L, new TaskData("할 일")))
                        .isInstanceOf(TaskNotFoundException.class);
            }
        }

    }

    @DisplayName("deleteTask 메서드는")
    @Nested
    class Describe_deleteTask {
        private Task task;

        @BeforeEach
        void setUp() {
            task = taskRepository.save(Task.from("할 일"));
        }

        @DisplayName("삭제 하려는 할 일의 식별자가 존재할 경우, 삭제되며 삭제된 할 일 정보가 반환된다.")
        @Test
        void deleteWithExistsTask() {
            final Task deletedTask = taskService.deleteTask(this.task.getId());

            assertThat(deletedTask).isEqualTo(task);

            assertThatThrownBy(() -> taskService.findTask(task.getId()))
                    .isInstanceOf(TaskNotFoundException.class);

        }

        @DisplayName("삭제 하려는 할 일의 식별자가 존재하지 않을 경우, 예외를 던진다.")
        @Test
        void deleteWithNotExistsTask() {
            assertThatThrownBy(() -> taskService.deleteTask(100L))
                    .isInstanceOf(TaskNotFoundException.class);
        }

    }

}

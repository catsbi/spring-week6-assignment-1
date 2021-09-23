package com.codesoom.assignment.todo.domain;

import com.codesoom.assignment.todo.providers.provideVariousTypesTaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TaskRepository 클래스")
@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @DisplayName("findAll 메서드는")
    @Nested
    class Describe_findAll {
        @DisplayName("할 일이 있을 경우")
        @Nested
        class Context_with_exists_tasks {
            private static final int MIN_SIZE = 1;
            private static final int MAX_SIZE = 10;
            private List<Task> tasks;

            @BeforeEach
            void setUp() {
                tasks = IntStream.rangeClosed(MIN_SIZE, MAX_SIZE)
                        .mapToObj(index -> Task.from("taskTitle" + index))
                        .collect(Collectors.toList());

                taskRepository.saveAll(tasks);
            }

            @DisplayName("할 일 목록을 반환한다.")
            @Test
            void findAll() {
                final List<Task> foundTasks = taskRepository.findAll();

                assertThat(foundTasks.size()).isEqualTo(MAX_SIZE);
                assertThat(foundTasks).containsAll(tasks);
            }
        }

        @DisplayName("할 일이 없는 경우")
        @Nested
        class Context_without_tasks {
            @BeforeEach
            void setUp() {
                taskRepository.deleteAll();
            }

            @DisplayName("비어있는 목록을 반환한다.")
            @Test
            void findAll() {
                final List<Task> foundTasks = taskRepository.findAll();

                assertThat(foundTasks).isEmpty();
            }
        }
    }

    @DisplayName("findById 메서드는")
    @Nested
    class Describe_findById {

        @DisplayName("조회하고자 하는 식별자가 존재 할 경우")
        @Nested
        class Context_exists_id {
            private Task task;

            @BeforeEach
            void setUp() {
                task = Task.from("밥먹기");

                task = taskRepository.save(task);
            }

            @DisplayName("할 일이 담긴 Optional이 반환된다.")
            @Test
            void findByExistsTask() {
                final Optional<Task> foundTaskOpt = taskRepository.findById(task.getId());

                assertThat(foundTaskOpt)
                        .isPresent()
                        .contains(task);
            }
        }

        @DisplayName("조회하고자 하는 식별자가 존재하지 않는 경우")
        @Nested
        class Context_not_exists_id {
            @DisplayName("비어있는 Optional이 반환된다.")
            @Test
            void findByNotExistsTask() {
                final Optional<Task> foundTaskOpt = taskRepository.findById(999L);

                assertThat(foundTaskOpt).isNotPresent();
            }
        }
    }

    @DisplayName("saveAll 메서드는")
    @Nested
    class Describe_saveAll {
        @DisplayName("할 일 목록이 Iterable의 구현체면 저장한다.")
        @ParameterizedTest
        @ArgumentsSource(provideVariousTypesTaskList.class)
        void saveAll(Iterable<Task> it){
            final List<Task> savedTasks = taskRepository.saveAll(it);

            for (Task task : it) {
                assertThat(savedTasks).contains(task);
            }
        }

    }

    @DisplayName("save 메서드는")
    @Nested
    class Describe_save {
        private Task task;

        @BeforeEach
        void setUp() {
            task = taskRepository.save(Task.from("할 일 1"));
        }

        @DisplayName("저장하려는 할 일의 식별자가 없는 경우, 새로운 식별자를 발급하며 저장한다.")
        @Test
        void saveTask() {
            final Task task = new Task("할 일");
            final Task savedTask = taskRepository.save(task);

            assertThat(savedTask.getTitle()).isEqualTo(task.getTitle());
            assertThat(savedTask.getId()).isNotNull();
        }

        @DisplayName("저장하려는 할 일의 식별자가 있는 경우")
        @Nested
        class Context_with_id {
            @DisplayName("저장소에 해당 식별자가 존재하지 않는다면, 신규 할 일로 등록된다.")
            @Test
            void saveWithNotRegisterTask() {
                final Task task = new Task(10L, "할 일");

                final Task savedTask = taskRepository.save(task);


                assertThat(taskRepository.findById(task.getId())).isNotPresent();
                assertThat(taskRepository.findById(savedTask.getId()))
                        .isPresent()
                        .contains(savedTask);
                assertThat(savedTask.getId()).isNotEqualTo(task.getId());
            }

            @DisplayName("저장소에 해당 식별자가 존재한다면, 신규 할 일로 덮어쓰기된다.")
            @Test
            void saveWithExistsTask() {
                final Task newTask = new Task(task.getId(), "새로운 할 일1");
                final Task savedTask = taskRepository.save(newTask);

                assertThat(savedTask.getId()).isEqualTo(newTask.getId());
                assertThat(savedTask.getTitle()).isEqualTo(newTask.getTitle());
            }
        }


    }

    @DisplayName("deleteAll 메서드는")
    @Nested
    class Describe_deleteAll {
        private static final int MIN_SIZE = 1;
        private static final int MAX_SIZE = 10;
        private List<Task> tasks;

        @BeforeEach
        void setUp() {
            tasks = IntStream.rangeClosed(MIN_SIZE, MAX_SIZE)
                    .mapToObj(index -> Task.from("taskTitle" + index))
                    .collect(Collectors.toList());

            taskRepository.saveAll(tasks);
        }

        @DisplayName("모든 할 일을 삭제한다.")
        @Test
        void deleteAll() {
            List<Task> foundTasks = taskRepository.findAll();
            assertThat(foundTasks.size()).isEqualTo(tasks.size());

            taskRepository.deleteAll();


            foundTasks = taskRepository.findAll();
            assertThat(foundTasks).isEmpty();
        }
    }

    @DisplayName("delete 메서드는")
    @Nested
    class Describe_delete {
        private Task task;

        @BeforeEach
        void setUp() {
            task = taskRepository.save(Task.from("할 일"));
        }

        @DisplayName("삭제 하고자 하는 할 일이 존재 여부와 상관없이 동작한다.")
        @Test
        void deleteWithExistsTask() {
            taskRepository.delete(task);
            taskRepository.delete(Task.from("할 일 2"));

            final Optional<Task> foundTaskOpt = taskRepository.findById(task.getId());

            assertThat(foundTaskOpt).isNotPresent();
        }
    }
}

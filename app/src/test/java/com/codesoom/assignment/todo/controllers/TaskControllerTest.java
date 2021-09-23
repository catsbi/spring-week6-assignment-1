package com.codesoom.assignment.todo.controllers;

import com.codesoom.assignment.todo.domain.Task;
import com.codesoom.assignment.todo.domain.TaskRepository;
import com.codesoom.assignment.todo.dto.TaskData;
import com.codesoom.assignment.todo.errors.InvalidTaskTitleException;
import com.codesoom.assignment.todo.errors.TaskNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TaskController 클래스")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TaskControllerTest {

    public static final String API = "/tasks";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    private ObjectMapper objectMapper;

    private List<Task> tasks;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        taskRepository.deleteAll();

        tasks = IntStream.rangeClosed(1, 10)
                .mapToObj(index -> Task.from("할 일" + index))
                .collect(Collectors.toList());

        tasks = taskRepository.saveAll(tasks);
    }

    @DisplayName("GET /tasks API는")
    @Nested
    class Describe_get_tasks {
        @DisplayName("200 상태코드와 할 일 목록을 반환한다.")
        @Test
        void getTasks() throws Exception {
            mockMvc.perform(
                            get(API).accept(MediaType.APPLICATION_JSON_UTF8)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(tasks.size())));
        }

        @DisplayName("200 상태코드와 비어있는 할 일 목록을 반환한다.")
        @Test
        void getTasksWithAllDeleted() throws Exception {
            taskRepository.deleteAll();

            mockMvc.perform(
                            get(API).accept(MediaType.APPLICATION_JSON_UTF8)
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @DisplayName("GET /tasks/{id} API는")
    @Nested
    class Describe_get_tasks_id {

        @DisplayName("존재하는 할 일의 식별자를 조회할 경우, 200 상태코드와 할 일 상세정보를 반환한다.")
        @Test
        void findByExistsId() throws Exception {
            for (Task task : tasks) {
                final String detailApi = API + "/" + task.getId();
                mockMvc.perform(get(detailApi).accept(MediaType.APPLICATION_JSON_UTF8))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.title").value(task.getTitle()));

            }
        }

        @DisplayName("존재하지 않는 할 일의 식별자를 조회할 경우, 404 상태코드를 반환한다.")
        @ParameterizedTest
        @ValueSource(longs = {Long.MIN_VALUE, Long.MAX_VALUE, 100L, 101L, 102L, 999L, 9999L})
        void findByNotExistsId(Long id) throws Exception {
            final String detailApi = API + "/" + id;
            mockMvc.perform(get(detailApi).accept(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isNotFound());
        }
    }

    @DisplayName("POST /tasks API는")
    @Nested
    class Describe_post_tasks {

        @DisplayName("저장하려는 할 일이 유효할 경우")
        @Nested
        class Context_with_valid_tasks {

            @BeforeEach
            void setUp() {
                taskRepository.deleteAll();
            }

            @DisplayName("201 상태코드와 생성된 할 일을 반환한다.")
            @Test
            void createTask() throws Exception {
                for (Task task : tasks) {
                    final byte[] contentBody = objectMapper.writeValueAsBytes(task);

                    mockMvc.perform(
                                    post(API)
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .accept(MediaType.APPLICATION_JSON_UTF8)
                                            .content(contentBody)
                            ).andExpect(status().isCreated())
                            .andExpect(jsonPath("$.title").value(task.getTitle()));
                }

            }
        }
    }

    @DisplayName("PUT/PATCH /tasks/{id} API는")
    @Nested
    class Describe_put_or_patch_tasks {

        @DisplayName("존재하는 할 일을 수정하려는 경우")
        @Nested
        class Context_with_exists_task {
            @DisplayName("수정하려는 정보가 유효한 경우, 200 상태코드와 수정된 할 일을 반환한다.")
            @ParameterizedTest
            @ValueSource(strings = {"test", "test1", "catsbi", "crong", "밥 먹고, 공부하기", "복습한 뒤 포스팅하기",
                    "밥먹고 공부하다가, 모르는 내용은 구글링 해본 뒤, 톡방에 물어보고 그래도 답이 안나오면 리뷰어분들에게 요청해본 뒤 정리"})
            void updateTask(String title) throws Exception {
                final TaskData taskData = new TaskData(title);
                final byte[] content = objectMapper.writeValueAsBytes(taskData);

                final Task existedTask = tasks.get(0);

                mockMvc.perform(
                                patch(API + "/" + existedTask.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON_UTF8)
                                        .content(content)
                        ).andExpect(status().isOk())
                        .andExpect(jsonPath("$.title").value(title))
                        .andExpect(jsonPath("$.id").value(existedTask.getId()));

            }

            @DisplayName("수정하려는 정보가 유효하지 않은 경우, 400 상태코드를 반환한다.")
            @ParameterizedTest
            @NullAndEmptySource
            void updateTaskWithInvalidTitle(String invalidTitle) throws Exception {
                final TaskData taskData = new TaskData(invalidTitle);
                final byte[] content = objectMapper.writeValueAsBytes(taskData);

                final Task existedTask = tasks.get(0);

                mockMvc.perform(
                                patch(API + "/" + existedTask.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON_UTF8)
                                        .content(content)
                        ).andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message")
                                .value(InvalidTaskTitleException.DEFAULT_MESSAGE));
            }
        }

        @DisplayName("존재하지 않는 할 일을 수정하려는 경우")
        @Nested
        class Context_with_not_exists_task {

            @DisplayName("404 상태코드를 반환한다.")
            @ParameterizedTest
            @ValueSource(longs = {Long.MIN_VALUE, Long.MAX_VALUE, 999L, 1999L, 5000L})
            void updateTaskWithNotExistsId(Long notExistsId) throws Exception {
                final TaskData taskData = new TaskData("존재하지 않는 할 일");
                final byte[] content = objectMapper.writeValueAsBytes(taskData);

                mockMvc.perform(
                                patch(API + "/" + notExistsId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON_UTF8)
                                        .content(content)
                        ).andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.message")
                                .value(String.format(TaskNotFoundException.DEFAULT_MESSAGE, notExistsId)));
            }
        }

    }

    @DisplayName("DELETE /tasks/{id} API")
    @Nested
    class Describe_delete_tasks {
        @DisplayName("존재하는 할 일의 식별자로 삭제하려고 하면, 204 상태코드를 반환한다.")
        @Test
        void deleteTaskWithExistsId() throws Exception {
            for (Task task : tasks) {
                mockMvc.perform(delete(API + "/" + task.getId()))
                        .andExpect(status().isNoContent());
            }
        }

        @DisplayName("존재하지 않는 할 일의 식별자로 삭제하려고 하면, 404 상태코드를 반환한다.")
        @ParameterizedTest
        @ValueSource(longs = {Long.MIN_VALUE, Long.MAX_VALUE, 999L, 1999L, 5000L})
        void deleteTaskWithNotExistsId(Long notExistsId) throws Exception {
            mockMvc.perform(delete(API + "/" + notExistsId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message")
                            .value(String.format(TaskNotFoundException.DEFAULT_MESSAGE, notExistsId)));
        }

    }

}

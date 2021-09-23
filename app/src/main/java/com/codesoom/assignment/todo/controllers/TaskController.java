package com.codesoom.assignment.todo.controllers;

import com.codesoom.assignment.todo.application.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * TODO 구현해야 할 항목
 *  * 1. 할 일 목록 얻기 - GET /tasks
 *  * 2. 할 일 상세 조회 - GET /tasks/{id}
 *  * 3. 할 일 생성 하기 - POST /tasks
 *  * 4. 할 일 수정 하기 - PUT/PATCH /tasks/{id}
 *  * 5. 할 일 삭제 하기 - DELETE /tasks/{id}
 */

/**
 * 할 일에 대한 HTTP Request 요청을 처리한다.
 */
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
}

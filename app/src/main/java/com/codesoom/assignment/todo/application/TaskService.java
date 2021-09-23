package com.codesoom.assignment.todo.application;


import com.codesoom.assignment.common.convertors.EntitySupplier;
import com.codesoom.assignment.todo.domain.Task;
import com.codesoom.assignment.todo.domain.TaskRepository;
import com.codesoom.assignment.todo.errors.InvalidTaskTitleException;
import com.codesoom.assignment.todo.errors.TaskNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 할 일을 관리한다.
 */
@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    /**
     * 할 일 목록을 조회한다.
     *
     * @return 할 일 목록
     */
    public List<Task> findAllTasks(){
        return null;
    }

    /**
     * 할 일 상세 정보를 조회한다.
     *
     * @param id 할 일의 식별자
     * @return 할 일 상세 정보
     * @throws TaskNotFoundException 해당 식별자 할 일이 없는 경우
     */
    public Task findTask(Long id){
        return null;
    }

    /**
     * 할 일을 등록한다.
     *
     * @param supplier 등록 할 할 일 정보
     * @return 등록된 할 일 상세정보
     * @throws InvalidTaskTitleException 할 일 내용이 유효하지 않는 경우
     */
    public Task createTask(EntitySupplier supplier){
        return null;
    }

    /**
     * 할 일 내용을 수정한다.
     *
     * @param supplier 수정 할 할 일 정보
     * @return 수정된 할 일 상세정보
     * @throws InvalidTaskTitleException 할 일 내용이 유효하지 않는 경우
     */
    public Task updateTask(Long id, EntitySupplier supplier){
        return null;
    }

    /**
     * 등록된 할 일을 삭제한다.
     *
     * @param id 삭제할 할 일 식별자
     * @return 삭제된 할 일 상세정보
     * @throws TaskNotFoundException 해당 식별자 할 일이 없는 경우
     */
    public Task deleteTask(Long id){
        return null;
    }
}

package com.javaee.springbootodo.service;

import com.javaee.springbootodo.entity.TodoEntity;
import java.util.List;

// Service for handling TodoEntity operations
public interface TodoService {

    // Retrieve all Todo entries
    List<TodoEntity> findAll();

    List<TodoEntity> findAllByUserId(int theUserId);

    List<TodoEntity> findCompltdByUserId(int theUserId);

    List<TodoEntity> findUnCompltByUserId(int theUserId);

    // Retrieve a Todo entry by its ID
    TodoEntity findById(int theId);

    // Save a Todo entry (create or update)
    TodoEntity save(TodoEntity theTodo);

    // Delete a Todo entry by its ID
    void deleteById(int theId);

    //获取userid
    int getUserId(TodoEntity theTodo);

    //获取id
    int getId(TodoEntity theTodo);
}
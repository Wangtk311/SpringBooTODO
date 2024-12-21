package com.javaee.springbootodo.service;

import com.javaee.springbootodo.entity.TodoEntity;
import com.javaee.springbootodo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Service implementation for handling TodoEntity operations
@Service
public class TodoServiceImpl implements TodoService {

    private TodoRepository todoRepository;

    // Constructor-based dependency injection
    @Autowired
    public TodoServiceImpl(TodoRepository theTodoRepository) {
        todoRepository = theTodoRepository;
    }

    // Retrieve all Todo entries
    @Override
    public List<TodoEntity> findAll() {
        return todoRepository.findAll();
    }

    @Override
    public List<TodoEntity> findAllByUserId(int theUserId) {
        return todoRepository.findAllByUserid(theUserId);
    }

    @Override
    public List<TodoEntity> findCompltdByUserId(int theUserId) {

        return todoRepository.findAllByUseridAndCompletedTrue(theUserId);
    }

    @Override
    public List<TodoEntity> findUnCompltByUserId(int theUserId) {
        return todoRepository.findAllByUseridAndCompletedFalse(theUserId);
    }

    // Retrieve a Todo entry by its ID
    @Override
    public TodoEntity findById(int theId) {
        Optional<TodoEntity> result = todoRepository.findById(theId);

        TodoEntity theTodo = null;

        if (result.isPresent()) {
            theTodo = result.get();
        }
        else {
            // Handle the case where the Todo entry is not found
            throw new RuntimeException("Did not find todo id - " + theId);
        }
        return theTodo;
    }

    // Save or update a Todo entry
    @Override
    public TodoEntity save(TodoEntity theTodo) {
        return todoRepository.save(theTodo);
    }

    // Delete a Todo entry by its ID
    @Override
    public void deleteById(int theId) {
        todoRepository.deleteById(theId);
    }

    @Override
    public int getUserId(TodoEntity theTodo) {
        return theTodo.getUserid();
    }

    @Override
    public int getId(TodoEntity theTodo) {
        return theTodo.getId();
    }
}
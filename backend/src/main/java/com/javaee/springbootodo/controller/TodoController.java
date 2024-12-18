package com.javaee.springbootodo.controller;

import com.javaee.springbootodo.entity.TodoEntity;
import com.javaee.springbootodo.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Controller for managing TodoEntity entities
@RestController
@CrossOrigin(origins = "http://localhost:5173/")  // Allows CORS requests from the specified origin
public class TodoController {

    // Service for handling Todo operations
    private TodoService todoService;

    // Constructor-based dependency injection
    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // Endpoint to add a new Todo
    @PostMapping("/todo")
    public ResponseEntity<String> addTodo(@Valid @RequestBody TodoEntity todoEntity, BindingResult bindingResult) {

        // Check if there are validation errors
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors occurred.");
        }
        // Save the todoEntity object to the database using the service layer
        todoService.save(todoEntity);
        // Return a successful response indicating that the todo was added
        return ResponseEntity.ok("Todo Added Successfully.");
    }

    // Endpoint to retrieve a Todo by its ID
    @GetMapping("/todo/{id}")
    public TodoEntity getTodoById(@PathVariable("id") int id) {
        return todoService.findById(id);
    }

    // Endpoint to retrieve all Todos
    @GetMapping("/todo")
    public List<TodoEntity> getTodos() {
        return todoService.findAll();
    }

    // Endpoint to update an existing Todo
    @PutMapping("/todo")
    public ResponseEntity<TodoEntity> updateTodo(@Valid @RequestBody TodoEntity todoEntity, BindingResult bindingResult) {
        // Check if there are validation errors
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(null); // Or handle errors as needed
        }
        // Save the updated todoEntity object using the service layer
        TodoEntity updatedTodo = todoService.save(todoEntity);
        return ResponseEntity.ok(updatedTodo);
    }

    // Endpoint to delete a Todo by its ID
    @DeleteMapping("/todo/{id}")
    public String deleteTodo(@PathVariable("id") int id) {
        todoService.deleteById(id);
        return "Todo with Id "+id +" was deleted.";
    }
}
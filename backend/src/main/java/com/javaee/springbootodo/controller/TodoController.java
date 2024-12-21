package com.javaee.springbootodo.controller;

import com.javaee.springbootodo.entity.TodoEntity;
import com.javaee.springbootodo.security.JwtUtils;
import com.javaee.springbootodo.service.TodoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Controller for managing TodoEntity entities
@RestController
@CrossOrigin(origins ={"http://localhost:5173/", "http://47.99.66.142:5173/"})  // Allows CORS requests from the specified origin
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
    public ResponseEntity<String> addTodo(@Valid @RequestBody TodoEntity todoEntity,  BindingResult bindingResult, HttpServletRequest request) {

        // 从请求头中获取Token
        String header = request.getHeader("Authorization");
        //System.out.println("header.startsWith(\"Bearer \")");
        String token = header.substring(7); // 去除"Bearer "前缀

        // 从Token中提取用户名
        String usernameFromToken = JwtUtils.getUsernameFromTokenAsymmetric(token);

        // 根据用户名获取userid
        int userIdFromToken = Integer.parseInt((usernameFromToken));

        int userid = todoService.getUserId(todoEntity);
        // 判断token中的userid和todoEntity中的userid是否一致
        if (userIdFromToken == userid) {
            // Check if there are validation errors
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body("Validation errors occurred.");
            }
            // Save the todoEntity object to the database using the service layer
            todoService.save(todoEntity);
            // Return a successful response indicating that the todo was added
            return ResponseEntity.ok("Todo Added Successfully.");
        }

        return ResponseEntity.badRequest().body("User errors occurred.");
    }

    // Endpoint to retrieve a Todo by its ID
    @GetMapping("/todo/{id}")
    public TodoEntity getTodoById(@PathVariable("id") int id, int userid, HttpServletRequest request) {
        // 从请求头中获取Token
        String header = request.getHeader("Authorization");
        //System.out.println("header.startsWith(\"Bearer \")");
        String token = header.substring(7); // 去除"Bearer "前缀

        // 从Token中提取用户名
        String usernameFromToken = JwtUtils.getUsernameFromTokenAsymmetric(token);

        // 根据用户名获取userid
        int userIdFromToken = Integer.parseInt((usernameFromToken));

        // 判断token中的userid和请求参数中的userid是否一致
        if (userIdFromToken == userid) {
            // 获取待办事项
            TodoEntity todo = todoService.findById(id);
            if(todo != null){
                int uid = todoService.getUserId(todo);
                if (uid == userid) {
                    System.out.println("id: " + id);
                    return todo;
                }
            }
        }

        return null;
    }


    // Endpoint to retrieve all Todos
    @GetMapping("/todo")
    public List<TodoEntity> getTodos(int userid, HttpServletRequest request) {
        // 从请求头中获取Token
        String header = request.getHeader("Authorization");
        //System.out.println("header.startsWith(\"Bearer \")");
        String token = header.substring(7); // 去除"Bearer "前缀

        // 从Token中提取用户名
        String usernameFromToken = JwtUtils.getUsernameFromTokenAsymmetric(token);

        // 根据用户名获取userid
        int userIdFromToken = Integer.parseInt((usernameFromToken));

        // 判断token中的userid和请求参数中的userid是否一致
        if (userIdFromToken == userid) {
            return todoService.findAllByUserId(userid);
        }
        return null;

    }


    @GetMapping("/todocompleted")
    public List<TodoEntity> getTodosComptld(int userid, HttpServletRequest request) {
        // 从请求头中获取Token
        String header = request.getHeader("Authorization");
        //System.out.println("header.startsWith(\"Bearer \")");
        String token = header.substring(7); // 去除"Bearer "前缀

        // 从Token中提取用户名
        String usernameFromToken = JwtUtils.getUsernameFromTokenAsymmetric(token);

        // 根据用户名获取userid
        int userIdFromToken = Integer.parseInt((usernameFromToken));

        // 判断token中的userid和请求参数中的userid是否一致
        if (userIdFromToken == userid) {
            return todoService.findCompltdByUserId(userid);
        }
        return null;

    }

    @GetMapping("/todouncomplete")
    public List<TodoEntity> getTodosUnComptle(int userid, HttpServletRequest request) {
        // 从请求头中获取Token
        String header = request.getHeader("Authorization");
        //System.out.println("header.startsWith(\"Bearer \")");
        String token = header.substring(7); // 去除"Bearer "前缀

        // 从Token中提取用户名
        String usernameFromToken = JwtUtils.getUsernameFromTokenAsymmetric(token);

        // 根据用户名获取userid
        int userIdFromToken = Integer.parseInt((usernameFromToken));

        // 判断token中的userid和请求参数中的userid是否一致
        if (userIdFromToken == userid) {
            return todoService.findUnCompltByUserId(userid);
        }
        return null;

    }

    // Endpoint to update an existing Todo
    @PutMapping("/todo")
    public ResponseEntity<TodoEntity> updateTodo(@Valid @RequestBody TodoEntity todoEntity, BindingResult bindingResult, HttpServletRequest request) {

        // 从请求头中获取Token
        String header = request.getHeader("Authorization");
        //System.out.println("header.startsWith(\"Bearer \")");
        String token = header.substring(7); // 去除"Bearer "前缀

        // 从Token中提取用户名
        String usernameFromToken = JwtUtils.getUsernameFromTokenAsymmetric(token);

        // 根据用户名获取userid
        int userIdFromToken = Integer.parseInt((usernameFromToken));

        int userid = todoService.getUserId(todoEntity);
        // 判断token中的userid和todoEntity中的userid是否一致
        if (userIdFromToken == userid) {
            int id = todoService.getId(todoEntity);
            TodoEntity todo = todoService.findById(id);
            if(todo != null){
                int uid = todoService.getUserId(todo);
                if (uid == userid) {
                    // Check if there are validation errors
                    if (bindingResult.hasErrors()) {
                        return ResponseEntity.badRequest().body(null); // Or handle errors as needed
                    }
                    // Save the updated todoEntity object using the service layer
                    TodoEntity updatedTodo = todoService.save(todoEntity);
                    return ResponseEntity.ok(updatedTodo);
                }
            }

        }

        return ResponseEntity.badRequest().body(null);
    }

    // Endpoint to delete a Todo by its ID
    @DeleteMapping("/todo/{id}")
    public String deleteTodo(@PathVariable("id") int id,int userid, HttpServletRequest request) {


        // 从请求头中获取Token
        String header = request.getHeader("Authorization");
        //System.out.println("header.startsWith(\"Bearer \")");
        String token = header.substring(7); // 去除"Bearer "前缀

        // 从Token中提取用户名
        String usernameFromToken = JwtUtils.getUsernameFromTokenAsymmetric(token);

        // 根据用户名获取userid
        int userIdFromToken = Integer.parseInt((usernameFromToken));

        // 判断token中的userid和请求参数中的userid是否一致
        if (userIdFromToken == userid) {
            // 获取待办事项
            TodoEntity todo = todoService.findById(id);
            if(todo != null){
                int uid = todoService.getUserId(todo);
                if (uid == userid) {
                    todoService.deleteById(id);
                    return "Todo with Id "+id +" was deleted.";
                }
            }

        }

        return "User/todo errors occurred.";
    }
}
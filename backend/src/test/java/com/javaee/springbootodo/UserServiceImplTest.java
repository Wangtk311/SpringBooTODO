package com.javaee.springbootodo;

import com.javaee.springbootodo.entity.TodoEntity;
import com.javaee.springbootodo.entity.UserEntity;
import com.javaee.springbootodo.repository.TodoRepository;
import com.javaee.springbootodo.repository.UserRepository;
import com.javaee.springbootodo.service.TodoServiceImpl;
import com.javaee.springbootodo.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Class for Unit tests for TodoServiceImpl class
public class UserServiceImplTest {

    // Mock the userRepository to simulate database operations
    @Mock
    private UserRepository userRepository;

    // Inject the mocked userRepository into the TodoServiceImpl
    @InjectMocks
    private UserServiceImpl userService;

    // Initialize mocks before each test
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Test the findAll method of TodoServiceImpl
    @Test
    void testFindAll() {
        // Create sample TodoEntity objects
        UserEntity todo1 = new UserEntity("pwd1", "name1");
        UserEntity todo2 = new UserEntity("pwd2", "name2");

        // Mock the behavior of todoRepository.findAll() to return the sample Todos
        when(userRepository.findAll()).thenReturn(Arrays.asList(todo1, todo2));

        // Call the findAll method of the service
        List<UserEntity> todos = userService.findAll();

        // Verify that the result is not null and contains the expected number of Todos
        assertNotEquals(todos, null);
        assertEquals(2, todos.size());
    }

    // Test the findById method of TodoServiceImpl
    @Test
    void testFindById() {
        // Create a sample userEntity object
        UserEntity user = new UserEntity("pwd", "name");

        // Mock the behavior of userRepository.findById() to return the sample Todo wrapped in Optional
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Call the findById method of the service
        UserEntity result = userService.findById(1);

        // Verify that the result is not null and has the expected title
        assertNotNull(result);
        assertEquals("pwd", result.getPassword());

    }

    // Test the save method of TodoServiceImpl
    @Test
    void testSave() {
        // Create a sample userEntity object
        UserEntity user = new UserEntity("pwd", "name");

        // Mock the behavior of userRepository.save() to return the same Todo
        when(userRepository.save(user)).thenReturn(user);

        // Call the save method of the service
        UserEntity result = userService.save(user);

        // Verify that the result is not null and has the expected title
        assertNotNull(result);
        assertEquals("pwd", result.getPassword());
    }

    // Test the deleteById method of TodoServiceImpl
    @Test
    void testDeleteById() {
        // Create a sample userEntity object with ID 1
        UserEntity todo = new UserEntity("pwd", "name");
        todo.setId(1);

        // Mock the behavior of userRepository.findById() to return the sample Todo
        when(userRepository.findById(1)).thenReturn(Optional.of(todo));

        // Call the deleteById method of the service
        userService.deleteById(1);

        // Verify that the deleteById method of the repository was called with the correct ID
        verify(userRepository, times(1)).deleteById(1);

        // Mock the behavior of userRepository.findAll() to return an empty list
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // Check if the repository is empty
        List<UserEntity> users = userService.findAll();
        assertEquals(0, users.size());
    }
}
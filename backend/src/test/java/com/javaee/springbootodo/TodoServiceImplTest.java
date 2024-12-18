package com.javaee.springbootodo;

import com.javaee.springbootodo.entity.TodoEntity;
import com.javaee.springbootodo.repository.TodoRepository;
import com.javaee.springbootodo.service.TodoServiceImpl;
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
public class TodoServiceImplTest {

    // Mock the TodoRepository to simulate database operations
    @Mock
    private TodoRepository todoRepository;

    // Inject the mocked TodoRepository into the TodoServiceImpl
    @InjectMocks
    private TodoServiceImpl todoService;

    // Initialize mocks before each test
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Test the findAll method of TodoServiceImpl
    @Test
    void testFindAll() {
        // Create sample TodoEntity objects
        TodoEntity todo1 = new TodoEntity("Title1", "Description1", "高", LocalDate.now(), false, 111111);
        TodoEntity todo2 = new TodoEntity("Title2", "Description2", "低", LocalDate.now(), true, 111111);

        // Mock the behavior of todoRepository.findAll() to return the sample Todos
        when(todoRepository.findAll()).thenReturn(Arrays.asList(todo1, todo2));

        // Call the findAll method of the service
        List<TodoEntity> todos = todoService.findAll();

        // Verify that the result is not null and contains the expected number of Todos
        assertNotEquals(todos, null);
        assertEquals(2, todos.size());
    }

    // Test the findById method of TodoServiceImpl
    @Test
    void testFindById() {
        // Create a sample TodoEntity object
        TodoEntity todo = new TodoEntity("Title", "Description", "中", LocalDate.now(), false, 111111);

        // Mock the behavior of todoRepository.findById() to return the sample Todo wrapped in Optional
        when(todoRepository.findById(1)).thenReturn(Optional.of(todo));

        // Call the findById method of the service
        TodoEntity result = todoService.findById(1);

        // Verify that the result is not null and has the expected title
        assertNotNull(result);
        assertEquals("Title", result.getTitle());
    }

    // Test the save method of TodoServiceImpl
    @Test
    void testSave() {
        // Create a sample TodoEntity object
        TodoEntity todo = new TodoEntity("Title", "Description", "中", LocalDate.now(), false, 111111);

        // Mock the behavior of todoRepository.save() to return the same Todo
        when(todoRepository.save(todo)).thenReturn(todo);

        // Call the save method of the service
        TodoEntity result = todoService.save(todo);

        // Verify that the result is not null and has the expected title
        assertNotNull(result);
        assertEquals("Title", result.getTitle());
    }

    // Test the deleteById method of TodoServiceImpl
    @Test
    void testDeleteById() {
        // Create a sample TodoEntity object with ID 1
        TodoEntity todo = new TodoEntity("Title", "Description", "中", LocalDate.now(), false, 111111);
        todo.setId(1);

        // Mock the behavior of todoRepository.findById() to return the sample Todo
        when(todoRepository.findById(1)).thenReturn(Optional.of(todo));

        // Call the deleteById method of the service
        todoService.deleteById(1);

        // Verify that the deleteById method of the repository was called with the correct ID
        verify(todoRepository, times(1)).deleteById(1);

        // Mock the behavior of todoRepository.findAll() to return an empty list
        when(todoRepository.findAll()).thenReturn(Collections.emptyList());

        // Check if the repository is empty
        List<TodoEntity> todos = todoService.findAll();
        assertEquals(0, todos.size());
    }
}
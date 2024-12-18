package com.javaee.springbootodo;

import com.javaee.springbootodo.entity.TodoEntity;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

// Class for Unit tests for TodoEntity class
public class TodoEntityTest {

    // Test the creation and initialization of a TodoEntity object using the constructor
    @Test
    void testTodoEntryCreation() {
        LocalDate date = LocalDate.now();
        TodoEntity todo = new TodoEntity("Title", "Description", "高", date, false, 111111);

        assertEquals("Title", todo.getTitle());
        assertEquals("Description", todo.getDescription());
        assertEquals("高", todo.getPriority());
        assertEquals(date, todo.getDate());
        assertFalse(todo.isCompleted());
    }

    // Test the setters of the TodoEntity class
    @Test
    void testTodoEntrySetters() {
        LocalDate date = LocalDate.now();
        TodoEntity todo = new TodoEntity();

        // Set values using setters
        todo.setId(1);
        todo.setTitle("New Title");
        todo.setDescription("New Description");
        todo.setPriority("低");
        todo.setDate(date);
        todo.setCompleted(true);

        // Assert that the values are correctly set
        assertEquals(1, todo.getId());
        assertEquals("New Title", todo.getTitle());
        assertEquals("New Description", todo.getDescription());
        assertEquals("低", todo.getPriority());
        assertEquals(date, todo.getDate());
        assertTrue(todo.isCompleted());
    }
}
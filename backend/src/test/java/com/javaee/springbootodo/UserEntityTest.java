package com.javaee.springbootodo;


import com.javaee.springbootodo.entity.UserEntity;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

// Class for Unit tests for TodoEntity class
public class UserEntityTest {

    // Test the creation and initialization of a TodoEntity object using the constructor
    @Test
    void testUserEntityCreation() {

        UserEntity todo = new UserEntity("Password", "Name");

        assertEquals("Password", todo.getPassword());
        assertEquals("Name", todo.getName());

    }

    // Test the setters of the TodoEntity class
    @Test
    void testUserEntitySetters() {

        UserEntity user = new UserEntity();

        // Set values using setters
        user.setId(222222);
        user.setPassword("NewPassword");
        user.setName("NewName");


        // Assert that the values are correctly set
        assertEquals(222222, user.getId());
        assertEquals("NewPassword", user.getPassword());
        assertEquals("NewName", user.getName());

    }
}
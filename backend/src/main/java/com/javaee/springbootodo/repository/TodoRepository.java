package com.javaee.springbootodo.repository;

import com.javaee.springbootodo.entity.TodoEntry;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository interface for CRUD operations on TodoEntry entities
public interface TodoRepository extends JpaRepository<TodoEntry, Integer> {

}
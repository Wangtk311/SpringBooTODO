package com.javaee.springbootodo.repository;

import com.javaee.springbootodo.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

// Repository interface for CRUD operations on TodoEntity entities
public interface TodoRepository extends JpaRepository<TodoEntity, Integer> {
    List<TodoEntity> findAllByUserid(int theUserId);
    // 添加方法，根据 userid 和 com 属性为 true 查找
    List<TodoEntity> findAllByUseridAndCompletedTrue(int theUserId);

    List<TodoEntity> findAllByUseridAndCompletedFalse(int theUserId);
}
package com.javaee.springbootodo.service;

import com.javaee.springbootodo.entity.UserEntity;

import java.util.List;


public interface UserService {

    // Retrieve all Todo entries
    List<UserEntity> findAll();

    // Retrieve a Todo entry by its ID
    UserEntity findById(int theId);

    // Save a Todo entry (create or update)
    UserEntity save(UserEntity theUser);

    // Delete a Todo entry by its ID
    void deleteById(int theId);
}

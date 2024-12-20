package com.javaee.springbootodo.service;

import com.javaee.springbootodo.entity.UserEntity;

import java.util.List;


public interface UserService {

    // Retrieve all user entries
    List<UserEntity> findAll();

    // Retrieve a user entry by its ID
    UserEntity findById(int theId);

    // Save a user entry (create or update)
    UserEntity save(UserEntity theUser);

    // Delete a user entry by its ID
    void deleteById(int theId);

    //获取userid
    int getId(UserEntity theUser);
}

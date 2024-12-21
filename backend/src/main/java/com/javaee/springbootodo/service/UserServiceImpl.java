package com.javaee.springbootodo.service;

import com.javaee.springbootodo.entity.UserEntity;
import com.javaee.springbootodo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Service implementation for handling TodoEntity operations
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    // Constructor-based dependency injection
    @Autowired
    public UserServiceImpl(UserRepository theuserRepository) {
        userRepository = theuserRepository;
    }

    // Retrieve all Todo entries
    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    // Retrieve a Todo entry by its ID
    @Override
    public UserEntity findById(int theId) {
        Optional<UserEntity> result = userRepository.findById(theId);

        UserEntity theUser = null;

        if (result.isPresent()) {
            theUser = result.get();
        }
        else {
            // Handle the case where the Todo entry is not found
            throw new RuntimeException("Did not find todo id - " + theId);
        }
        return theUser;
    }

    @Override
    public UserEntity save(UserEntity theUser) {
        return userRepository.save(theUser);
    }



    // Delete a Todo entry by its ID
    @Override
    public void deleteById(int theId) {
        userRepository.deleteById(theId);
    }

    @Override
    public int getId(UserEntity theUser) {
        return theUser.getId();
    }

    @Override
    public String getPassword(UserEntity theUser) {
        return theUser.getPassword();
    }

    @Override
    public String GetName(UserEntity theUser) {
        return theUser.getName();
    }
}
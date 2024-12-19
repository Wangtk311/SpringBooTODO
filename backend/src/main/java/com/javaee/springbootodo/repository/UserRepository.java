package com.javaee.springbootodo.repository;

import com.javaee.springbootodo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity getUserById(int id);
}

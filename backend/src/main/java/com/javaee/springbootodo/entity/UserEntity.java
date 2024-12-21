package com.javaee.springbootodo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Entity class for a Todo entry in the database
@Entity
@Table(name="user")
public class UserEntity {

    // Unique identifier for the Todo entry, auto-generated
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    // Title of the Todo entry, must be at least 2 characters long
    @NotNull
    @Size(min=6, message="Too short")
    @Column(name="password")
    private String password;

    // Completion status of the Todo entry
    @Column(name="name")
    private String name;



    // Default constructor for JPA
    public UserEntity() {
    }

    // Constructor to initialize TodoEntity with title, description, priority, date, completion status
    public UserEntity(String password, String name) {
        this.password = password;
        this.name = name;
    }



    // Get the unique identifier of the Todo entry
    public int getId() {
        return id;
    }

    // Set the unique identifier of the Todo entry
    public void setId(int id) {
        this.id = id;
    }



    // Get the password of the Todo entry
    public String getPassword() {
        return password;
    }

    // Set the password of the Todo entry
    public void setPassword(String password) {
        this.password = password;
    }

    // Get the name of the Todo entry
    public String getName() {
        return name;
    }

    // Set the name of the Todo entry
    public void setName(String name) {
        this.name = name;
    }


    // Return a string representation of the TodoEntity object
    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", name='" + name +
                "'}";
    }
}

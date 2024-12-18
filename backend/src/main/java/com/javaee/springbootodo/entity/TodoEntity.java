package com.javaee.springbootodo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

// Entity class for a Todo entry in the database
@Entity
@Table(name="todo")
public class TodoEntity {

    // Unique identifier for the Todo entry, auto-generated
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    // Title of the Todo entry, must be at least 2 characters long
    @NotNull
    @Size(min=2, message="Too short")
    @Column(name="title")
    private String title;

    // Description of the Todo entry, must be at least 3 characters long
    @NotNull
    @Size(min=3, message="Too short")
    @Column(name="description")
    private String description;

    // Priority of the Todo entry, must be not null
    @NotNull
    @Column(name="priority")
    private String priority;

    // Date of the Todo entry, must be not null
    @NotNull
    @Column(name="date")
    private LocalDate date;

    // Completion status of the Todo entry
    @Column(name="completed")
    private boolean completed;

    @NotNull
    @Column(name="userid")
    private int userid;

    // Default constructor for JPA
    public TodoEntity() {
    }

    // Constructor to initialize TodoEntity with title, description, priority, date, completion status
    public TodoEntity(String title, String description, String priority, LocalDate date, boolean completed, int userid) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.date = date;
        this.completed = completed;
        this.userid = userid;
    }

    // Get the date of the Todo entry
    public LocalDate getDate() {
        return date;
    }

    // Set the date of the Todo entry
    public void setDate(LocalDate date) {
        this.date = date;
    }

    // Get the unique identifier of the Todo entry
    public int getId() {
        return id;
    }

    // Set the unique identifier of the Todo entry
    public void setId(int id) {
        this.id = id;
    }

    // Get the title of the Todo entry
    public String getTitle() {
        return title;
    }

    // Set the title of the Todo entry
    public void setTitle(String title) {
        this.title = title;
    }

    // Get the description of the Todo entry
    public String getDescription() {
        return description;
    }

    // Set the description of the Todo entry
    public void setDescription(String description) {
        this.description = description;
    }

    // Get the priority of the Todo entry
    public String getPriority() {
        return priority;
    }

    // Set the priority of the Todo entry
    public void setPriority(String priority) {
        this.priority = priority;
    }

    // Check if the Todo entry is completed
    public boolean isCompleted() {
        return completed;
    }

    // Set the completion status of the Todo entry
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    // Get the userid of the Todo entry
    public int getUserid() {
        return userid;
    }

    // Set the userid of the Todo entry
    public void setUserid(int userid) {
        this.userid = userid;
    }

    // Return a string representation of the TodoEntity object
    @Override
    public String toString() {
        return "TodoEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", completed=" + completed + '\'' +
                ", userid=" + userid +
                '}';
    }
}
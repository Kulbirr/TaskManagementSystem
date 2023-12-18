package com.example.TaskManagementSystem.Exceptions;

import com.example.TaskManagementSystem.Services.TaskService;

public class TaskNotFoundException extends Exception{
    public TaskNotFoundException(String message){
        super(message);
    }
}

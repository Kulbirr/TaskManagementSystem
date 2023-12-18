package com.example.TaskManagementSystem.Transformers;

import com.example.TaskManagementSystem.Entity.Task;
import com.example.TaskManagementSystem.RequestDTOS.AddTaskRequest;

public class AddTaskTransformer {

    public static Task convertAddTaskRequestToEntity(AddTaskRequest addTaskRequest){
        Task taskObj = Task.builder()
                .title(addTaskRequest.getTitle())
                .description(addTaskRequest.getDescription())
                .dueDate(addTaskRequest.getDueDate())
                .user(addTaskRequest.getUser())
                .build();

        return taskObj;
    }
}

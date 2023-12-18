package com.example.TaskManagementSystem.RequestDTOS;

import com.example.TaskManagementSystem.Enum.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateTaskRequest {
    private String title;

    private String description;

    private LocalDate dueDate;

    private TaskStatus status;

}

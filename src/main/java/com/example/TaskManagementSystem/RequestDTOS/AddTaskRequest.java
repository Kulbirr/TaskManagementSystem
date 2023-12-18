package com.example.TaskManagementSystem.RequestDTOS;

import com.example.TaskManagementSystem.Entity.User;
import com.example.TaskManagementSystem.Enum.TaskStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddTaskRequest {
    private String title;

    private String description;

    private LocalDate dueDate;

    private User user;
}

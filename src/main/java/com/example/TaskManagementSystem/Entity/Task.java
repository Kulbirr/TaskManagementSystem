package com.example.TaskManagementSystem.Entity;

import com.example.TaskManagementSystem.Enum.TaskStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    private String title;

    private String description;

    private LocalDate dueDate;

    @Enumerated(value = EnumType.STRING)
    private TaskStatus status;

    @JoinColumn
    @ManyToOne
    private User user;
}

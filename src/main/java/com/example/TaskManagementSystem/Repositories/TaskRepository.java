package com.example.TaskManagementSystem.Repositories;

import com.example.TaskManagementSystem.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long>{
}

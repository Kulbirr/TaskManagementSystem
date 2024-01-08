package com.example.TaskManagementSystem.Controllers;

import com.example.TaskManagementSystem.Entity.Task;
import com.example.TaskManagementSystem.Entity.User;
import com.example.TaskManagementSystem.Enum.Roles;
import com.example.TaskManagementSystem.Exceptions.TaskNotFoundException;
import com.example.TaskManagementSystem.Exceptions.UserNotFoundException;
import com.example.TaskManagementSystem.Exceptions.WrongUserException;
import com.example.TaskManagementSystem.Repositories.UserRepository;
import com.example.TaskManagementSystem.RequestDTOS.AddTaskRequest;
import com.example.TaskManagementSystem.RequestDTOS.UpdateTaskRequest;
import com.example.TaskManagementSystem.Services.TaskService;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    private ResponseEntity addTask(@RequestBody AddTaskRequest addTaskRequest) {
            String result = taskService.addTask(addTaskRequest);
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);

    }

    @GetMapping("/home")
//    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    private String home() {
        return "Welcome to Task Management System";
    }

    @GetMapping("/user-get-tasklist")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    private ResponseEntity getTaskList(Authentication authentication) {
        try {
            User user = userRepository.findByUserName(authentication.getName());
            if(authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(Roles.ROLE_USER.name())))
            {
                List<Task> taskList = taskService.getTaskList(user.getId(), user.getUserName());
                return new ResponseEntity<>(taskList, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("You are not authorized to view this page", HttpStatus.UNAUTHORIZED);
            }
        } catch (UserNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }catch(WrongUserException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/admin-get-allTasks")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    private ResponseEntity getallTasks( @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(defaultValue = "id") String sortBy,
                                        @RequestParam(defaultValue = "ASC") String sortOrder,
                                        Authentication authentication) {

        page = Math.max(0, page);
        size = Math.min(100, Math.max(1, size)); // Limit size to a reasonable range
        Sort.Direction direction = Sort.Direction.fromString(sortOrder.toUpperCase());

        if(authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(Roles.ROLE_ADMIN.name()))){
            Sort sort = Sort.by(direction, sortBy);
            Pageable pageable = (Pageable) PageRequest.of(page, size, sort);
            Page<Task> taskPage = taskService.getAllTasks(pageable);

            return new ResponseEntity<>(taskPage.getContent(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("You are not authorized to view this page", HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/update-task/{taskId}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    private ResponseEntity updateTask(@PathVariable Long taskId, @RequestBody UpdateTaskRequest updateTaskRequest, Authentication authentication) {
        try {
            if(authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(Roles.ROLE_ADMIN.name())) ||
                    authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(Roles.ROLE_USER.name()))){
                Task updatedtask = taskService.updateTask(taskId, updateTaskRequest, authentication.getName());
                return new ResponseEntity<>(updatedtask, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("You are not authorized to view this page", HttpStatus.UNAUTHORIZED);
            }

        }catch (TaskNotFoundException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }catch(WrongUserException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/mark-as-completed/{taskId}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    private ResponseEntity markTaskAsCompleted(@PathVariable Long taskId, Authentication authentication) {
        try {
            if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(Roles.ROLE_ADMIN.name())) ||
                    authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(Roles.ROLE_USER.name()))) {
                Task completedTask = taskService.markTaskAsCompleted(taskId, authentication.getName());
                return new ResponseEntity<>(completedTask, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("You are not authorized to view this page", HttpStatus.UNAUTHORIZED);
            }
        } catch (TaskNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (WrongUserException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/delete-task/{taskId}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    private ResponseEntity<String> deleteTask(@PathVariable Long taskId, Authentication authentication) {
        try {
            if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(Roles.ROLE_ADMIN.name())) ||
                    authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(Roles.ROLE_USER.name()))) {
                taskService.deleteTask(taskId, authentication.getName());
                return new ResponseEntity<>("Task deleted successfully", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("You are not authorized to view this page", HttpStatus.UNAUTHORIZED);
            }
        }
        catch (TaskNotFoundException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (WrongUserException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }


}

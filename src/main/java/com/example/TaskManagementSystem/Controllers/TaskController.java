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
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add-task")
    private ResponseEntity addTask(@RequestBody AddTaskRequest addTaskRequest) {
            String result = taskService.addTask(addTaskRequest);
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);

    }

//    @GetMapping("/home")
//    private String home() {
//        return "Welcome to Task Management System";
//    }

    @GetMapping("/user-get-tasklist")
    private ResponseEntity getTaskList(Authentication authentication) {
        try {
            User user = userRepository.findByUserName(authentication.getName());
            if(authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(Roles.USER.name())))
            {
                List<Task> taskList = taskService.getTaskList(user.getId(), user.getUserName());
                return new ResponseEntity<>(taskList, HttpStatus.OK);
            }
            else if(authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(Roles.ADMIN.name()))){
                List<Task> taskList = taskService.getAllTasks();
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
    private ResponseEntity getallTasks(Authentication authentication) {
        if(authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(Roles.ADMIN.name()))){
            List<Task> taskList = taskService.getAllTasks();
            return new ResponseEntity<>(taskList, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("You are not authorized to view this page", HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/update-task/{taskId}")
    private ResponseEntity updateTask(@PathVariable Long taskId, @RequestBody UpdateTaskRequest updateTaskRequest, Authentication authentication) {
        try {
            if(authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(Roles.ADMIN.name())) ||
                    authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(Roles.USER.name()))){
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
    private ResponseEntity markTaskAsCompleted(@PathVariable Long taskId, Authentication authentication) {
        try {
            if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(Roles.ADMIN.name())) ||
                    authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(Roles.USER.name()))) {
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
    private ResponseEntity<String> deleteTask(@PathVariable Long taskId, Authentication authentication) {
        try {
            if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(Roles.ADMIN.name())) ||
                    authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(Roles.USER.name()))) {
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

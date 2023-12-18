package com.example.TaskManagementSystem.Services;

import com.example.TaskManagementSystem.Entity.Task;
import com.example.TaskManagementSystem.Entity.User;
import com.example.TaskManagementSystem.Enum.Roles;
import com.example.TaskManagementSystem.Enum.TaskStatus;
import com.example.TaskManagementSystem.Exceptions.TaskNotFoundException;
import com.example.TaskManagementSystem.Exceptions.UserNotFoundException;
import com.example.TaskManagementSystem.Exceptions.WrongUserException;
import com.example.TaskManagementSystem.Repositories.TaskRepository;
import com.example.TaskManagementSystem.Repositories.UserRepository;
import com.example.TaskManagementSystem.RequestDTOS.AddTaskRequest;
import com.example.TaskManagementSystem.RequestDTOS.UpdateTaskRequest;
import com.example.TaskManagementSystem.Transformers.AddTaskTransformer;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    
    public String addTask(AddTaskRequest addTaskRequest) {
        Task task = AddTaskTransformer.convertAddTaskRequestToEntity(addTaskRequest);
        task.setStatus(TaskStatus.NEW);

        User user = task.getUser();
        user.getTaskList().add(task);

        taskRepository.save(task);
        return "Task has been successfully created";
    }

    public List<Task> getTaskList(Long userId,String userName) throws UserNotFoundException, WrongUserException {
        User authenticatedUser = userRepository.findByUserName(userName);
        String Admin = Roles.ADMIN.name();
        Optional<User> optionalUser = userRepository.findById(userId);

        if(!optionalUser.isPresent()){
            throw new UserNotFoundException("No user found with userId: " + userId);
        }
        User user = optionalUser.get();
        if(authenticatedUser.getId().equals(user.getId()) || authenticatedUser.getRole().name().equals(Admin)) {
            List<Task> taskList = new ArrayList<>();
            taskList = user.getTaskList();
            return taskList;
        }else{
            throw new WrongUserException("The task you are trying to access is not yours");
        }
    }

    public List<Task> getAllTasks(){
        List<Task> allTasksList = new ArrayList<>();
        allTasksList = taskRepository.findAll();
        return allTasksList;
    }

    public Task updateTask(Long taskId, UpdateTaskRequest updateTaskRequest, String userName) throws TaskNotFoundException, WrongUserException{
        User user = userRepository.findByUserName(userName);
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if(!optionalTask.isPresent()){
            throw new TaskNotFoundException("No task present with taskId: " + taskId);
        }
        Task task = optionalTask.get();
        String Admin = Roles.ADMIN.name();
        if(user.getId().equals(task.getUser().getId()) || user.getRole().name().equals(Admin)) {
            task.setTitle(updateTaskRequest.getTitle());
            task.setDescription(updateTaskRequest.getDescription());
            task.setDueDate(updateTaskRequest.getDueDate());
            task.setStatus(updateTaskRequest.getStatus());

            taskRepository.save(task);
            return task;
        }else{
            throw new WrongUserException("The task you are trying to update is not yours");
        }
    }

    public Task markTaskAsCompleted(Long taskId, String userName) throws TaskNotFoundException, WrongUserException{
        User user = userRepository.findByUserName(userName);
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if(!optionalTask.isPresent()){
            throw new TaskNotFoundException("No task present with taskId: " + taskId);
        }
        Task task = optionalTask.get();
        String Admin = Roles.ADMIN.name();
        if(user.getId().equals(task.getUser().getId()) || user.getRole().name().equals(Admin)) {
            task.setStatus(TaskStatus.COMPLETED);
            return task;
        }else{
            throw new WrongUserException("The task you are trying to update is not yours");
        }
    }


    public void deleteTask(Long taskId, String userName) throws TaskNotFoundException, WrongUserException{
        User user = userRepository.findByUserName(userName);
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if(!optionalTask.isPresent()){
            throw new TaskNotFoundException("No task present with taskId: " + taskId);
        }
        Task task = optionalTask.get();
        String Admin = Roles.ADMIN.name();
        if(user.getId().equals(task.getUser().getId()) || user.getRole().name().equals(Admin)) {
            taskRepository.delete(task);
        }else{
            throw new WrongUserException("The task you are trying to delete is not yours");
        }
    }
}

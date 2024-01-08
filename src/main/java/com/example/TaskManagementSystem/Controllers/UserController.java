package com.example.TaskManagementSystem.Controllers;

import com.example.TaskManagementSystem.RequestDTOS.UserRegistrationDTO;
import com.example.TaskManagementSystem.Services.UserService;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("add")
    public ResponseEntity addUser(@RequestBody UserRegistrationDTO userRegistrationDTO){
        String result = userService.addUser(userRegistrationDTO);
        return new ResponseEntity(result, HttpStatus.CREATED);
    }

    @GetMapping("home")
    public String home(){
        return "Welcome to Task Management System. You are in the User page.";
    }
}

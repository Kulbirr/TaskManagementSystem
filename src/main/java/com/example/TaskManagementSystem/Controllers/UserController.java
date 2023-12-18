package com.example.TaskManagementSystem.Controllers;

import com.example.TaskManagementSystem.RequestDTOS.UserRegistrationDTO;
import com.example.TaskManagementSystem.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

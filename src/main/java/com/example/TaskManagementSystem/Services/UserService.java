package com.example.TaskManagementSystem.Services;

import com.example.TaskManagementSystem.Entity.User;
import com.example.TaskManagementSystem.Repositories.UserRepository;
import com.example.TaskManagementSystem.RequestDTOS.UserRegistrationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public String addUser(UserRegistrationDTO userRegistrationDTO) {
            User user = new User();
            user.setUserName(userRegistrationDTO.getUserName());
            user.setPassword(userRegistrationDTO.getPassword());
            user.setRole(userRegistrationDTO.getRole());

            userRepository.save(user);
            return "User has been created successfully";

    }
}

package com.example.TaskManagementSystem.RequestDTOS;

import com.example.TaskManagementSystem.Enum.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRegistrationDTO {

    private String userName;

    private String password;

    private Roles role;
}

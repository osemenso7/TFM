package com.finantialWeb.FinantialWeb.module_users.controller.model;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDto {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must contain between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 255, message = "Password must contain between 8 and 255 characters")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String mail;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must contain between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Surname is required")
    private String surname;

    @NotNull(message = "Birthday is required")
    @Past(message = "Birthday must be in the past") 
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")  
    private LocalDate birthday;
}
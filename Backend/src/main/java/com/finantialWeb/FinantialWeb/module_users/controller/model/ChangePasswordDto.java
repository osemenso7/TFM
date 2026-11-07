package com.finantialWeb.FinantialWeb.module_users.controller.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDto {

    @NotBlank(message = "Current password is required")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 8, max = 255, message = "New password must contain between 8 and 255 characters")
    private String newPassword;

    @NotBlank(message = "Confirmation password is required")
    @Size(min = 8, max = 255, message = "Confirmation password must contain between 8 and 255 characters")
    private String confirmationPassword;
}
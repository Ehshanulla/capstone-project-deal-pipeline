package com.example.InvestmentBankingDealPipelineManagementPortal.models;

import com.example.InvestmentBankingDealPipelineManagementPortal.user.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateUserRequestDTO {

    @NotBlank(message = "Username is required")
    private String username;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotNull(message = "Role is required")
    private Role role;
}

package com.example.InvestmentBankingDealPipelineManagementPortal.models;

import com.example.InvestmentBankingDealPipelineManagementPortal.user.Role;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class UserResponseDTO {

    private String id;
    private String username;
    private String email;
    private Role role;
    private boolean active;
    private Instant createdAt;
}


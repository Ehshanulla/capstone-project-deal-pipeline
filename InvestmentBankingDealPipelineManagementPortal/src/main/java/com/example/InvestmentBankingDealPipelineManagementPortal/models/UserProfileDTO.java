package com.example.InvestmentBankingDealPipelineManagementPortal.models;

import com.example.InvestmentBankingDealPipelineManagementPortal.user.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileDTO {

    private String id;
    private String username;
    private String email;
    private Role role;
}

package com.example.InvestmentBankingDealPipelineManagementPortal.user;

import com.example.InvestmentBankingDealPipelineManagementPortal.models.CreateUserRequestDTO;
import com.example.InvestmentBankingDealPipelineManagementPortal.models.PageResponse;
import com.example.InvestmentBankingDealPipelineManagementPortal.models.UserProfileDTO;
import com.example.InvestmentBankingDealPipelineManagementPortal.models.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO createUser(CreateUserRequestDTO request);

    UserResponseDTO updateUserStatus(String userId, boolean active);

    UserProfileDTO getCurrentUserProfile(String username);

    PageResponse<UserResponseDTO> getAllUsers(int page, int size);
}


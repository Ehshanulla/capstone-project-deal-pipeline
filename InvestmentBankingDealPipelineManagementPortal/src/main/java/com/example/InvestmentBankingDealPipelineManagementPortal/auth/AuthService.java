package com.example.InvestmentBankingDealPipelineManagementPortal.auth;

import com.example.InvestmentBankingDealPipelineManagementPortal.models.LoginRequestDTO;
import com.example.InvestmentBankingDealPipelineManagementPortal.models.LoginResponseDTO;

public interface AuthService {

    LoginResponseDTO login(LoginRequestDTO request);
}


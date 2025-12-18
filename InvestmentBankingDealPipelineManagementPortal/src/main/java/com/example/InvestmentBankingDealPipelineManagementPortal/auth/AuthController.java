package com.example.InvestmentBankingDealPipelineManagementPortal.auth;

import com.example.InvestmentBankingDealPipelineManagementPortal.models.LoginRequestDTO;
import com.example.InvestmentBankingDealPipelineManagementPortal.models.LoginResponseDTO;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<@NonNull LoginResponseDTO> login(
            @RequestBody @Valid LoginRequestDTO request) {

        LoginResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}


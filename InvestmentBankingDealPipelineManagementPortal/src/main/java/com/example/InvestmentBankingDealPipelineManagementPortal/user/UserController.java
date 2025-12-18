package com.example.InvestmentBankingDealPipelineManagementPortal.user;

import com.example.InvestmentBankingDealPipelineManagementPortal.models.UserProfileDTO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<@NonNull UserProfileDTO> getProfile(Authentication authentication) {

        String username = authentication.getName();
        UserProfileDTO profile = userService.getCurrentUserProfile(username);
        return ResponseEntity.ok(profile);
    }
}


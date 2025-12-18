package com.example.InvestmentBankingDealPipelineManagementPortal.auth;

import com.example.InvestmentBankingDealPipelineManagementPortal.handler.UserInactiveException;
import com.example.InvestmentBankingDealPipelineManagementPortal.user.User;
import com.example.InvestmentBankingDealPipelineManagementPortal.models.LoginRequestDTO;
import com.example.InvestmentBankingDealPipelineManagementPortal.models.LoginResponseDTO;
import com.example.InvestmentBankingDealPipelineManagementPortal.repository.UserRepository;
import com.example.InvestmentBankingDealPipelineManagementPortal.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new IllegalArgumentException("Invalid username or password");
        }


        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        boolean isActive = user.isActive();

        if(!isActive){
            throw new UserInactiveException("user account is inactive, please contact administration");
        }

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("name", user.getUsername());
        claims.put("role", user.getRole());

        // Generate token
        String token = jwtService.generateToken(claims, user.getUsername());

        return LoginResponseDTO.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

}


package com.example.InvestmentBankingDealPipelineManagementPortal.servicetests;

import com.example.InvestmentBankingDealPipelineManagementPortal.auth.AuthServiceImpl;
import com.example.InvestmentBankingDealPipelineManagementPortal.handler.UserInactiveException;
import com.example.InvestmentBankingDealPipelineManagementPortal.models.LoginRequestDTO;
import com.example.InvestmentBankingDealPipelineManagementPortal.models.LoginResponseDTO;
import com.example.InvestmentBankingDealPipelineManagementPortal.repository.UserRepository;
import com.example.InvestmentBankingDealPipelineManagementPortal.security.JwtService;
import com.example.InvestmentBankingDealPipelineManagementPortal.user.Role;
import com.example.InvestmentBankingDealPipelineManagementPortal.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void login_successful() {
        LoginRequestDTO request = new LoginRequestDTO("john", "password");

        User user = User.builder()
                .username("john")
                .role(Role.USER)
                .active(true)
                .build();

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        when(jwtService.generateToken(anyMap(), eq("john")))
                .thenReturn("jwt-token");

        LoginResponseDTO response = authService.login(request);

        assertEquals("john", response.getUsername());
        assertEquals(Role.USER, response.getRole());
        assertEquals("jwt-token", response.getToken());
    }

    @Test
    void login_invalidCredentials() {
        LoginRequestDTO request = new LoginRequestDTO("john", "wrong");

        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager)
                .authenticate(any());

        assertThrows(IllegalArgumentException.class,
                () -> authService.login(request));
    }

    @Test
    void login_userNotFound() {
        LoginRequestDTO request = new LoginRequestDTO("john", "password");

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> authService.login(request));
    }

    @Test
    void login_userInactive() {
        LoginRequestDTO request = new LoginRequestDTO("john", "password");

        User user = User.builder()
                .username("john")
                .active(false)
                .build();

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        assertThrows(UserInactiveException.class,
                () -> authService.login(request));
    }
}


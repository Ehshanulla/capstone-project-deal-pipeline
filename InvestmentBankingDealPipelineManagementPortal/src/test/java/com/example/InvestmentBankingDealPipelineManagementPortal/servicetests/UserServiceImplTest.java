package com.example.InvestmentBankingDealPipelineManagementPortal.servicetests;

import com.example.InvestmentBankingDealPipelineManagementPortal.models.CreateUserRequestDTO;
import com.example.InvestmentBankingDealPipelineManagementPortal.models.UserProfileDTO;
import com.example.InvestmentBankingDealPipelineManagementPortal.models.UserResponseDTO;
import com.example.InvestmentBankingDealPipelineManagementPortal.repository.UserRepository;
import com.example.InvestmentBankingDealPipelineManagementPortal.user.Role;
import com.example.InvestmentBankingDealPipelineManagementPortal.user.User;
import com.example.InvestmentBankingDealPipelineManagementPortal.user.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_success() {
        CreateUserRequestDTO request = new CreateUserRequestDTO(
                "john", "john@mail.com", "pass", Role.USER
        );

        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(userRepository.existsByEmail("john@mail.com")).thenReturn(false);
        when(passwordEncoder.encode("pass")).thenReturn("encoded");

        User saved = User.builder()
                .username("john")
                .email("john@mail.com")
                .role(Role.USER)
                .build();

        when(userRepository.save(any())).thenReturn(saved);

        UserResponseDTO response = userService.createUser(request);

        assertEquals("john", response.getUsername());
    }

    @Test
    void createUser_duplicateUsername() {
        when(userRepository.existsByUsername("john")).thenReturn(true);

        assertThrows(RuntimeException.class,
                () -> userService.createUser(
                        new CreateUserRequestDTO("john", "x@y.com", "p", Role.USER)));
    }

    @Test
    void getCurrentUserProfile_success() {
        User user = User.builder()
                .username("john")
                .email("john@mail.com")
                .role(Role.USER)
                .build();

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        UserProfileDTO profile =
                userService.getCurrentUserProfile("john");

        assertEquals("john", profile.getUsername());
    }
}


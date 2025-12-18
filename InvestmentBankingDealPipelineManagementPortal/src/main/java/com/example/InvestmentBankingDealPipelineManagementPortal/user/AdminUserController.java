package com.example.InvestmentBankingDealPipelineManagementPortal.user;

import com.example.InvestmentBankingDealPipelineManagementPortal.models.CreateUserRequestDTO;
import com.example.InvestmentBankingDealPipelineManagementPortal.models.PageResponse;
import com.example.InvestmentBankingDealPipelineManagementPortal.models.UpdateUserStatusRequestDTO;
import com.example.InvestmentBankingDealPipelineManagementPortal.models.UserResponseDTO;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<@NonNull UserResponseDTO> createUser(
            @RequestBody @Valid CreateUserRequestDTO request) {

        UserResponseDTO user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<UserResponseDTO>> listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                userService.getAllUsers(page, size)
        );
    }


    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> updateStatus(
            @PathVariable String id,
            @RequestBody UpdateUserStatusRequestDTO request) {

        UserResponseDTO user = userService.updateUserStatus(id, request.isActive());
        return ResponseEntity.ok(user);
    }
}



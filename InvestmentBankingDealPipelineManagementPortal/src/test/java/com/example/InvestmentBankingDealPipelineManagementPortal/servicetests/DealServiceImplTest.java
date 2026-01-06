package com.example.InvestmentBankingDealPipelineManagementPortal.servicetests;

import com.example.InvestmentBankingDealPipelineManagementPortal.deal.Deal;
import com.example.InvestmentBankingDealPipelineManagementPortal.deal.DealServiceImpl;
import com.example.InvestmentBankingDealPipelineManagementPortal.deal.DealStage;
import com.example.InvestmentBankingDealPipelineManagementPortal.handler.UnauthorizedActionException;
import com.example.InvestmentBankingDealPipelineManagementPortal.models.CreateDealRequestDTO;
import com.example.InvestmentBankingDealPipelineManagementPortal.models.DealResponseDTO;
import com.example.InvestmentBankingDealPipelineManagementPortal.models.UpdateDealStageDTO;
import com.example.InvestmentBankingDealPipelineManagementPortal.repository.DealRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealServiceImplTest {

    @Mock
    private DealRepository dealRepository;

    @InjectMocks
    private DealServiceImpl dealService;

    @BeforeEach
    void setup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createDeal_user_autoAssigned() {
        mockUser("user1", "ROLE_USER");

        CreateDealRequestDTO dto = new CreateDealRequestDTO();
        dto.setClientName("Client");
        dto.setSummary("Summary");

        Deal savedDeal = Deal.builder()
                .assignedTo("user1")
                .build();

        when(dealRepository.save(any()))
                .thenReturn(savedDeal);

        DealResponseDTO response = dealService.createDeal(dto);

        assertEquals("user1", response.getAssignedTo());
    }

    @Test
    void updateStage_ownerAllowed() {
        mockUser("user1", "ROLE_USER");

        Deal deal = Deal.builder()
                .id("1")
                .assignedTo("user1")
                .currentStage(DealStage.PROSPECT)
                .build();

        when(dealRepository.findById("1"))
                .thenReturn(Optional.of(deal));

        when(dealRepository.save(any()))
                .thenReturn(deal);

        Deal updated = dealService.updateStage(
                "1",
                new UpdateDealStageDTO(DealStage.CLOSED)
        );

        assertEquals(DealStage.CLOSED, updated.getCurrentStage());
    }

    @Test
    void updateStage_notOwner_shouldFail() {
        mockUser("user2", "ROLE_USER");

        Deal deal = Deal.builder()
                .assignedTo("user1")
                .build();

        when(dealRepository.findById("1"))
                .thenReturn(Optional.of(deal));

        assertThrows(UnauthorizedActionException.class,
                () -> dealService.updateStage("1",
                        new UpdateDealStageDTO(DealStage.CLOSED)));
    }

    @Test
    void updateDealValue_nonAdmin_shouldFail() {
        mockUserWithRole("ROLE_USER");

        assertThrows(UnauthorizedActionException.class,
                () -> dealService.updateDealValue("1", 1000L));
    }

    @Test
    void deleteDeal_adminAllowed() {
        mockUser("admin", "ROLE_ADMIN");

        Deal deal = Deal.builder()
                .assignedTo("user1")
                .build();

        when(dealRepository.findById("1"))
                .thenReturn(Optional.of(deal));

        dealService.deleteDeal("1");

        verify(dealRepository).deleteById("1");
    }

    private void mockUser(String username, String role) {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(username);

        doReturn(List.of(new SimpleGrantedAuthority(role)))
                .when(auth).getAuthorities();

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);

        SecurityContextHolder.setContext(context);
    }

    private void mockUserWithRole(String role) {
        Authentication auth = mock(Authentication.class);

        doReturn(List.of(new SimpleGrantedAuthority(role)))
                .when(auth).getAuthorities();

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);

        SecurityContextHolder.setContext(context);
    }


}


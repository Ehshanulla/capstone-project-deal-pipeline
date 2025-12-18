package com.example.InvestmentBankingDealPipelineManagementPortal.deal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DealNote {

    // Manually set from SecurityContext
    private String createdBy;

    @NotBlank
    @Size(max = 1000)
    private String note;

    // Manually set at creation time
    private Instant createdAt;
}


package com.example.InvestmentBankingDealPipelineManagementPortal.models;

import com.example.InvestmentBankingDealPipelineManagementPortal.deal.DealStage;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDealStageDTO {

    @NotNull(message = "Deal stage is required")
    private DealStage stage;
}

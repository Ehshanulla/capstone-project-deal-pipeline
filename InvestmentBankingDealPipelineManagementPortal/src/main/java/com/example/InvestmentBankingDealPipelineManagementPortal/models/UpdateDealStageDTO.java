package com.example.InvestmentBankingDealPipelineManagementPortal.models;

import com.example.InvestmentBankingDealPipelineManagementPortal.deal.DealStage;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateDealStageDTO {

    @NotNull(message = "Deal stage is required")
    private DealStage stage;
}

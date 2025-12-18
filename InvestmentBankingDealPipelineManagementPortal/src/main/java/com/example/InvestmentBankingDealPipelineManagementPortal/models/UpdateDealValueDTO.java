package com.example.InvestmentBankingDealPipelineManagementPortal.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateDealValueDTO {

    @NotNull(message = "Deal value is required")
    @Min(value = 1, message = "Deal value must be greater than zero")
    private Long dealValue;
}

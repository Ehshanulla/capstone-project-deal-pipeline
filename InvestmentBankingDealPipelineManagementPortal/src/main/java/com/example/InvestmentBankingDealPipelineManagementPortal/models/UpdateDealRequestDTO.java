package com.example.InvestmentBankingDealPipelineManagementPortal.models;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateDealRequestDTO {

    @Size(max = 50, message = "Deal type cannot exceed 50 characters")
    private String dealType;

    @Size(max = 50, message = "Sector cannot exceed 50 characters")
    private String sector;

    @Size(max = 1000, message = "Summary cannot exceed 1000 characters")
    private String summary;
}

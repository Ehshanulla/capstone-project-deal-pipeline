package com.example.InvestmentBankingDealPipelineManagementPortal.models;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateDealRequestDTO {

    @NotBlank(message = "Client name is required")
    @Size(max = 100, message = "Client name cannot exceed 100 characters")
    private String clientName;

    @NotBlank(message = "Deal type is required")
    private String dealType;

    @NotBlank(message = "Sector is required")
    private String sector;

    @Size(max = 1000, message = "Summary cannot exceed 1000 characters")
    private String summary;

    private String assignedTo;
}


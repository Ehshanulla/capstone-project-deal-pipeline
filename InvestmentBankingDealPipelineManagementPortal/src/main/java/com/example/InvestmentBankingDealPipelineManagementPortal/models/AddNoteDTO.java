package com.example.InvestmentBankingDealPipelineManagementPortal.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddNoteDTO {

    @NotBlank(message = "Note cannot be empty")
    @Size(max = 500, message = "Note cannot exceed 500 characters")
    private String note;
}



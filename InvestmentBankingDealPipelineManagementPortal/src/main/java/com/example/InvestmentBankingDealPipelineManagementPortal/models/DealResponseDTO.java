package com.example.InvestmentBankingDealPipelineManagementPortal.models;

import com.example.InvestmentBankingDealPipelineManagementPortal.deal.DealNote;
import com.example.InvestmentBankingDealPipelineManagementPortal.deal.DealStage;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class DealResponseDTO {
    private String id;
    private String clientName;
    private String dealType;
    private String sector;
    private Long dealValue;
    private DealStage stage;
    private String summary;
    private List<DealNote> notes;
    private String createdBy;
    private String assignedTo;
}


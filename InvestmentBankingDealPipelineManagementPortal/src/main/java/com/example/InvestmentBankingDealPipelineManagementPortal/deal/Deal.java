package com.example.InvestmentBankingDealPipelineManagementPortal.deal;


import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "deals")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Deal {

    @Id
    private String id;

    private String clientName;
    private String dealType;
    private String sector;

    // 🔐 Sensitive
    private Long dealValue;

    private DealStage currentStage;
    private String summary;

    @Builder.Default
    private List<DealNote> notes = new ArrayList<>();

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

    private String assignedTo;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}

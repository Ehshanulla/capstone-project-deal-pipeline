package com.example.InvestmentBankingDealPipelineManagementPortal.models;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ApiErrorResponse {

    private Instant timestamp;
    private int status;
    private String error;    // HTTP STATUS NAME
    private String code;     // APPLICATION ERROR CODE
    private String message;  // USER FRIENDLY MESSAGE
    private String path;     // API PATH
    private Object details;  // FIELD ERRORS / EXTRA DATA
}


package com.example.InvestmentBankingDealPipelineManagementPortal.handler;


public class UnauthorizedActionException extends RuntimeException {
    public UnauthorizedActionException(String message) {
        super(message);
    }
}


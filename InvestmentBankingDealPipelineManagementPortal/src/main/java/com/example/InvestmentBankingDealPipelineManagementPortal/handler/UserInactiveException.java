package com.example.InvestmentBankingDealPipelineManagementPortal.handler;


public class UserInactiveException extends RuntimeException {
    public UserInactiveException(String msg) {
        super(msg);
    }
}

package com.example.InvestmentBankingDealPipelineManagementPortal.repository;

import com.example.InvestmentBankingDealPipelineManagementPortal.deal.Deal;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DealRepository extends MongoRepository<Deal, String> {
}


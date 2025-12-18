package com.example.InvestmentBankingDealPipelineManagementPortal.deal;

import com.example.InvestmentBankingDealPipelineManagementPortal.models.*;

import java.util.List;

public interface DealService {

    DealResponseDTO createDeal(CreateDealRequestDTO dto);

    List<DealResponseDTO> getAllDeals();

    PageResponse<DealResponseDTO> getAllDeals(
            int page, int size, String sortBy, String direction);

    DealResponseDTO getDealById(String id);

    DealResponseDTO updateDeal(String id, UpdateDealRequestDTO dto);
    Deal updateStage(String id, UpdateDealStageDTO stage);

    DealResponseDTO updateDealValue(String id, Long value);
    DealResponseDTO addNote(String id, String note);

    void deleteDeal(String id);
}


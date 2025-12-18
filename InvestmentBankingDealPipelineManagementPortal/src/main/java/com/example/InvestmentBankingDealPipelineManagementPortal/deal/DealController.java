package com.example.InvestmentBankingDealPipelineManagementPortal.deal;


import com.example.InvestmentBankingDealPipelineManagementPortal.models.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/deals")
@RequiredArgsConstructor
public class DealController {

    private final DealService dealService;

    // CREATE DEAL
    @PostMapping
    public ResponseEntity<DealResponseDTO> create(
            @Valid @RequestBody CreateDealRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dealService.createDeal(dto));
    }

    // LIST DEALS WITH PAGINATION + SORTING
    @GetMapping
    public ResponseEntity<PageResponse<DealResponseDTO>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        return ResponseEntity.ok(
                dealService.getAllDeals(page, size, sortBy, direction)
        );
    }

    // GET DEAL BY ID
    @GetMapping("/{id}")
    public DealResponseDTO getById(@PathVariable String id) {
        return dealService.getDealById(id);
    }

    // UPDATE BASIC FIELDS
    @PutMapping("/{id}")
    public DealResponseDTO update(
            @PathVariable String id,
            @Valid @RequestBody UpdateDealRequestDTO dto) {

        return dealService.updateDeal(id, dto);
    }

    // UPDATE STAGE
    @PatchMapping("/{id}/stage")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Map<String,Object>> updateStage(
            @PathVariable String id,
            @Valid @RequestBody UpdateDealStageDTO stageDTO
    ) {
        DealResponseDTO updated = map(dealService.updateStage(id, stageDTO));

        return ResponseEntity.ok(
                Map.of(
                        "message", "Stage updated for the deal successfully",
                        "deal", updated
                )
        );
    }


    // UPDATE DEAL VALUE (ADMIN)
    @PatchMapping("/{id}/value")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> updateValue(
            @PathVariable String id,
            @Valid @RequestBody UpdateDealValueDTO dto) {

        DealResponseDTO updated = dealService.updateDealValue(id, dto.getDealValue());

        return ResponseEntity.ok(
                Map.of(
                        "message", "Deal value updated successfully",
                        "deal", updated
                )
        );
    }

    // ADD NOTE
    @PostMapping("/{id}/notes")
    public DealResponseDTO addNote(
            @PathVariable String id,
            @Valid @RequestBody AddNoteDTO dto) {

        return dealService.addNote(id, dto.getNote());
    }

    // DELETE DEAL (ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        dealService.deleteDeal(id);
    }

    private DealResponseDTO map(Deal deal) {
        return DealResponseDTO.builder()
                .id(deal.getId())
                .clientName(deal.getClientName())
                .dealType(deal.getDealType())
                .sector(deal.getSector())
                .summary(deal.getSummary())
                .stage(deal.getCurrentStage())
                .createdBy(deal.getCreatedBy())
                .assignedTo(deal.getAssignedTo())
                .dealValue(deal.getDealValue())
                .notes(deal.getNotes())
                .build();
    }
}
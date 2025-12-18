package com.example.InvestmentBankingDealPipelineManagementPortal.deal;

import com.example.InvestmentBankingDealPipelineManagementPortal.handler.ResourceNotFoundException;
import com.example.InvestmentBankingDealPipelineManagementPortal.handler.UnauthorizedActionException;
import com.example.InvestmentBankingDealPipelineManagementPortal.models.*;
import com.example.InvestmentBankingDealPipelineManagementPortal.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class DealServiceImpl implements DealService {


    private final DealRepository dealRepo;



    @Override
    public DealResponseDTO createDeal(CreateDealRequestDTO dto) {
        String currentUserId = getCurrentUserId();
        boolean admin = isAdmin();

        String assignedUser;


        if (admin && dto.getAssignedTo() != null && !dto.getAssignedTo().isBlank()) {
            assignedUser = dto.getAssignedTo(); // assign to chosen user
        } else {
            assignedUser = currentUserId; // auto-assign to self
        }

        Deal deal = Deal.builder()
                .clientName(dto.getClientName())
                .dealType(dto.getDealType())
                .sector(dto.getSector())
                .summary(dto.getSummary())
                .currentStage(DealStage.PROSPECT)
                .assignedTo(assignedUser)
                .build();


        return map(dealRepo.save(deal));
    }

    @Override
    public PageResponse<DealResponseDTO> getAllDeals(
            int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Deal> dealPage = dealRepo.findAll(pageable);

        return PageResponse.<DealResponseDTO>builder()
                .content(dealPage.getContent()
                        .stream()
                        .map(this::map)
                        .toList())
                .page(dealPage.getNumber())
                .size(dealPage.getSize())
                .totalElements(dealPage.getTotalElements())
                .totalPages(dealPage.getTotalPages())
                .last(dealPage.isLast())
                .build();
    }



    @Override
    public List<DealResponseDTO> getAllDeals() {
        return dealRepo.findAll().stream().map(this::map).toList();
    }


    @Override
    public DealResponseDTO getDealById(String id) {
        return map(getDeal(id));
    }


    @Override
    public DealResponseDTO updateDeal(String id, UpdateDealRequestDTO dto) {
        Deal deal = getDeal(id);


        deal.setSummary(dto.getSummary());
        deal.setSector(dto.getSector());
        deal.setDealType(dto.getDealType());


        return map(dealRepo.save(deal));
    }


    @Override
    public Deal updateStage(String id, UpdateDealStageDTO newStage) {

        Deal deal = getDeal(id);

        // 🔐 Ownership rule
        validateOwnershipOrAdmin(deal);

        deal.setCurrentStage(newStage.getStage());

        return dealRepo.save(deal);
    }




    @Override
    public DealResponseDTO updateDealValue(String id, Long value) {

        if (!isAdmin()) {
            throw new UnauthorizedActionException(
                    "Only admins can update deal value"
            );
        }

        Deal deal = getDeal(id);
        deal.setDealValue(value);
        return map(dealRepo.save(deal));
    }



    @Override
    public DealResponseDTO addNote(String id, String note) {
        Deal deal = getDeal(id);


        DealNote dealNote = DealNote.builder()
                .note(note)
                .createdBy(getCurrentUserId())
                .createdAt(Instant.now())
                .build();


        deal.getNotes().add(dealNote);
        return map(dealRepo.save(deal));
    }


    @Override
    public void deleteDeal(String id) {

        Deal deal = getDeal(id);

        if (!deal.getAssignedTo().equals(getCurrentUserId()) && !isAdmin()) {
            throw new UnauthorizedActionException(
                    "You are not allowed to delete this deal"
            );
        }

        dealRepo.deleteById(id);
    }

    private boolean isAdmin() {
        return org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    private void validateOwnershipOrAdmin(Deal deal) {
        if (isAdmin()) return;

        if (!deal.getAssignedTo().equals(getCurrentUserId())) {
            throw new UnauthorizedActionException(
                    "You are not allowed to modify this deal"
            );
        }
    }



    private Deal getDeal(String id) {
        return dealRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deal not found"));
    }


    private String getCurrentUserId() {
        return org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
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
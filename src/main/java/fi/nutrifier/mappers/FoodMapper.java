package fi.nutrifier.mappers;

import fi.nutrifier.dto.*;
import fi.nutrifier.entities.Food;
import fi.nutrifier.entities.FoodReport;
import fi.nutrifier.enums.FoodStatus;
import fi.nutrifier.enums.ReportStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class FoodMapper implements EntityMapper<Food, FoodResponse, FoodRequest> {

    @Override
    public FoodResponse toResponse(Food food) {
        if (food == null) return null;
        return new FoodResponse(
                food.getId(),
                food.getName(),
                food.getBrand(),
                food.getCategory(),
                food.getBarcode(),
                food.getServingSize(),
                food.getCalories(),
                food.getCarbs(),
                food.getProtein(),
                food.getFat(),
                food.getVerified(),
                food.getStatus()
        );
    }

    @Override
    public Food toEntity(UUID userId, FoodRequest request) {
        LocalDateTime now = LocalDateTime.now();
        return new Food(
                UUID.randomUUID(),
                request.getName(),
                request.getBrand(),
                request.getCategory(),
                request.getBarcode(),
                request.getServingSize(),
                request.getCalories(),
                request.getCarbs(),
                request.getProtein(),
                request.getFat(),
                false,  // Verified
                FoodStatus.ACTIVE,
                userId, // Created by
                userId, // Updated by
                now,    // Created at
                now     // Updated at
        );
    }

    public void updateEntityFromRequest(FoodRequest request, Food entity) {
        entity.setName(request.getName());
        entity.setBarcode(request.getBarcode());
        entity.setServingSize(request.getServingSize());
        entity.setCalories(request.getCalories());
        entity.setCarbs(request.getCarbs());
        entity.setProtein(request.getProtein());
        entity.setFat(request.getFat());
        entity.setUpdatedAt(LocalDateTime.now());
    }

    public FoodReport reportCreateRequestToEntity(UUID foodId, UUID userId, FoodReportCreateRequest request) {
        LocalDateTime now = LocalDateTime.now();
        return new FoodReport(
                UUID.randomUUID(),
                foodId,
                userId,
                request.getType(),
                request.getReason(),
                ReportStatus.PENDING,
                request.getDescription(),
                request.getProposedName(),
                request.getProposedCalories(),
                request.getProposedFat(),
                request.getProposedCarbs(),
                request.getProposedProtein(),
                null,
                null,
                null,
                now
        );
    }

    public void reportUpdateRequestToEntity(UUID userId, FoodReportReviewRequest request, FoodReport entity) {
        LocalDateTime now = LocalDateTime.now();
        entity.setDecisionReasoning(request.getDecisionReasoning());
        entity.setReviewedBy(userId);
        entity.setReviewedAt(now);
    }

    public FoodReportResponse reportEntityToResponse(FoodReport report) {
        return new FoodReportResponse(
            report.getId(),
            report.getFoodId(),
            report.getUserId(),
            report.getType(),
            report.getReason(),
            report.getStatus(),
            report.getDescription(),
            report.getProposedName(),
            report.getProposedCalories(),
            report.getProposedFat(),
            report.getProposedCarbs(),
            report.getProposedProtein(),
            report.getDecisionReasoning(),
            report.getReviewedBy(),
            report.getReviewedAt()
        );
    }
}

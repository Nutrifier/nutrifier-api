package fi.nutrifier.mappers;

import fi.nutrifier.dto.FoodEntryRequest;
import fi.nutrifier.dto.FoodEntryResponse;
import fi.nutrifier.entities.FoodEntry;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class FoodEntryMapper implements EntityMapper<FoodEntry, FoodEntryResponse, FoodEntryRequest> {

    @Override
    public FoodEntry toEntity(UUID userId, FoodEntryRequest request) {
        LocalDateTime now = LocalDateTime.now();
        return new FoodEntry(
                UUID.randomUUID(),
                request.getAmount(),
                request.getDate(),
                request.getTime(),
                request.getMealType(),
                request.getUnit(),
                request.getCaloriesSnapshot(),
                request.getFatSnapshot(),
                request.getCarbsSnapshot(),
                request.getProteinSnapshot(),
                request.getFineliId(),
                userId,
                request.getFoodId()
        );
    }

    @Override
    public FoodEntryResponse toResponse(FoodEntry foodEntry) {
        if (foodEntry == null) return null;
        return new FoodEntryResponse(
                foodEntry.getId(),
                foodEntry.getDate(),
                foodEntry.getTime(),
                foodEntry.getMealType(),
                foodEntry.getUserId(),
                foodEntry.getFoodId(),
                foodEntry.getFineliId(),
                foodEntry.getAmount()
        );
    }

    public void updateEntityFromRequest(FoodEntryRequest request, FoodEntry entity) {
        entity.setDate(request.getDate());
        entity.setTime(request.getTime());
        entity.setMealType(request.getMealType());
        //entity.setUserId(request.getUserId());
        entity.setFoodId(request.getFoodId());
        entity.setFineliId(request.getFineliId());
        entity.setAmount(request.getAmount());
    }
}

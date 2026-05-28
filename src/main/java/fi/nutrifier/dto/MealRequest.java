package fi.nutrifier.dto;

import fi.nutrifier.entities.Meal;
import fi.nutrifier.entities.MealEntry;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class MealRequest {
    private String name;
    private Boolean isPublic;
    private Boolean isForked;
    private List<MealEntry> entries;

    public Meal toEntity(UUID userId) {
        LocalDateTime now = LocalDateTime.now();

        return new Meal(
            UUID.randomUUID(),
            userId,
            this.getName(),
            this.getIsPublic(),
            this.getIsForked(),
            now,
            now,
            this.getEntries()
        );
    }
}
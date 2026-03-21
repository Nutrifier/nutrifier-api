package fi.nutrifier.dto;

import fi.nutrifier.entities.MealEntry;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class MealResponse {
    private UUID id;
    private UUID userId;
    private String name;
    private Boolean isPublic;
    private Boolean isForked;
    private List<MealEntry> entries;
}
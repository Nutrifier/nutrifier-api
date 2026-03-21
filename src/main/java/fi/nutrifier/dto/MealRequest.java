package fi.nutrifier.dto;

import fi.nutrifier.entities.MealEntry;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MealRequest {
    private String name;
    private Boolean isPublic;
    private Boolean isForked;
    private List<MealEntry> entries;
}
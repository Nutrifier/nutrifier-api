package fi.nutrifier.dto;

import fi.nutrifier.enums.ActivityLevel;
import fi.nutrifier.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileResponse {
    private Integer height;
    private Integer age;
    private Sex sex;
    private ActivityLevel activityLevel;
}

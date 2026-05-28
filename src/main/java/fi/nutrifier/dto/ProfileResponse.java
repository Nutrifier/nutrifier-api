package fi.nutrifier.dto;

import fi.nutrifier.enums.ActivityLevel;
import fi.nutrifier.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ProfileResponse extends ApiResponse {
    private Integer height;
    private Integer age;
    private Sex sex;
    private ActivityLevel activityLevel;
}

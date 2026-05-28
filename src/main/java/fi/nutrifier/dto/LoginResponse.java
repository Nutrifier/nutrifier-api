package fi.nutrifier.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class LoginResponse extends ApiResponse {
    private String token;
    private UUID userId;
}

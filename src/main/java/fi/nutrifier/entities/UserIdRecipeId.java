package fi.nutrifier.entities;

import jakarta.persistence.Column;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class UserIdRecipeId implements Serializable {

    @Column(columnDefinition = "CHAR(36)")
    private UUID userId;

    @Column(columnDefinition = "CHAR(36)")
    private UUID recipeId;
}

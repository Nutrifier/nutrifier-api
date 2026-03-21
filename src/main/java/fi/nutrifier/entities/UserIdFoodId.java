package fi.nutrifier.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class UserIdFoodId implements Serializable {

    @Column(columnDefinition = "CHAR(36)")
    private UUID userId;

    @Column(columnDefinition = "CHAR(36)")
    private UUID foodId;
}

package fi.nutrifier.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Data
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    // Reference constants for ease of use (ok to be hardcoded, because these shouldn't change)
    public static final Role REGULAR =
            new Role(UUID.fromString("00010003-0000-0000-0000-000000000000"), "REGULAR");
    public static final Role ADMIN =
            new Role(UUID.fromString("00010001-0000-0000-0000-000000000000"), "ADMIN");
    public static final Role PREMIUM =
            new Role(UUID.fromString("00010002-0000-0000-0000-000000000000"), "PREMIUM");

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;
}

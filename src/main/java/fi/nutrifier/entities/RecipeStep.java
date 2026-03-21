package fi.nutrifier.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "recipe_steps")
@NoArgsConstructor
@AllArgsConstructor
public class RecipeStep {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "recipe_id", columnDefinition = "CHAR(36)")
    private Recipe recipe;

    @Column(nullable = false)
    private Integer position;

    @Column(nullable = false)
    private String instruction;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}

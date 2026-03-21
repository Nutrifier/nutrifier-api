package fi.nutrifier.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "weight_entries")
@NoArgsConstructor
@AllArgsConstructor
public class WeightEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition = "CHAR(36)", nullable = false)
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private LocalDateTime date;
}
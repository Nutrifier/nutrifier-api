package com.example.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.Data;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Data
@Table(name = "foods")
@NoArgsConstructor
@AllArgsConstructor
public class Food {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    @Column(nullable = false)
    @NotBlank
    private String name;

    private String barcode;

    @Column(nullable = false)
    @Min(value = 1)
    private Integer servingSize = 100;

    @Column(nullable = false)
    @NotNull
    @Min(value = 0)
    private Double calories;

    @Min(value = 0)
    private Double carbs;

    @Min(value = 0)
    private Double protein;

    @Min(value = 0)
    private Double fat;

    @Column(nullable = false)
    @NotNull
    private UUID createdBy;

    @Column(nullable = false)
    @NotNull
    private UUID editedBy;

    @CreationTimestamp
    @Column(nullable = false)
    private String created;

    @UpdateTimestamp
    @Column(nullable = false)
    private String edited;
}

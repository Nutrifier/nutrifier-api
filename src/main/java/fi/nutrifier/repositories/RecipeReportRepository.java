package fi.nutrifier.repositories;

import fi.nutrifier.entities.RecipeReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecipeReportRepository extends JpaRepository<RecipeReport, UUID> {

}
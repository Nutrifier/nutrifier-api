package fi.nutrifier.repositories;

import fi.nutrifier.entities.FoodReport;
import fi.nutrifier.entities.RecipeFavourite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FoodReportRepository extends JpaRepository<FoodReport, UUID> {

}
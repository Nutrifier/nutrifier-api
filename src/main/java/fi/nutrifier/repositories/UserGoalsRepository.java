package fi.nutrifier.repositories;

import fi.nutrifier.entities.UserGoals;
import fi.nutrifier.entities.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserGoalsRepository extends JpaRepository<UserGoals, String> {
    Optional<UserGoals> findByUserId(String userId);
}
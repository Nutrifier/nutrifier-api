package fi.nutrifier.repositories;

import fi.nutrifier.entities.UserFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserFeedbackRepository extends JpaRepository<UserFeedback, UUID> {

}
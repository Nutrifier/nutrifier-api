package fi.nutrifier.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import fi.nutrifier.entities.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}
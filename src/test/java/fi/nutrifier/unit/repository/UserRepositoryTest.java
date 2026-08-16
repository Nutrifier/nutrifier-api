package fi.nutrifier.unit.repository;

import fi.nutrifier.entities.Role;
import fi.nutrifier.entities.User;
import fi.nutrifier.repositories.UserRepository;
import fi.nutrifier.repositories.reference.RoleRepository;
import fi.nutrifier.unit.utils.TestObjects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import java.util.Optional;

import static fi.nutrifier.unit.utils.TestObjects.ROLE_REGULAR;
import static fi.nutrifier.unit.utils.TestObjects.toUser;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void setup() {
        TestObjects.reset();
        repository.deleteAll();

        Role savedRole = roleRepository.save(TestObjects.ROLE_REGULAR);
        TestObjects.userResponse1.setRole(savedRole.getName());
        TestObjects.userResponse2.setRole(savedRole.getName());
    }

    @Test
    public void testSaveUser_ReturnsSavedUser() {
        User user = toUser(TestObjects.userResponse1, ROLE_REGULAR);
        user.setPassword("qwerty");
        User saved = repository.save(user);

        assertNotNull(saved.getId());
        assertEquals("test@gmail.com", saved.getEmail());
        assertEquals("qwerty", saved.getPassword());
    }

    @Test
    public void testFindById_ReturnsUser() {
        User user = toUser(TestObjects.userResponse1, ROLE_REGULAR);
        user.setPassword("qwerty");
        User saved = repository.save(user);

        User found = repository.findById(saved.getId()).get();

        assertNotNull(found);
        assertEquals("test@gmail.com", found.getEmail());
        assertEquals("qwerty", found.getPassword());
    }

    @Test
    public void testFindAll_ReturnsMultipleUsers() {
        Optional<Role> savedRole = roleRepository.findByNameIgnoreCase("REGULAR");
        User user1 = toUser(TestObjects.userResponse1, savedRole.get());
        user1.setPassword("qwerty");
        repository.save(user1);

        User user2 = toUser(TestObjects.userResponse2, savedRole.get());
        user2.setPassword("qwerty");
        repository.save(user2);

        List<User> found = repository.findAll();

        assertEquals(2, found.size());
        assertEquals("test@gmail.com", found.get(0).getEmail());
        assertEquals("test2@gmail.com", found.get(1).getEmail());
    }

    @Test
    public void testUpdateUser_ReturnsFood() {
        User saved = repository.save(toUser(TestObjects.userResponse1, ROLE_REGULAR));

        saved.setEmail("changed@gmail.com");
        saved.setPassword("1234");
        User updated = repository.save(saved);

        assertEquals(saved.getId(), updated.getId());
        assertEquals("changed@gmail.com", updated.getEmail());
        assertEquals("1234", updated.getPassword());
    }

    @Test
    public void testDeleteUser_ReturnsEmptyList() {
        Optional<Role> savedRole = roleRepository.findByNameIgnoreCase("REGULAR");
        User user = toUser(TestObjects.userResponse1, savedRole.get());
        user.setPassword("qwerty");
        User saved = repository.save(user);

        repository.delete(saved);
        Optional<User> found = repository.findById(saved.getId());

        assertFalse(found.isPresent());
        assertEquals(0, repository.findAll().size());
    }

    @Test
    public void testFindByEmail_ReturnsUser() {
        Optional<Role> savedRole = roleRepository.findByNameIgnoreCase("REGULAR");
        User user = toUser(TestObjects.userResponse1, savedRole.get());
        user.setPassword("password");
        User saved = repository.save(user);

        User found = repository.findByEmail(saved.getEmail()).get();

        assertNotNull(found);
        assertEquals("test@gmail.com", found.getEmail());
        assertEquals("password", found.getPassword());
    }
}

package fi.nutrifier.unit.repository;

import fi.nutrifier.entities.User;
import fi.nutrifier.repositories.UserRepository;
import fi.nutrifier.unit.utils.TestObjects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @BeforeEach
    public void setup() {
        TestObjects.reset();
        repository.deleteAll();
    }

    @Test
    public void testSaveUser_ReturnsSavedUser() {
        User user = TestObjects.user1.toUser();
        user.setPassword("qwerty");
        User saved = repository.save(user);

        assertNotNull(saved.getId());
        assertEquals("test@gmail.com", saved.getEmail());
        assertEquals("qwerty", saved.getPassword());
    }

    @Test
    public void testFindById_ReturnsUser() {
        User user = TestObjects.user1.toUser();
        user.setPassword("qwerty");
        User saved = repository.save(user);

        User found = repository.findById(saved.getId()).get();

        assertNotNull(found);
        assertEquals("test@gmail.com", found.getEmail());
        assertEquals("qwerty", found.getPassword());
    }

    @Test
    public void testFindAll_ReturnsMultipleUsers() {
        User user1 = TestObjects.user1.toUser();
        user1.setPassword("qwerty");
        repository.save(user1);

        User user2 = TestObjects.user2.toUser();
        user2.setPassword("qwerty");
        repository.save(user2);

        List<User> found = repository.findAll();

        assertEquals(2, found.size());
        assertEquals("test@gmail.com", found.get(0).getEmail());
        assertEquals("test2@gmail.com", found.get(1).getEmail());
    }

    @Test
    public void testUpdateUser_ReturnsFood() {
        User saved = repository.save(TestObjects.user1.toUser());

        saved.setEmail("changed@gmail.com");
        saved.setPassword("1234");
        User updated = repository.save(saved);

        assertEquals(saved.getId(), updated.getId());
        assertEquals("changed@gmail.com", updated.getEmail());
        assertEquals("1234", updated.getPassword());
    }

    @Test
    public void testDeleteUser_ReturnsEmptyList() {
        User user = TestObjects.user1.toUser();
        user.setPassword("qwerty");
        User saved = repository.save(user);

        repository.delete(saved);
        Optional<User> found = repository.findById(saved.getId());

        assertFalse(found.isPresent());
        assertEquals(0, repository.findAll().size());
    }

    @Test
    public void testFindByEmail_ReturnsUser() {
        User user = TestObjects.user1.toUser();
        user.setPassword("password");
        User saved = repository.save(user);

        User found = repository.findByEmail(saved.getEmail()).get();

        assertNotNull(found);
        assertEquals("test@gmail.com", found.getEmail());
        assertEquals("password", found.getPassword());
    }
}

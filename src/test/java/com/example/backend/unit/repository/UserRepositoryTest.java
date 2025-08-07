package com.example.backend.unit.repository;

import com.example.backend.entities.Role;
import com.example.backend.entities.User;
import com.example.backend.repositories.UserRepository;
import com.example.backend.unit.utils.TestObjects;
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
        User saved = repository.save(TestObjects.user1.toUser());

        assertNotNull(saved.getId());
        assertEquals("test@gmail.com", saved.getEmail());
        assertEquals("password", saved.getPassword());
    }

    @Test
    public void testFindById_ReturnsUser() {
        User saved = repository.save(TestObjects.user1.toUser());

        User found = repository.findById(saved.getId()).get();

        assertNotNull(found);
        assertEquals("test@gmail.com", found.getEmail());
        assertEquals("password", found.getPassword());
    }

    @Test
    public void testFindAll_ReturnsMultipleUsers() {
        repository.save(TestObjects.user1.toUser());
        repository.save(TestObjects.user2.toUser());

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
        User saved = repository.save(TestObjects.user1.toUser());

        repository.delete(saved);
        Optional<User> found = repository.findById(saved.getId());

        assertFalse(found.isPresent());
        assertEquals(0, repository.findAll().size());
    }

    @Test
    public void testFindByEmail_ReturnsUser() {
        User saved = repository.save(TestObjects.user1.toUser());

        User found = repository.findByEmail(saved.getEmail()).get();

        assertNotNull(found);
        assertEquals("test@gmail.com", found.getEmail());
        assertEquals("password", found.getPassword());
    }
}

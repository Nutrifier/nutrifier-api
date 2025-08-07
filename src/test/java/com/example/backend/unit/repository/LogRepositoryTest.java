package com.example.backend.unit.repository;

import com.example.backend.entities.Log;
import com.example.backend.repositories.LogRepository;
import com.example.backend.unit.utils.TestObjects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class LogRepositoryTest {

    @Autowired
    private LogRepository repository;

    @BeforeEach
    public void setup() {
        TestObjects.reset();
        repository.deleteAll();
    }

    @Test
    public void testSaveLog_ReturnsSavedLog() {
        Log saved = repository.save(TestObjects.log1);

        assertNotNull(saved.getId());
        assertEquals(22, saved.getAmount());
        assertEquals("BREAKFAST", saved.getMeal());
    }

    @Test
    public void testFindById_ReturnsLog() {
        Log saved = repository.save(TestObjects.log1);

        Log found = repository.findById(saved.getId()).get();

        assertNotNull(found);
        assertEquals(22, found.getAmount());
        assertEquals("BREAKFAST", found.getMeal());
    }

    @Test
    public void testFindAll_ReturnsMultipleLogs() {
        repository.save(TestObjects.log1);
        repository.save(TestObjects.log2);

        List<Log> found = repository.findAll();

        assertEquals(2, found.size());
        assertEquals("BREAKFAST", found.get(0).getMeal());
        assertEquals("LUNCH", found.get(1).getMeal());
    }

    @Test
    public void testUpdateUser_ReturnsLog() {
        Log saved = repository.save(TestObjects.log1);

        saved.setAmount(120.0);
        saved.setMeal("SNACKS");
        Log updated = repository.save(saved);

        assertEquals(saved.getId(), updated.getId());
        assertEquals(120, updated.getAmount());
        assertEquals("SNACKS", updated.getMeal());
    }

    @Test
    public void testDeleteUser_ReturnsEmptyList() {
        Log saved = repository.save(TestObjects.log1);

        repository.delete(saved);
        Optional<Log> found = repository.findById(saved.getId());

        assertFalse(found.isPresent());
        assertEquals(0, repository.findAll().size());
    }

    @Test
    public void testFindByDate_ReturnsMultipleLogs() {
        repository.save(TestObjects.log1);
        repository.save(TestObjects.log2);
        repository.save(TestObjects.log3);

        List<Log> found1 = repository.findByDateAndUserId(TestObjects.date, TestObjects.userId1);
        assertEquals(2, found1.size());

        List<Log> found2 = repository.findByDateAndUserId(TestObjects.date, TestObjects.userId2);
        assertEquals(1, found2.size());
    }

    @Test
    public void testFindByUserId_ReturnsMultipleLogs() {
        repository.save(TestObjects.log1);
        repository.save(TestObjects.log2);
        repository.save(TestObjects.log3);

        List<Log> found1 = repository.findByUserId(TestObjects.userId1);
        assertEquals(2, found1.size());

        List<Log> found2 = repository.findByUserId(TestObjects.userId2);
        assertEquals(1, found2.size());
    }
}

package com.example.backend.unit.repository;

import com.example.backend.entities.Food;
import com.example.backend.repositories.FoodRepository;
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
class FoodRepositoryTest {

    @Autowired
    private FoodRepository repository;

    @BeforeEach
    public void setup() {
        TestObjects.reset();
        repository.deleteAll();
    }

    @Test
    public void testSaveFood_ReturnsSavedFood() {
        Food saved = repository.save(TestObjects.food1);

        assertNotNull(saved.getId());
        assertEquals("Kanan rintafilee", saved.getName());
        assertEquals(250.0, saved.getCalories());
    }

    @Test
    public void testFindById_ReturnsFood() {
        Food saved = repository.save(TestObjects.food1);

        Food found = repository.findById(saved.getId()).get();

        assertNotNull(found);
        assertEquals("Kanan rintafilee", found.getName());
        assertEquals(250.0, found.getCalories());
    }

    @Test
    public void testFindAll_ReturnsMultipleFoods() {
        repository.save(TestObjects.food1);
        repository.save(TestObjects.food2);

        List<Food> found = repository.findAll();

        assertEquals(2, found.size());
        assertEquals("Kanan rintafilee", found.get(0).getName());
        assertEquals("Riisi (keitetty)", found.get(1).getName());
    }

    @Test
    public void testUpdateFood_ReturnsFood() {
        Food saved = repository.save(TestObjects.food1);

        saved.setName("Laktoositon maito");
        saved.setCalories(35.0);
        Food updated = repository.save(saved);

        assertEquals(saved.getId(), updated.getId());
        assertEquals("Laktoositon maito", updated.getName());
        assertEquals(35.0, updated.getCalories());
    }

    @Test
    public void testDeleteFood_ReturnsEmptyList() {
        Food saved = repository.save(TestObjects.food1);

        repository.delete(saved);
        Optional<Food> found = repository.findById(saved.getId());

        assertFalse(found.isPresent());
        assertEquals(0, repository.findAll().size());
    }

    @Test
    public void testFindFoodsByName_ReturnsFoods() {
        repository.save(TestObjects.food1);
        repository.save(TestObjects.food2);
        repository.save(TestObjects.food3);

        List<Food> found = repository.findFoodsByNameContainingIgnoreCase("kA");

        assertEquals(2, found.size());
        assertEquals("Kanan rintafilee", found.get(0).getName());
        assertEquals("Kalkkunaleike", found.get(1).getName());
    }

    @Test
    public void testFindFoodsByName_ReturnsEmptyList() {
        repository.save(TestObjects.food1);
        repository.save(TestObjects.food2);
        repository.save(TestObjects.food3);

        List<Food> found = repository.findFoodsByNameContainingIgnoreCase("mai");

        assertEquals(0, found.size());
    }
}

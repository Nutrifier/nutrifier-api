package fi.nutrifier.unit.repository;

import fi.nutrifier.entities.FoodEntry;
import fi.nutrifier.entities.MealType;
import fi.nutrifier.entities.ServingType;
import fi.nutrifier.repositories.FoodEntryRepository;
import fi.nutrifier.repositories.reference.MealTypeRepository;
import fi.nutrifier.repositories.reference.ServingTypeRepository;
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
class FoodEntryRepositoryTest {

    @Autowired
    private FoodEntryRepository repository;

    @Autowired
    private MealTypeRepository mealTypeRepository;

    @Autowired
    private ServingTypeRepository servingTypeRepository;

    @BeforeEach
    public void setup() {
        TestObjects.reset();
        repository.deleteAll();

        MealType savedMealTypeBreakfast = mealTypeRepository.save(TestObjects.MEAL_TYPE_BREAKFAST);
        MealType savedMealTypeLunch = mealTypeRepository.save(TestObjects.MEAL_TYPE_LUNCH);
        TestObjects.foodEntry1.setMealType(savedMealTypeBreakfast);
        TestObjects.foodEntry2.setMealType(savedMealTypeLunch);
        TestObjects.foodEntry3.setMealType(savedMealTypeBreakfast);

        ServingType savedServingType = servingTypeRepository.save(TestObjects.SERVING_TYPE_GRAMS);
        TestObjects.foodEntry1.setServingType(savedServingType);
        TestObjects.foodEntry2.setServingType(savedServingType);
        TestObjects.foodEntry3.setServingType(savedServingType);
    }

    @Test
    public void testSaveLog_ReturnsSavedLog() {
        FoodEntry saved = repository.save(TestObjects.foodEntry1);

        assertNotNull(saved.getId());
        assertEquals(22, saved.getServingAmount());
        assertEquals(TestObjects.MEAL_TYPE_BREAKFAST.getName(), saved.getMealType().getName());
    }

    @Test
    public void testFindById_ReturnsLog() {
        FoodEntry saved = repository.save(TestObjects.foodEntry1);
        FoodEntry found = repository.findById(saved.getId()).get();

        assertNotNull(found);
        assertEquals(22, found.getServingAmount());
        assertEquals(TestObjects.MEAL_TYPE_BREAKFAST.getName(), found.getMealType().getName());
    }

    @Test
    public void testFindAll_ReturnsMultipleLogs() {
        repository.save(TestObjects.foodEntry1);
        repository.save(TestObjects.foodEntry2);

        List<FoodEntry> found = repository.findAll();

        assertEquals(2, found.size());
        assertEquals(TestObjects.MEAL_TYPE_BREAKFAST.getName(), found.get(0).getMealType().getName());
        assertEquals(TestObjects.MEAL_TYPE_LUNCH.getName(), found.get(1).getMealType().getName());
    }

    @Test
    public void testUpdateUser_ReturnsLog() {
        FoodEntry saved = repository.save(TestObjects.foodEntry1);

        saved.setServingAmount(120.0);
        saved.setMealType(TestObjects.MEAL_TYPE_SNACKS);
        FoodEntry updated = repository.save(saved);

        assertEquals(saved.getId(), updated.getId());
        assertEquals(120, updated.getServingAmount());
        assertEquals(TestObjects.MEAL_TYPE_SNACKS, updated.getMealType());
    }

    @Test
    public void testDeleteUser_ReturnsEmptyList() {
        FoodEntry saved = repository.save(TestObjects.foodEntry1);

        repository.delete(saved);
        Optional<FoodEntry> found = repository.findById(saved.getId());

        assertFalse(found.isPresent());
        assertEquals(0, repository.findAll().size());
    }

    @Test
    public void testFindByDate_ReturnsMultipleLogs() {
        repository.save(TestObjects.foodEntry1);
        repository.save(TestObjects.foodEntry2);
        repository.save(TestObjects.foodEntry3);

        List<FoodEntry> found1 = repository.findByDateAndUserId(TestObjects.date, TestObjects.id1);
        assertEquals(2, found1.size());

        List<FoodEntry> found2 = repository.findByDateAndUserId(TestObjects.date, TestObjects.id2);
        assertEquals(0, found2.size());
    }
}

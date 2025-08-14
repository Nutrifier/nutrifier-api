package fi.nutrifier.unit.service;

import fi.nutrifier.config.SecurityConfig;
import fi.nutrifier.entities.Food;
import fi.nutrifier.repositories.FoodRepository;
import fi.nutrifier.services.FoodService;
import fi.nutrifier.unit.utils.TestObjects;
import fi.nutrifier.utils.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class FoodServiceTest {

    @InjectMocks
    private FoodService service;

    @Mock
    private FoodRepository repository;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    public void setup() {
        TestObjects.reset();
        repository.deleteAll();
    }

    @Test
    @WithMockUser
    public void testSaveFood_ReturnsFood() {
        when(repository.save(any(Food.class))).thenReturn(TestObjects.food1);

        ResponseEntity<Food> response = service.create(TestObjects.food1);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Kanan rintafilee", response.getBody().getName());
        assertEquals(250.0, response.getBody().getCalories());
    }

    @Test
    public void testFindById_ReturnsFood() {
        when(repository.findById(TestObjects.id)).thenReturn(Optional.ofNullable(TestObjects.food1));

        ResponseEntity<Food> response = service.getById(TestObjects.id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Kanan rintafilee", response.getBody().getName());
        assertEquals(250.0, response.getBody().getCalories());
    }

    @Test
    public void testGetAll_ReturnsMultipleFoods() {
        List<Food> foods = new ArrayList<>();
        foods.add(TestObjects.food1);
        foods.add(TestObjects.food2);

        Pageable pageable = PageRequest.of(1, 10);
        Page<Food> mockPage = new PageImpl<>(foods, pageable, foods.size());

        when(repository.findAll(any(Pageable.class))).thenReturn(mockPage);

        ResponseEntity<Page<Food>> response = service.getAll(1, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getContent().size());
    }

    @Test
    public void testUpdateFood_ReturnsFood() {
        when(repository.findById(TestObjects.id)).thenReturn(Optional.of(TestObjects.food1));
        when(repository.save(any(Food.class))).thenReturn(TestObjects.food1);

        TestObjects.food1.setName("Riisi");
        TestObjects.food1.setCalories(245.0);
        ResponseEntity<Food> response = service.update(TestObjects.id, TestObjects.food1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Riisi", response.getBody().getName());
        assertEquals(245.0, response.getBody().getCalories());
    }

    @Test
    public void testDeleteFood_ReturnsNullBody() {
        doNothing().when(repository).deleteById(TestObjects.id);

        ResponseEntity<Food> response = service.delete(TestObjects.id);

        verify(repository, times(1)).deleteById(TestObjects.id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetFoodsByQuery_ReturnsMultipleFoods() {
        List<Food> foods = List.of(TestObjects.food1);

        when(repository.findFoodsByNameContainingIgnoreCase(anyString())).thenReturn(foods);

        // Act
        ResponseEntity<List<Food>> response = service.getFoodsByQuery("ka");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Kanan rintafilee", response.getBody().get(0).getName());
    }
}

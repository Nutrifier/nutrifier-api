package fi.nutrifier.unit.service;

import fi.nutrifier.config.SecurityConfig;
import fi.nutrifier.dto.FoodRequest;
import fi.nutrifier.dto.FoodResponse;
import fi.nutrifier.entities.Food;
import fi.nutrifier.mappers.FoodMapper;
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
import org.springframework.test.context.ActiveProfiles;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Mock
    private FoodMapper mapper;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    public void setup() {
        TestObjects.reset();
        repository.deleteAll();
    }

    @Test
    public void testSaveFood_ReturnsFood() {
        when(mapper.toEntity(any(UUID.class), any(FoodRequest.class)))
                .thenReturn(TestObjects.food1);
        when(mapper.toResponse(any(Food.class)))
                .thenReturn(TestObjects.food1.toResponse());
        when(repository.save(any(Food.class)))
                .thenReturn(TestObjects.food1);

        ResponseEntity<FoodResponse> response = service.create(TestObjects.food1.toRequest(), TestObjects.id1);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Kanan rintafilee", response.getBody().getName());
        assertEquals(250.0, response.getBody().getCalories());
    }

    @Test
    public void testFindById_ReturnsFood() {
        when(repository.findById(TestObjects.id)).thenReturn(Optional.ofNullable(TestObjects.food1));
        when(mapper.toResponse(any(Food.class))).thenReturn(TestObjects.food1.toResponse());

        ResponseEntity<FoodResponse> response = service.getById(TestObjects.id);

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

        ResponseEntity<Page<FoodResponse>> response = service.getAll(1, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getContent().size());
    }

    @Test
    public void testUpdateFood_ReturnsFood() {
        TestObjects.food1.setName("Riisi");
        TestObjects.food1.setCalories(245.0);

        when(mapper.toResponse(any(Food.class))).thenReturn(TestObjects.food1.toResponse());
        when(repository.findById(TestObjects.id)).thenReturn(Optional.of(TestObjects.food1));
        when(repository.save(any(Food.class))).thenReturn(TestObjects.food1);

        ResponseEntity<FoodResponse> response = service.update(TestObjects.id, TestObjects.id1, TestObjects.food1.toRequest());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Riisi", response.getBody().getName());
        assertEquals(245.0, response.getBody().getCalories());
    }

    @Test
    public void testDeleteFood_ReturnsNullBody() {
        doNothing().when(repository).deleteById(TestObjects.id);

        ResponseEntity<FoodResponse> response = service.delete(TestObjects.id);

        verify(repository, times(1)).deleteById(TestObjects.id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetFoodsByQuery_ReturnsMultipleFoods() {
        List<Food> foods = List.of(TestObjects.food1);
        Page<Food> foodPage = new PageImpl<>(foods);


        when(repository.findFoodsByNameContainingIgnoreCase(anyString(), any(Pageable.class))).thenReturn(foodPage);
        when(mapper.toResponse(any(Food.class))).thenAnswer(invocation -> {
            Food f = invocation.getArgument(0);
            return new FoodResponse(
                    f.getId(),
                    f.getName(),
                    f.getBrand(),
                    f.getCategory(),
                    f.getBarcode(),
                    f.getServingSize(),
                    f.getCalories(),
                    f.getCarbs(),
                    f.getProtein(),
                    f.getFat(),
                    f.getVerified(),
                    f.getStatus()
            );
        });

        // Act
        ResponseEntity<Page<FoodResponse>> response = service.getFoodsByQuery(0, 10, "ka");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getContent().size());
        assertEquals("Kanan rintafilee", response.getBody().getContent().get(0).getName());
    }
}

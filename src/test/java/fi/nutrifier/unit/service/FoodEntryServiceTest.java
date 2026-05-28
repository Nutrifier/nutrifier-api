package fi.nutrifier.unit.service;

import fi.nutrifier.config.SecurityConfig;
import fi.nutrifier.dto.ApiResponse;
import fi.nutrifier.dto.FoodEntryResponse;
import fi.nutrifier.entities.FoodEntry;
import fi.nutrifier.enums.MealType;
import fi.nutrifier.repositories.FoodEntryRepository;
import fi.nutrifier.services.FoodEntryService;
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
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class FoodEntryServiceTest {

    @InjectMocks
    private FoodEntryService service;

    @Mock
    private FoodEntryRepository repository;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    public void setup() {
        TestObjects.reset();
        repository.deleteAll();
    }

    @Test
    public void testSaveLog_ReturnsLog() {
        when(repository.save(any(FoodEntry.class))).thenReturn(TestObjects.foodEntry1);

        ApiResponse<FoodEntryResponse> response = service.create(TestObjects.id1, TestObjects.foodEntry1.toRequest());

        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertEquals(22, response.getData().getAmount());
        assertEquals("BREAKFAST", response.getData().getMealType());
    }

    @Test
    public void testFindById_ReturnsLog() {
        when(repository.findByIdAndUserId(TestObjects.id, TestObjects.id1))
                .thenReturn(Optional.ofNullable(TestObjects.foodEntry1));

        ApiResponse<FoodEntryResponse> response = service.getByIdAndUserId(TestObjects.id, TestObjects.id1);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(22, response.getData().getAmount());
        assertEquals("BREAKFAST", response.getData().getMealType());
    }

    @Test
    public void testFindAll_ReturnsMultipleLogs() {
        List<FoodEntry> foodEntries = List.of(TestObjects.foodEntry1, TestObjects.foodEntry2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<FoodEntry> mockPage = new PageImpl<>(foodEntries, pageable, foodEntries.size());

        when(repository.findAll(pageable)).thenReturn(mockPage);

        ApiResponse<Page<FoodEntryResponse>> response = service.getAll(0, 10);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(2, response.getData().getContent().size());
    }

    @Test
    public void testUpdateUser_ReturnsLog() {
        when(repository.findByIdAndUserId(TestObjects.id, TestObjects.id1)).thenReturn(Optional.of(TestObjects.foodEntry1));
        when(repository.save(any(FoodEntry.class))).thenReturn(TestObjects.foodEntry1);

        TestObjects.foodEntry1.setMealType(MealType.LUNCH);
        TestObjects.foodEntry1.setAmount(54.0);
        ApiResponse<FoodEntryResponse> response = service.update(TestObjects.id1, TestObjects.id, TestObjects.foodEntry1);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.getData());
        assertEquals("LUNCH", response.getData().getMealType());
        assertEquals(54, response.getData().getAmount());
    }

    @Test
    public void testDeleteUser_ReturnsNullBody() {
        doNothing().when(repository).deleteById(TestObjects.id);

        ApiResponse<FoodEntryResponse> response = service.delete(TestObjects.id1, TestObjects.id);

        verify(repository, times(1)).deleteByIdAndUserId(TestObjects.id, TestObjects.id1);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertNull(response.getData());
    }

    @Test
    public void testFindLogsByDate_ReturnsMultipleLogs() {
        List<FoodEntry> foodEntries = new ArrayList<>();
        foodEntries.add(TestObjects.foodEntry1);
        foodEntries.add(TestObjects.foodEntry2);

        when(repository.findByDateAndUserId(TestObjects.date, TestObjects.id)).thenReturn(foodEntries);

        ApiResponse<List<FoodEntryResponse>> response = service.getLogsByDateAndUser(TestObjects.date, TestObjects.id);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(2, response.getData().size());
    }
}

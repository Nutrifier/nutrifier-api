package fi.nutrifier.unit.service;

import fi.nutrifier.config.SecurityConfig;
import fi.nutrifier.dto.FoodEntryResponse;
import fi.nutrifier.entities.FoodEntry;
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
import org.springframework.http.ResponseEntity;
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

        ResponseEntity<FoodEntryResponse> response = service.create(TestObjects.userId1, TestObjects.foodEntry1Request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(22, response.getBody().getAmount());
        assertEquals("BREAKFAST", response.getBody().getMealType());
    }

    @Test
    public void testFindById_ReturnsLog() {
        when(repository.findByIdAndUserId(TestObjects.id, TestObjects.userId1))
                .thenReturn(Optional.ofNullable(TestObjects.foodEntry1));

        ResponseEntity<FoodEntryResponse> response = service.getByIdAndUserId(TestObjects.id, TestObjects.userId1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(22, response.getBody().getAmount());
        assertEquals("BREAKFAST", response.getBody().getMealType());
    }

    @Test
    public void testFindAll_ReturnsMultipleLogs() {
        List<FoodEntry> foodEntries = List.of(TestObjects.foodEntry1, TestObjects.foodEntry2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<FoodEntry> mockPage = new PageImpl<>(foodEntries, pageable, foodEntries.size());

        when(repository.findAll(pageable)).thenReturn(mockPage);

        ResponseEntity<Page<FoodEntryResponse>> response = service.getAll(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getContent().size());
    }

    @Test
    public void testUpdateUser_ReturnsLog() {
        when(repository.findByIdAndUserId(TestObjects.id, TestObjects.userId1)).thenReturn(Optional.of(TestObjects.foodEntry1));
        when(repository.save(any(FoodEntry.class))).thenReturn(TestObjects.foodEntry1);

        TestObjects.foodEntry1.setMealType("LUNCH");
        TestObjects.foodEntry1.setAmount(54.0);
        ResponseEntity<FoodEntryResponse> response = service.update(TestObjects.userId1, TestObjects.id, TestObjects.foodEntry1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("LUNCH", response.getBody().getMealType());
        assertEquals(54, response.getBody().getAmount());
    }

    @Test
    public void testDeleteUser_ReturnsNullBody() {
        doNothing().when(repository).deleteById(TestObjects.id);

        ResponseEntity<FoodEntryResponse> response = service.delete(TestObjects.userId1, TestObjects.id);

        verify(repository, times(1)).deleteByIdAndUserId(TestObjects.id, TestObjects.userId1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testFindLogsByDate_ReturnsMultipleLogs() {
        List<FoodEntry> foodEntries = new ArrayList<>();
        foodEntries.add(TestObjects.foodEntry1);
        foodEntries.add(TestObjects.foodEntry2);

        when(repository.findByDateAndUserId(TestObjects.date, TestObjects.id)).thenReturn(foodEntries);

        ResponseEntity<List<FoodEntryResponse>> response = service.getLogsByDateAndUser(TestObjects.date, TestObjects.id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }
}

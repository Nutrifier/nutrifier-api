package fi.nutrifier.unit.service;

import fi.nutrifier.config.SecurityConfig;
import fi.nutrifier.dto.AnalyticsFullResponse;
import fi.nutrifier.dto.AnalyticsSingleResponse;
import fi.nutrifier.dto.FoodEntryResponse;
import fi.nutrifier.entities.FoodEntry;
import fi.nutrifier.enums.AnalyticsTimePeriod;
import fi.nutrifier.enums.DayGoalResult;
import fi.nutrifier.enums.MealType;
import fi.nutrifier.repositories.*;
import fi.nutrifier.services.AnalyticsService;
import fi.nutrifier.services.FoodEntryService;
import fi.nutrifier.services.FoodUsageService;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class AnalyticsServiceTest {

    @InjectMocks
    private AnalyticsService service;

    @Mock
    private FoodUsageService foodUsageService;

    @Mock
    private FoodEntryRepository repository;

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private GoalsRepository goalsRepository;

    @Mock
    private DailySummaryRepository dailySummaryRepository;

    @Mock
    private FoodUsageRepository foodUsageRepository;

    @Mock
    private FoodEntryRepository foodEntryRepository;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    public void setup() {
        TestObjects.reset();
        repository.deleteAll();
    }

    @Test
    public void testCalculateAnalyticsByDate_ReturnsAnalytics() {
        when(foodEntryRepository.findByDateAndUserId(any(LocalDate.class), any(UUID.class)))
                .thenReturn(List.of(TestObjects.foodEntry3));
        when(goalsRepository.findByUserId(any(UUID.class)))
                .thenReturn(Optional.of(TestObjects.goals));

        ResponseEntity<AnalyticsSingleResponse> response = service.calculateAnalyticsByDate(
                LocalDate.parse("2026-01-01"),
                TestObjects.id1,
                TestObjects.dailySummary
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(DayGoalResult.FAILED, response.getBody().getResult());
        assertEquals(1800.0, response.getBody().getTotalConsumedCalories());
    }

    @Test
    public void testCalculateAnalyticsWithinDateRange_ReturnsAnalytics() {
        when(foodEntryRepository.findByDateBetweenAndUserId(any(LocalDate.class), any(LocalDate.class), any(UUID.class)))
                .thenReturn(List.of(TestObjects.foodEntry3));
        when(goalsRepository.findByUserId(any(UUID.class)))
                .thenReturn(Optional.of(TestObjects.goals));
        when(dailySummaryRepository.findByDateAndUserId(any(LocalDate.class), any(UUID.class)))
                .thenReturn(TestObjects.dailySummary);

        ResponseEntity<AnalyticsFullResponse> response = service.calculateAnalyticsWithinDateRange(
                LocalDate.parse("2026-01-01"),
                TestObjects.id1,
                AnalyticsTimePeriod.WEEK
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1800.0, response.getBody().getTotalConsumedCalories());
    }
}

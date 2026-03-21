package fi.nutrifier.services;

import fi.nutrifier.dto.DailyNutritionSummaryCreateRequest;
import fi.nutrifier.dto.DailyNutritionSummaryResponse;
import fi.nutrifier.dto.DailyNutritionSummaryUpdateRequest;
import fi.nutrifier.entities.DailyNutritionSummary;
import fi.nutrifier.mappers.DailyNutritionSummaryMapperWithUpdate;
import fi.nutrifier.repositories.DailyNutritionSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class DailyNutritionSummaryService {

    private final DailyNutritionSummaryRepository repository;
    private final DailyNutritionSummaryMapperWithUpdate mapper;

    @Autowired
    public DailyNutritionSummaryService(DailyNutritionSummaryRepository repository, DailyNutritionSummaryMapperWithUpdate mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ResponseEntity<DailyNutritionSummaryResponse> create(UUID userId, DailyNutritionSummaryCreateRequest request) {
        try {
            DailyNutritionSummary dailyNutritionSummary = repository.save(mapper.toEntity(userId, request));
            return new ResponseEntity<>(mapper.toResponse(dailyNutritionSummary), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<DailyNutritionSummaryResponse> update(UUID userId, UUID id, DailyNutritionSummaryUpdateRequest request) {
        try {
            DailyNutritionSummary existingEntity = repository.findByIdAndUserId(id, userId).orElse(null);

            if (existingEntity != null) {
                mapper.updateEntityFromRequest(request, existingEntity);

                DailyNutritionSummary saved = repository.save(existingEntity);
                return new ResponseEntity<>(mapper.toResponse(saved), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Page<DailyNutritionSummaryResponse>> getAll(Integer page, Integer size) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<DailyNutritionSummary> dailyNutrientsPage = repository.findAll(pageRequest);

            Page<DailyNutritionSummaryResponse> dtoPage = dailyNutrientsPage.map(mapper::toResponse);

            return new ResponseEntity<>(dtoPage, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Page<DailyNutritionSummaryResponse>> getAllByUserId(UUID userId, Integer page, Integer size) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<DailyNutritionSummary> dailyNutrientsPage = repository.findByUserId(userId, pageRequest);

            Page<DailyNutritionSummaryResponse> dtoPage = dailyNutrientsPage.map(mapper::toResponse);

            return new ResponseEntity<>(dtoPage, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<DailyNutritionSummaryResponse> getByDateAndUser(LocalDate date, UUID userId) {
        try {
            DailyNutritionSummary found = repository.findByDateAndUserId(date, userId);
            return new ResponseEntity<>(mapper.toResponse(found), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
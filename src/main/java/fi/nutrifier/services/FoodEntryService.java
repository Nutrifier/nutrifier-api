package fi.nutrifier.services;

import fi.nutrifier.dto.FoodEntryRequest;
import fi.nutrifier.dto.FoodEntryResponse;
import fi.nutrifier.entities.FoodEntry;
import fi.nutrifier.entities.FoodUsage;
import fi.nutrifier.mappers.FoodEntryMapper;
import fi.nutrifier.repositories.FoodEntryRepository;
import fi.nutrifier.repositories.FoodUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FoodEntryService {

    private final FoodEntryRepository repository;
    private final FoodUsageRepository usageRepository;
    private final FoodEntryMapper mapper;

    @Autowired
    public FoodEntryService(
            FoodEntryRepository repository,
            FoodUsageRepository usageRepository,
            FoodEntryMapper mapper
    ) {
        this.repository = repository;
        this.usageRepository = usageRepository;
        this.mapper = mapper;
    }

    public ResponseEntity<FoodEntryResponse> create(UUID userId, FoodEntryRequest request) {
        try {
            FoodEntry data = repository.save(mapper.toEntity(userId, request));

            // Log usage of the food
            FoodUsage usage = usageRepository.findByUserIdAndFoodId(userId, data.getFoodId()).orElse(null);
            LocalDateTime now = LocalDateTime.now();
            if (usage != null) {
                usage.setUsageCount(usage.getUsageCount() + 1);
                usage.setLastUsedAt(now);
                usageRepository.save(usage);
            } else {
                FoodUsage newUsage = new FoodUsage(userId, data.getFoodId(), 1, now);
                usageRepository.save(newUsage);
            }

            return new ResponseEntity<>(mapper.toResponse(data), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Page<FoodEntryResponse>> getAll(Integer page, Integer size) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<FoodEntry> foodEntryPage = repository.findAll(pageRequest);

            Page<FoodEntryResponse> dtoPage = foodEntryPage.map(mapper::toResponse);

            return new ResponseEntity<>(dtoPage, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Page<FoodEntryResponse>> getAllByUserId(UUID userId, Integer page, Integer size) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<FoodEntry> foodEntryPage = repository.findByUserId(userId, pageRequest);

            Page<FoodEntryResponse> dtoPage = foodEntryPage.map(mapper::toResponse);

            return new ResponseEntity<>(dtoPage, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<FoodEntryResponse> getById(UUID id) {
        try {
            FoodEntry data = repository.findById(id).orElse(null);
            if (data == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(mapper.toResponse(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<FoodEntryResponse> getByIdAndUserId(UUID id, UUID userId) {
        try {
            FoodEntry data = repository.findByIdAndUserId(id, userId).orElse(null);
            if (data == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(mapper.toResponse(data), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<FoodEntryResponse> update(UUID userId, UUID id, FoodEntry entity) {
        try {
            FoodEntry existingEntity = repository.findByIdAndUserId(id, userId).orElse(null);

            if (existingEntity != null) {
                FoodEntry data = repository.save(entity);
                return new ResponseEntity<>(mapper.toResponse(data), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<FoodEntryResponse> delete(UUID userId, UUID id) {
        try {
            repository.deleteByIdAndUserId(id, userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<FoodEntryResponse>> getLogsByDateAndUser(LocalDate date, UUID userId) {
        try {
            List<FoodEntry> foodEntries = repository.findByDateAndUserId(date, userId);
            List<FoodEntryResponse> mapped = foodEntries.stream().map(mapper::toResponse).toList();

            return new ResponseEntity<>(mapped, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
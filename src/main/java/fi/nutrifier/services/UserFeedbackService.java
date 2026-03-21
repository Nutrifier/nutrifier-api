package fi.nutrifier.services;

import fi.nutrifier.dto.*;
import fi.nutrifier.entities.*;
import fi.nutrifier.mappers.UserFeedbackMapper;
import fi.nutrifier.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserFeedbackService {

    private final UserFeedbackRepository repository;
    private final UserFeedbackMapper mapper;

    @Autowired
    public UserFeedbackService(
            UserFeedbackRepository repository,
            UserFeedbackMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ResponseEntity<String> createFeedback(UUID userId, UserFeedbackCreateRequest request) {
        try {
            UserFeedback feedback = mapper.toEntity(userId, request);
            repository.save(feedback);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> reviewFeedback(UUID feedbackId, UUID userId, UserFeedbackReviewRequest request) {
        try {
            UserFeedback existing = repository.findById(feedbackId).orElse(null);

            if (existing != null) {
                mapper.updateRequestToEntity(userId, request, existing);
                repository.save(existing);

                return new ResponseEntity<>(HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Page<UserFeedbackResponse>> getAllFeedbacks(Integer page, Integer size) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<UserFeedback> feedbackPage = repository.findAll(pageRequest);

            Page<UserFeedbackResponse> dtoPage = feedbackPage.map(mapper::toResponse);

            return new ResponseEntity<>(dtoPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
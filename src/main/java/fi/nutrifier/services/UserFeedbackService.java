package fi.nutrifier.services;

import fi.nutrifier.dto.*;
import fi.nutrifier.entities.*;
import fi.nutrifier.exceptions.UserFeedbackNotFoundException;
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

    @Autowired
    public UserFeedbackService(UserFeedbackRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<String> createFeedback(UUID userId, UserFeedbackCreateRequest request) {
        repository.save(request.toEntity(userId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<String> reviewFeedback(UUID feedbackId, UUID userId, UserFeedbackReviewRequest request) {
        UserFeedback existing = repository.findById(feedbackId).orElseThrow(UserFeedbackNotFoundException::new);

        existing.updateRequestToEntity(userId, request);
        repository.save(existing);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Page<UserFeedbackResponse>> getAllFeedbacks(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<UserFeedbackResponse> dtoPage = repository.findAll(pageRequest).map(UserFeedback::toResponse);

        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }
}
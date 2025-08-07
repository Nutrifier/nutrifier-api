package com.example.backend.unit.service;

import com.example.backend.config.SecurityConfig;
import com.example.backend.entities.Log;
import com.example.backend.entities.User;
import com.example.backend.exceptions.EncryptionKeyException;
import com.example.backend.exceptions.FailedCryptionException;
import com.example.backend.repositories.LogRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.LogService;
import com.example.backend.services.UserService;
import com.example.backend.unit.utils.TestObjects;
import com.example.backend.utils.JwtTokenUtil;
import com.example.backend.utils.SecurityUtil;
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

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class LogServiceTest {

    @InjectMocks
    private LogService service;

    @Mock
    private LogRepository repository;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    public void setup() {
        TestObjects.reset();
        repository.deleteAll();
    }

    @Test
    public void testSaveLog_ReturnsLog() {
        when(repository.save(any(Log.class))).thenReturn(TestObjects.log1);

        ResponseEntity<Log> response = service.create(TestObjects.log1);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(22, response.getBody().getAmount());
        assertEquals("BREAKFAST", response.getBody().getMeal());
    }

    @Test
    public void testFindById_ReturnsLog() {
        when(repository.findById(TestObjects.id)).thenReturn(Optional.ofNullable(TestObjects.log1));

        ResponseEntity<Log> response = service.getById(TestObjects.id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(22, response.getBody().getAmount());
        assertEquals("BREAKFAST", response.getBody().getMeal());
    }

    @Test
    public void testFindAll_ReturnsMultipleLogs() {
        List<Log> logs = List.of(TestObjects.log1, TestObjects.log2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Log> mockPage = new PageImpl<>(logs, pageable, logs.size());

        when(repository.findAll(pageable)).thenReturn(mockPage);

        ResponseEntity<Page<Log>> response = service.getAll(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getTotalElements());
    }

    @Test
    public void testUpdateUser_ReturnsLog() {
        when(repository.findById(TestObjects.id)).thenReturn(Optional.of(TestObjects.log1));
        when(repository.save(any(Log.class))).thenReturn(TestObjects.log1);

        TestObjects.log1.setMeal("LUNCH");
        TestObjects.log1.setAmount(54.0);
        ResponseEntity<Log> response = service.update(TestObjects.id, TestObjects.log1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("LUNCH", response.getBody().getMeal());
        assertEquals(54, response.getBody().getAmount());
    }

    @Test
    public void testDeleteUser_ReturnsNullBody() {
        doNothing().when(repository).deleteById(TestObjects.id);

        ResponseEntity<Log> response = service.delete(TestObjects.id);

        verify(repository, times(1)).deleteById(TestObjects.id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testFindLogsByDate_ReturnsMultipleLogs() {
        List<Log> logs = new ArrayList<>();
        logs.add(TestObjects.log1);
        logs.add(TestObjects.log2);

        when(repository.findByDateAndUserId(TestObjects.date, TestObjects.id)).thenReturn(logs);

        ResponseEntity<List<Log>> response = service.getLogsByDateAndUser(TestObjects.date, TestObjects.id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testFindLogsByUser_ReturnsMultipleLogs() {
        List<Log> logs = new ArrayList<>();
        logs.add(TestObjects.log1);
        logs.add(TestObjects.log2);

        when(repository.findByUserId(TestObjects.userId1)).thenReturn(logs);

        ResponseEntity<List<Log>> response = service.getLogsByUserId(TestObjects.userId1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }
}

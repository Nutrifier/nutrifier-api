package fi.nutrifier.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.nutrifier.controllers.WeightController;
import fi.nutrifier.entities.WeightEntry;
import fi.nutrifier.services.*;
import fi.nutrifier.unit.utils.TestObjects;
import fi.nutrifier.utils.JwtTokenUtil;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeightController.class)
@ActiveProfiles("test")
public class WeightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserWeightService userWeightService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    public void setup() {
        TestObjects.reset();
    }

    @Test
    @WithMockUser
    public void testGetUsersWeightEntries_ReturnOk() throws Exception {
        UUID id = UUID.randomUUID();
        TestObjects.user1.setId(id); // Mock id generation

        Pageable pageable = PageRequest.of(0, 10);
        Page<WeightEntry> mockPage = new PageImpl<>(TestObjects.weightEntries, pageable, 10);

        when(userWeightService.getByUserId(any(UUID.class), anyInt(), anyInt()))
                .thenReturn(new ResponseEntity<>(mockPage, HttpStatus.OK));

        mockMvc.perform(get("/api/users/weight")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwt().jwt(jwt -> jwt.subject(TestObjects.userId1.toString()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].weight", CoreMatchers.is(84.0)));
    }

    @Test
    @WithMockUser
    public void testAddUsersWeightEntries_ReturnOk() throws Exception {
        WeightEntry newWeightEntry = new WeightEntry();
        newWeightEntry.setWeight(134.5);
        newWeightEntry.setDate(LocalDateTime.now());
        TestObjects.weightEntries = List.of(newWeightEntry);

        when(userWeightService.create(any(UUID.class), any()))
                .thenReturn(new ResponseEntity<>(newWeightEntry, HttpStatus.CREATED));

        mockMvc.perform(post("/api/users/weight")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(134.5))
                .with(jwt().jwt(jwt -> jwt.subject(TestObjects.userId1.toString()))))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.weight", CoreMatchers.is(134.5)));
    }
}

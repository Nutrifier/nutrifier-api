package fi.nutrifier.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.nutrifier.unit.utils.TestObjects;
import fi.nutrifier.utils.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@ActiveProfiles("test")
public abstract class ControllerTestInterface<T> {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected T service;

    @MockBean
    protected JwtTokenUtil jwtTokenUtil;

    @Autowired
    protected ObjectMapper objectMapper;

    protected final String baseUrl;

    protected ControllerTestInterface(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @BeforeEach
    public void setup() {
        TestObjects.reset();
    }
}

package fi.nutrifier.services;

import fi.nutrifier.dto.FineliFoodResponse;
import fi.nutrifier.dto.FoodEntryResponse;
import fi.nutrifier.entities.FoodEntry;
import fi.nutrifier.exceptions.FoodNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class FineliService {

    private final RestTemplate restTemplate;
    private final String FINELI_API_BASE = "https://fineli.fi/fineli/api/v1/";

    @Autowired
    public FineliService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<FineliFoodResponse> getFoodsByQuery(String query) {
        String url = FINELI_API_BASE + "foods?q=" + query;

        FineliFoodResponse[] responseArray = restTemplate.getForObject(url, FineliFoodResponse[].class);
        return responseArray != null ? Arrays.asList(responseArray) : Collections.emptyList();
    }

    public FineliFoodResponse getFoodById(Integer id) {
        String url = FINELI_API_BASE + "foods/" + id;

        FineliFoodResponse response = restTemplate.getForObject(url, FineliFoodResponse.class);
        if (response == null) {
            throw new FoodNotFoundException("Food not found in Fineli database for ID: " + id);
        }
        return response;
    }
}
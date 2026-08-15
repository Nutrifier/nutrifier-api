package fi.nutrifier.services;

import fi.nutrifier.entities.MealType;
import fi.nutrifier.exceptions.NoSuchMealTypeException;
import fi.nutrifier.repositories.MealTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MealTypeService {

    private final MealTypeRepository repository;

    @Autowired
    public MealTypeService(MealTypeRepository repository) {
        this.repository = repository;
    }

    public MealType of(String mealTypeStr) {
        if (mealTypeStr == null) return null;

        return repository.findByNameIgnoreCase(mealTypeStr)
                .orElseThrow(() -> new NoSuchMealTypeException(mealTypeStr));
    }
}
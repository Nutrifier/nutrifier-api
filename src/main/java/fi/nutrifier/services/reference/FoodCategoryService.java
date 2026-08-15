package fi.nutrifier.services.reference;

import fi.nutrifier.entities.FoodCategory;
import fi.nutrifier.repositories.reference.FoodCategoryRepository;
import fi.nutrifier.services.reference.base.ReferenceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FoodCategoryService extends ReferenceDataService<FoodCategory> {

    @Autowired
    public FoodCategoryService(FoodCategoryRepository repository) {
        super(repository, "FoodCategory");
    }
}
package fi.nutrifier.services.reference;

import fi.nutrifier.entities.FoodBrand;
import fi.nutrifier.repositories.reference.FoodBrandRepository;
import fi.nutrifier.services.reference.base.ReferenceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FoodBrandService extends ReferenceDataService<FoodBrand> {

    @Autowired
    public FoodBrandService(FoodBrandRepository repository) {
        super(repository, "FoodBrand");
    }
}
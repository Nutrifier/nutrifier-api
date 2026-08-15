package fi.nutrifier.services.reference;

import fi.nutrifier.entities.MealType;
import fi.nutrifier.repositories.reference.MealTypeRepository;
import fi.nutrifier.services.reference.base.ReferenceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MealTypeService extends ReferenceDataService<MealType> {

    @Autowired
    public MealTypeService(MealTypeRepository repository) {
        super(repository, "MealType");
    }
}
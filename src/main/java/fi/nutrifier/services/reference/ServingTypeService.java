package fi.nutrifier.services.reference;

import fi.nutrifier.entities.ServingType;
import fi.nutrifier.repositories.reference.ServingTypeRepository;
import fi.nutrifier.services.reference.base.ReferenceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServingTypeService extends ReferenceDataService<ServingType> {
    @Autowired
    public ServingTypeService(ServingTypeRepository repository) {
        super(repository, "ServingType");
    }
}
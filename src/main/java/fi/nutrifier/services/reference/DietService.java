package fi.nutrifier.services.reference;

import fi.nutrifier.entities.Diet;
import fi.nutrifier.repositories.reference.DietRepository;
import fi.nutrifier.services.reference.base.ReferenceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DietService extends ReferenceDataService<Diet> {

    @Autowired
    public DietService(DietRepository repository) {
        super(repository, "Diet");
    }
}
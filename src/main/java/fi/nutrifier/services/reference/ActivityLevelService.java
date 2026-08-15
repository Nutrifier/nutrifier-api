package fi.nutrifier.services.reference;

import fi.nutrifier.entities.ActivityLevel;
import fi.nutrifier.repositories.reference.ActivityLevelRepository;
import fi.nutrifier.services.reference.base.ReferenceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityLevelService extends ReferenceDataService<ActivityLevel> {

    @Autowired
    public ActivityLevelService(ActivityLevelRepository repository) {
        super(repository, "ActivityLevel");
    }
}
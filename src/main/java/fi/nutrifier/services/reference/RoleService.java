package fi.nutrifier.services.reference;

import fi.nutrifier.entities.Role;
import fi.nutrifier.repositories.reference.RoleRepository;
import fi.nutrifier.services.reference.base.ReferenceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends ReferenceDataService<Role> {

    @Autowired
    public RoleService(RoleRepository repository) {
        super(repository, "Role");
    }
}
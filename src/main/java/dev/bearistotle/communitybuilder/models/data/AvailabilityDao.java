package dev.bearistotle.communitybuilder.models.data;

import dev.bearistotle.communitybuilder.models.Availability;
import org.springframework.data.repository.CrudRepository;

public interface AvailabilityDao extends CrudRepository<Availability, Integer> {
}

package dev.bearistotle.communitybuilder.models.data;

import dev.bearistotle.communitybuilder.models.Availability;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface AvailabilityDao extends CrudRepository<Availability, Integer> {
}

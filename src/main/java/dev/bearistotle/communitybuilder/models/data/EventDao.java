package dev.bearistotle.communitybuilder.models.data;

import dev.bearistotle.communitybuilder.models.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventDao extends CrudRepository<Event, Integer> {
}

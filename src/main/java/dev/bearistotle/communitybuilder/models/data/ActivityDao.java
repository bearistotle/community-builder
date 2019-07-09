package dev.bearistotle.communitybuilder.models.data;

import dev.bearistotle.communitybuilder.models.Activity;
import org.springframework.data.repository.CrudRepository;

public interface ActivityDao extends CrudRepository<Activity, Integer> {
}

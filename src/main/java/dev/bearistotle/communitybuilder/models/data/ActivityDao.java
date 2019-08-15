package dev.bearistotle.communitybuilder.models.data;

import dev.bearistotle.communitybuilder.models.Activity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface ActivityDao extends CrudRepository<Activity, Integer> {

}

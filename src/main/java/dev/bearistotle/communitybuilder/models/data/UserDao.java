package dev.bearistotle.communitybuilder.models.data;

import dev.bearistotle.communitybuilder.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<User,Integer> {
}

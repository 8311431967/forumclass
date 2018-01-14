package kun.clSystem.repository;

import kun.clSystem.domain.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer> {

	Role findByName(String name);
	
}

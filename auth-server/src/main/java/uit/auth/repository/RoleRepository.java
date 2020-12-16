package uit.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uit.auth.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}

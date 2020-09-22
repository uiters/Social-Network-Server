package uit.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uit.auth.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}

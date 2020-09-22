package uit.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uit.auth.entity.Friend;

public interface FriendRepository extends JpaRepository<Friend, Long> {
}

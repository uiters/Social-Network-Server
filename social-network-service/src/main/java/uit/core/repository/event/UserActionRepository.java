package uit.core.repository.event;

import org.springframework.data.jpa.repository.JpaRepository;
import uit.core.entity.Comment;
import uit.core.entity.event.UserAction;

public interface UserActionRepository extends JpaRepository<UserAction, Long> {
}

package uit.core.repository.event;

import org.springframework.data.jpa.repository.JpaRepository;
import uit.core.entity.Comment;
import uit.core.entity.event.Action;

import java.util.Optional;

public interface ActionRepository extends JpaRepository<Action, Long> {
    Optional<Action> findByAction(long actionId);
}

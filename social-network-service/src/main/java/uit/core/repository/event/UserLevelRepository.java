package uit.core.repository.event;

import org.springframework.data.jpa.repository.JpaRepository;
import uit.core.entity.Comment;
import uit.core.entity.event.UserAction;
import uit.core.entity.event.UserLevel;

import java.util.Optional;

public interface UserLevelRepository extends JpaRepository<UserLevel, Long> {
    Optional<UserLevel> findByUserIdAndPostId(long userId, long postId);
}

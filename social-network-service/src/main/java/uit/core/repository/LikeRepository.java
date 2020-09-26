package uit.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uit.core.entity.Like;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Long deleteByPostIdAndUserId(long postId, long userId);

    List<Like> findAllByPostId(long postId);
}

package uit.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uit.core.entity.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {
}

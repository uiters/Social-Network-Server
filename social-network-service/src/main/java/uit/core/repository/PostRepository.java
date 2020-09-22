package uit.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uit.core.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}

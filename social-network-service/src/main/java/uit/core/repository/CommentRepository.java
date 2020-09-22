package uit.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uit.core.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}

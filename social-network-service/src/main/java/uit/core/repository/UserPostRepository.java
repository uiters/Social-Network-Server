package uit.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uit.core.entity.UserPost;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPostRepository extends JpaRepository<UserPost,Long> {
    Optional<UserPost> findByPostIdAndUserId(long postId, Long id);


    List<UserPost> findAllByUserId(Long id);
}

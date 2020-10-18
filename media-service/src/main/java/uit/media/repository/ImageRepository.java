package uit.media.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uit.media.entity.Image;

import java.util.List;
import java.util.Set;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByPostId(long postId);
}

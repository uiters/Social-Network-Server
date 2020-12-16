package uit.core.repository.event;

import org.springframework.data.jpa.repository.JpaRepository;
import uit.core.entity.Comment;
import uit.core.entity.event.Level;

import java.util.Optional;

public interface LevelRepository extends JpaRepository<Level, Long> {
    Optional<Level> findByName(String name);
}

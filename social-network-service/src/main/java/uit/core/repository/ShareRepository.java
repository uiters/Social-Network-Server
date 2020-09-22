package uit.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uit.core.entity.Share;

public interface ShareRepository extends JpaRepository<Share, Long> {
}

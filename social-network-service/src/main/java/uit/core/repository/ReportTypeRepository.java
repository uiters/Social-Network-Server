package uit.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uit.core.entity.ReportType;

@Repository
public interface ReportTypeRepository extends JpaRepository<ReportType, Long> {
}

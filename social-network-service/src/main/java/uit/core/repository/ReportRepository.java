package uit.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uit.core.entity.Report;

import java.awt.print.Pageable;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
//    Page<Report> findAll(Pageable paging);
}

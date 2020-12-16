package uit.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uit.core.entity.Report;
import uit.core.entity.ReportType;
import uit.core.repository.ReportRepository;
import uit.core.repository.ReportTypeRepository;

@Service
public class ReportService {
    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReportTypeRepository reportTypeRepository;

    public Report create(Report report) {
        return reportRepository.save(report);
    }

    public ReportType createReporType(ReportType reportType) {
        return reportTypeRepository.save(reportType);
    }
}

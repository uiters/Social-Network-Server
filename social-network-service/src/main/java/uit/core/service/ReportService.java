package uit.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uit.core.dto.response.ReportResponse;
import uit.core.entity.Report;
import uit.core.entity.ReportType;
import uit.core.repository.ReportRepository;
import uit.core.repository.ReportTypeRepository;

import java.util.List;
import java.util.Optional;

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

    public List<ReportType> getReportTypes() {
        return reportTypeRepository.findAll();
    }

    public ReportType updateReporType(ReportType reportType, Long id) throws Exception {
        Optional<ReportType> reportTypeDb = reportTypeRepository.findById(id);
        if (reportTypeDb.isEmpty()) {
            throw new Exception("Report Type ID not found");
        }
        ReportType newReportType = reportTypeDb.get();
        newReportType.setName(reportType.getName());
        return reportTypeRepository.save(newReportType);
    }

    public String deleteReportType(Long id) {
        reportTypeRepository.deleteById(id);
        return "Delete report type successfully !";
    }
}

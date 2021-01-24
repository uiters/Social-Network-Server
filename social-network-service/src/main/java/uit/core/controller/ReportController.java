package uit.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uit.core.dto.response.ReportResponse;
import uit.core.entity.Report;
import uit.core.entity.ReportType;
import uit.core.service.ReportService;

import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @PostMapping
    private Report create(@RequestBody Report report) {
        return reportService.create(report);
    }

    @PostMapping("/type")
    private ReportType createTypeReport(@RequestBody ReportType reportType) {
        return reportService.createReporType(reportType);
    }

    @PutMapping("/type/{id}")
    private ReportType updateTypeReport(@PathVariable Long id, @RequestBody ReportType reportType) throws Exception {
        return reportService.updateReporType(reportType, id);
    }

    @DeleteMapping("/type/{id}")
    private String deleteTypeReport(@PathVariable Long id) throws Exception {
        return reportService.deleteReportType(id);
    }

    @GetMapping("/type")
    private List<ReportType> getReportTypes() {
        return reportService.getReportTypes();
    }
}

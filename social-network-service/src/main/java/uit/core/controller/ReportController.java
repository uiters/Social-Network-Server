package uit.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uit.core.entity.Report;
import uit.core.entity.ReportType;
import uit.core.service.ReportService;

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
}

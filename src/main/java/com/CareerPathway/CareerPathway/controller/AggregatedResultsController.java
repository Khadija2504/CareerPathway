package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.dto.AggregatedResultDTO;
import com.CareerPathway.CareerPathway.dto.ProgressMetricsDTO;
import com.CareerPathway.CareerPathway.service.ProgressService;
import com.CareerPathway.CareerPathway.service.UserService;
import com.CareerPathway.CareerPathway.util.PDFReportUtil;
import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.io.IOException;

@RestController
@RequestMapping("/api/admin")
public class AggregatedResultsController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProgressService progressService;

    @Autowired
    private PDFReportUtil pdfReportUtil;

    @GetMapping("/aggregated-results")
    public ResponseEntity<List<AggregatedResultDTO>> getAggregatedResults() {
        List<AggregatedResultDTO> results = userService.calculateAggregatedResults();
        return ResponseEntity.ok(results);
    }

    @GetMapping("/progress-metrics")
    public ResponseEntity<ProgressMetricsDTO> getProgressMetrics(HttpServletRequest request) {
        long employeeId = Integer.parseInt(request.getAttribute("userId").toString());

        ProgressMetricsDTO metrics = progressService.calculateProgressMetrics(employeeId);

        return ResponseEntity.status(HttpStatus.OK).body(metrics);
    }

    @GetMapping("/reports/{employeeId}")
    public ResponseEntity<ProgressMetricsDTO> getReports(@PathVariable long employeeId) {

        ProgressMetricsDTO metrics = progressService.calculateProgressMetrics(employeeId);

        return ResponseEntity.status(HttpStatus.OK).body(metrics);
    }

    @GetMapping("/reports/{employeeId}/download")
    public ResponseEntity<ByteArrayResource> downloadReport(@PathVariable long employeeId) throws IOException, DocumentException {
        ProgressMetricsDTO metrics = progressService.calculateProgressMetrics(employeeId);
        byte[] pdfBytes = pdfReportUtil.generatePdfReport(metrics);

        ByteArrayResource resource = new ByteArrayResource(pdfBytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employee_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdfBytes.length)
                .body(resource);
    }
}

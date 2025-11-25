/*
package com.abhishek.resultService.controller;

import com.abhishek.resultService.dto.AttemptDTO;
import com.abhishek.resultService.model.Result;
import com.abhishek.resultService.service.ResultsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.ByteArrayOutputStream;
import java.util.List;


@RestController
@RequestMapping("/results")
@RequiredArgsConstructor
public class ResultsController {
    private final ResultsService service;
    @GetMapping("/{attemptId}")
    public ResponseEntity<Result> getByAttempt(@PathVariable String attemptId) {
        Result r = service.getResultByAttemptId(attemptId);
        if (r == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(r);
    }

    @GetMapping("/test/{testId}")
    public ResponseEntity<List<Result>> getByTest(@PathVariable String testId) {
        return ResponseEntity.ok(service.getResultsByTestId(testId));
    }

    // Export CSV for a test
    @GetMapping(value = "/export/csv/{testId}", produces = "text/csv")
    public ResponseEntity<byte[]> exportCsv(@PathVariable String testId) throws Exception {
        List<Result> results = service.getResultsByTestId(testId);
        var baos = new ByteArrayOutputStream();
        var writer = new java.io.PrintWriter(baos);
        writer.println("candidateId,attemptId,totalQuestions,correctAnswers,scorePercent,evaluatedAt");
        for (var r : results) {
            writer.printf("%s,%s,%d,%d,%d,%s\n", r.getCandidateId(), r.getAttemptId(), r.getTotalQuestions(), r.getCorrectAnswers(), r.getScorePercent(), r.getEvaluatedAt());
        }
        writer.flush();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=results_"+testId+".csv").contentType(MediaType.parseMediaType("text/csv")).body(baos.toByteArray());
    }

*/
/*    // Export simple PDF report (one page) summarizing results
    @GetMapping(value = "/export/pdf/{testId}", produces = "application/pdf")
    public ResponseEntity<byte[]> exportPdf(@PathVariable String testId) throws Exception {
        List<Result> results = service.getResultsByTestId(testId);
        var baos = new ByteArrayOutputStream();
// create PDF using PDFBox
        try (org.apache.pdfbox.pdmodel.PDDocument doc = new org.apache.pdfbox.pdmodel.PDDocument()) {
            var page = new org.apache.pdfbox.pdmodel.PDPage();
            doc.addPage(page);
            try (var content = new org.apache.pdfbox.pdmodel.PDPageContentStream(doc, page)) {
                content.beginText();
                var font = org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD;
                content.setFont(font, 12);
                content.newLineAtOffset(50, 700);
                content.showText("Results summary for test: " + testId);
                content.newLineAtOffset(0, -20);
                content.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA, 10);
                for (var r : results) {
                    content.showText(String.format("Candidate: %s, Score: %d%%, Correct: %d/%d", r.getCandidateId(), r.getScorePercent(), r.getCorrectAnswers(), r.getTotalQuestions()));
                    content.newLineAtOffset(0, -15);
                }
                content.endText();
            }
            doc.save(baos);
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=results_"+testId+".pdf").contentType(MediaType.APPLICATION_PDF).body(baos.toByteArray());
    }*//*

}*/

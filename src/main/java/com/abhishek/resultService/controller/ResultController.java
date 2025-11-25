package com.abhishek.resultService.controller;

import com.abhishek.resultService.dto.ApiResponse;
import com.abhishek.resultService.dto.AttemptDTO;
import com.abhishek.resultService.model.Result;
import com.abhishek.resultService.service.ResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/results")
@RequiredArgsConstructor
public class ResultController {
    private final ResultService resultService;

    @PostMapping("/evaluate")
    public ResponseEntity<ApiResponse<Result>> evaluateAttempt(
            @RequestBody AttemptDTO attempt,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        log.info("POST /results/evaluate - Attempt: {}, Candidate: {}",
                attempt.getId(), attempt.getCandidateId());

        String bearerToken = extractBearerToken(authorizationHeader);
        Result result = resultService.evaluateAttempt(attempt, bearerToken);

        log.debug("Evaluation completed for attempt: {}, Score: {}",
                attempt.getId(), result != null ? result.getScore() : "N/A");

        return ResponseEntity.ok(ApiResponse.<Result>builder()
                .message("Attempt evaluated successfully")
                .data(result)
                .build());
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<ApiResponse<List<Result>>> getCandidateHistory(@PathVariable String candidateId) {
        log.info("GET /results/candidate/{} - Fetching candidate history", candidateId);

        List<Result> results = resultService.getHistory(candidateId);

        log.debug("Returning {} results for candidate: {}", results.size(), candidateId);
        return ResponseEntity.ok(ApiResponse.<List<Result>>builder()
                .message("Candidate history fetched successfully")
                .data(results)
                .build());
    }

    @GetMapping("/test/{testId}")
    public ResponseEntity<ApiResponse<List<Result>>> getTestResults(@PathVariable String testId) {
        log.info("GET /results/test/{} - Fetching test results", testId);

        List<Result> results = resultService.getTestResults(testId);

        log.debug("Returning {} results for test: {}", results.size(), testId);
        return ResponseEntity.ok(ApiResponse.<List<Result>>builder()
                .message("Test results fetched successfully")
                .data(results)
                .build());
    }

    @GetMapping("/export")
    public ResponseEntity<String> exportResultsAsCsv(@RequestParam String testId) {
        log.info("GET /results/export - Exporting results for test: {}", testId);

        String csvContent = resultService.exportResultsAsCsv(testId);

        log.debug("CSV export completed for test: {}", testId);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=results_" + testId + ".csv")
                .body(csvContent);
    }

    private String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader == null) {
            return null;
        }
        if (authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return authorizationHeader;
    }
}

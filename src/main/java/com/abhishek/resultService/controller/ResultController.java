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

import static com.abhishek.resultService.constant.Constants.*;

@Slf4j
@RestController
@RequestMapping(ENDPOINT_RESULTS)
@RequiredArgsConstructor
public class ResultController {
    private final ResultService resultService;

    @PostMapping(ENDPOINT_EVALUATE)
    public ResponseEntity<ApiResponse<Result>> evaluateAttempt(
            @RequestBody AttemptDTO attempt,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        log.info("POST {}{} - Attempt: {}, Candidate: {}",
                ENDPOINT_RESULTS, ENDPOINT_EVALUATE, attempt.getId(), attempt.getCandidateId());

        String bearerToken = extractBearerToken(authorizationHeader);
        Result result = resultService.evaluateAttempt(attempt, bearerToken);

        log.debug("Evaluation completed - Score: {}", result != null ? result.getScore() : "N/A");

        return ResponseEntity.ok(ApiResponse.<Result>builder()
                .message(MSG_ATTEMPT_EVALUATED)
                .data(result)
                .build());
    }

    @GetMapping(ENDPOINT_CANDIDATE + "/{candidateId}")
    public ResponseEntity<ApiResponse<List<Result>>> getCandidateHistory(@PathVariable String candidateId) {
        log.info("GET {}{}/{} - Fetching candidate history",
                ENDPOINT_RESULTS, ENDPOINT_CANDIDATE, candidateId);

        List<Result> results = resultService.getHistory(candidateId);

        log.debug("Returning {} results", results.size());
        return ResponseEntity.ok(ApiResponse.<List<Result>>builder()
                .message(MSG_HISTORY_FETCHED)
                .data(results)
                .build());
    }

    @GetMapping(ENDPOINT_TEST + "/{testId}")
    public ResponseEntity<ApiResponse<List<Result>>> getTestResults(@PathVariable String testId) {
        log.info("GET {}{}/{} - Fetching test results",
                ENDPOINT_RESULTS, ENDPOINT_TEST, testId);

        List<Result> results = resultService.getTestResults(testId);

        log.debug("Returning {} results", results.size());
        return ResponseEntity.ok(ApiResponse.<List<Result>>builder()
                .message(MSG_RESULTS_FETCHED)
                .data(results)
                .build());
    }

    @GetMapping(ENDPOINT_EXPORT)
    public ResponseEntity<String> exportResultsAsCsv(@RequestParam String testId) {
        log.info("GET {}{} - Exporting results for test: {}",
                ENDPOINT_RESULTS, ENDPOINT_EXPORT, testId);

        String csvContent = resultService.exportResultsAsCsv(testId);

        log.debug("CSV export completed");
        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "attachment; filename=" + CSV_FILENAME_PREFIX + testId + CSV_FILENAME_SUFFIX)
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

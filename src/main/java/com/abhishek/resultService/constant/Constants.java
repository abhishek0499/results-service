package com.abhishek.resultService.constant;

/**
 * Centralized constants for the Results Service
 */
public final class Constants {

    private Constants() {
        // Prevent instantiation
    }

    public static final String MSG_ATTEMPT_EVALUATED = "Attempt evaluated successfully";
    public static final String MSG_HISTORY_FETCHED = "Candidate history fetched successfully";
    public static final String MSG_RESULTS_FETCHED = "Test results fetched successfully";
    public static final String MSG_CSV_EXPORTED = "Results exported successfully";

    public static final String ERROR_VALIDATION_FAILED = "Validation failed";
    public static final String ERROR_RESULT_NOT_FOUND = "Result not found";
    public static final String ERROR_QUESTIONS_NOT_FOUND = "Questions not found for evaluation";

    public static final String ENDPOINT_RESULTS = "/results";
    public static final String ENDPOINT_EVALUATE = "/evaluate";
    public static final String ENDPOINT_CANDIDATE = "/candidate";
    public static final String ENDPOINT_TEST = "/test";
    public static final String ENDPOINT_EXPORT = "/export";

    public static final String CSV_HEADER = "CandidateID,CandidateName,Score,CorrectAnswers,TotalQuestions,EvaluatedAt\n";
    public static final String CSV_FILENAME_PREFIX = "results_";
    public static final String CSV_FILENAME_SUFFIX = ".csv";
}

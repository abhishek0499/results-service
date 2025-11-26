package com.abhishek.resultService.exception;

/**
 * Custom exception for result not found errors
 */
public class ResultNotFoundException extends RuntimeException {

    private final String resultId;

    public ResultNotFoundException(String resultId) {
        super(String.format("Result not found with ID: %s", resultId));
        this.resultId = resultId;
    }

    public String getResultId() {
        return resultId;
    }
}

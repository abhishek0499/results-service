package com.abhishek.resultService.exception;

import com.abhishek.resultService.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import static com.abhishek.resultService.constant.Constants.*;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle validation errors from @Valid annotations
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
            MethodArgumentNotValidException exception) {

        log.debug("Validation exception details:", exception);

        Map<String, String> validationErrors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
            log.debug("Validation error - Field: {}, Message: {}", fieldName, errorMessage);
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Map<String, String>>builder()
                        .message(ERROR_VALIDATION_FAILED)
                        .data(validationErrors)
                        .build());
    }

    /**
     * Handle ResultNotFoundException
     */
    @ExceptionHandler(ResultNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResultNotFound(ResultNotFoundException exception) {
        log.error("Result not found - ID: {}", exception.getResultId());
        log.debug("ResultNotFoundException details:", exception);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.<Void>builder()
                        .message(exception.getMessage())
                        .build());
    }

    /**
     * Handle QuestionsNotFoundException
     */
    @ExceptionHandler(QuestionsNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleQuestionsNotFound(QuestionsNotFoundException exception) {
        log.error("Questions not found for evaluation - IDs: {}", exception.getQuestionIds());
        log.debug("QuestionsNotFoundException details:", exception);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.<Void>builder()
                        .message(exception.getMessage())
                        .build());
    }

    /**
     * Handle IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException exception) {
        log.error("Illegal argument: {}", exception.getMessage());
        log.debug("IllegalArgumentException stack trace:", exception);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Void>builder()
                        .message(exception.getMessage())
                        .build());
    }

    /**
     * Handle all other uncaught exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(Exception exception) {
        log.error("Unhandled exception occurred: {}", exception.getMessage());
        log.error("Exception stack trace:", exception);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.<Void>builder()
                        .message("An unexpected error occurred. Please try again later.")
                        .build());
    }
}

package com.abhishek.resultService.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "results")
@Data
public class Result {
    @Id
    private String id;
    private String attemptId;
    private String candidateId;
    private String candidateName;
    private String testId;
    private int score;
    private int correctCount;
    private int totalQuestions;
    private Map<String, String> detailedAnswers; // questionId -> selectedOptionId
    private LocalDateTime evaluatedAt;
}
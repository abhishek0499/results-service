package com.abhishek.resultService.dto.event;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResultPublishedEvent implements Serializable {
    private String eventType = "RESULT_PUBLISHED";
    private String candidateId;
    private String candidateName;
    private String candidateEmail;
    private String testId;
    private String testName;
    private Integer score;
    private Integer totalQuestions;
    private Double percentage;
}

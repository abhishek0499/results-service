package com.abhishek.resultService.exception;

import java.util.List;

/**
 * Custom exception for questions not found during evaluation
 */
public class QuestionsNotFoundException extends RuntimeException {

    private final List<String> questionIds;

    public QuestionsNotFoundException(List<String> questionIds) {
        super(String.format("Questions not found for evaluation. Question IDs: %s", questionIds));
        this.questionIds = questionIds;
    }

    public List<String> getQuestionIds() {
        return questionIds;
    }
}

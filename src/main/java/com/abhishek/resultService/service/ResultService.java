package com.abhishek.resultService.service;

import com.abhishek.resultService.client.AdminClient;
import com.abhishek.resultService.dto.AnswerDTO;
import com.abhishek.resultService.dto.AttemptDTO;
import com.abhishek.resultService.dto.QuestionDTO;
import com.abhishek.resultService.dto.QuestionSnapshotDTO;
import com.abhishek.resultService.model.Result;
import com.abhishek.resultService.repository.ResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResultService {
    private final ResultRepository resultRepository;
    private final AdminClient adminClient;

    public Result evaluateAttempt(AttemptDTO attempt, String bearerToken) {
        log.info("Evaluating attempt: {} for candidate: {}, test: {}",
                attempt.getId(), attempt.getCandidateId(), attempt.getTestId());

        if (attempt.getQuestions() == null || attempt.getQuestions().isEmpty()) {
            log.warn("No questions found in attempt: {}", attempt.getId());
            return null;
        }

        List<String> questionIds = attempt.getQuestions().stream()
                .map(QuestionSnapshotDTO::getQuestionId)
                .collect(Collectors.toList());

        log.debug("Fetching {} questions from admin service for attempt: {}",
                questionIds.size(), attempt.getId());

        List<QuestionDTO> questions = adminClient.fetchQuestionsBulk(questionIds, bearerToken);
        if (questions.isEmpty()) {
            log.error("Failed to fetch questions for attempt: {}, questionIds: {}",
                    attempt.getId(), questionIds);
            return null;
        }

        log.debug("Successfully fetched {} questions for evaluation", questions.size());

        Map<String, String> correctAnswersMap = questions.stream()
                .collect(Collectors.toMap(QuestionDTO::getId, QuestionDTO::getCorrectOptionId));

        int totalScore = 0;
        int correctAnswersCount = 0;
        Map<String, String> candidateAnswersMap = new HashMap<>();

        // Map candidate answers for easy lookup
        if (attempt.getAnswers() != null) {
            log.debug("Processing {} candidate answers for attempt: {}",
                    attempt.getAnswers().size(), attempt.getId());

            for (AnswerDTO answer : attempt.getAnswers()) {
                candidateAnswersMap.put(answer.getQuestionId(), answer.getOptionId());
            }
        } else {
            log.warn("No answers submitted for attempt: {}", attempt.getId());
        }

        // Evaluate each question
        for (String questionId : questionIds) {
            String correctOptionId = correctAnswersMap.get(questionId);
            String candidateOptionId = candidateAnswersMap.get(questionId);

            if (correctOptionId != null && correctOptionId.equals(candidateOptionId)) {
                totalScore++; // Assume 1 mark per question
                correctAnswersCount++;
                log.debug("Question {} answered correctly by candidate: {}",
                        questionId, attempt.getCandidateId());
            } else {
                log.debug("Question {} answered incorrectly. Correct: {}, Candidate: {}",
                        questionId, correctOptionId, candidateOptionId);
            }
        }

        log.info("Evaluation complete for attempt: {}. Score: {}/{}",
                attempt.getId(), totalScore, questionIds.size());

        Result result = new Result();
        result.setAttemptId(attempt.getId());
        result.setCandidateId(attempt.getCandidateId());
        result.setTestId(attempt.getTestId());
        result.setScore(totalScore);
        result.setCorrectCount(correctAnswersCount);
        result.setTotalQuestions(questionIds.size());
        result.setDetailedAnswers(candidateAnswersMap);
        result.setEvaluatedAt(LocalDateTime.now());

        Result savedResult = resultRepository.save(result);
        log.info("Result saved successfully: {} for attempt: {}, candidate: {}",
                savedResult.getId(), attempt.getId(), attempt.getCandidateId());

        return savedResult;
    }

    public List<Result> getHistory(String candidateId) {
        log.info("Fetching result history for candidate: {}", candidateId);

        List<Result> results = resultRepository.findByCandidateId(candidateId);
        log.debug("Found {} results for candidate: {}", results.size(), candidateId);

        return results;
    }

    public List<Result> getTestResults(String testId) {
        log.info("Fetching results for test: {}", testId);

        List<Result> results = resultRepository.findByTestId(testId);
        log.debug("Found {} results for test: {}", results.size(), testId);

        return results;
    }

    public String exportResultsAsCsv(String testId) {
        log.info("Exporting results as CSV for test: {}", testId);

        List<Result> results = resultRepository.findByTestId(testId);
        StringBuilder csvContent = new StringBuilder("CandidateID,Score,CorrectAnswers,TotalQuestions,EvaluatedAt\n");

        for (Result result : results) {
            csvContent.append(result.getCandidateId()).append(",")
                    .append(result.getScore()).append(",")
                    .append(result.getCorrectCount()).append(",")
                    .append(result.getTotalQuestions()).append(",")
                    .append(result.getEvaluatedAt()).append("\n");
        }

        log.info("Exported {} results for test: {}", results.size(), testId);
        return csvContent.toString();
    }
}

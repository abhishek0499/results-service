package com.abhishek.resultService.dto;

import lombok.Data;
import java.util.List;

@Data
public class AttemptDTO {
    private String id;
    private String testId;
    private String candidateId;
    private List<QuestionSnapshotDTO> questions;
    private List<AnswerDTO> answers;
}
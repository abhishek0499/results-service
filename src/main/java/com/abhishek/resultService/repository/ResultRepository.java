package com.abhishek.resultService.repository;

import com.abhishek.resultService.model.Result;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ResultRepository extends MongoRepository<Result, String> {
    List<Result> findByCandidateId(String candidateId);

    List<Result> findByTestId(String testId);
}
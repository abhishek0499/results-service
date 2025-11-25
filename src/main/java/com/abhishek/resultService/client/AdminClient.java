package com.abhishek.resultService.client;

import com.abhishek.resultService.dto.ApiResponse;
import com.abhishek.resultService.dto.QuestionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminClient {
    private final RestClient restClient;
    private final String ADMIN_SERVICE_URL = "http://localhost:8082/admin";

    public List<QuestionDTO> fetchQuestionsBulk(List<String> questionIds, String bearerToken) {
        log.debug("Fetching {} questions in bulk from admin service", questionIds.size());

        try {
            var request = restClient.post().uri(ADMIN_SERVICE_URL + "/questions/bulk");

            if (bearerToken != null) {
                request = request.headers(headers -> headers.setBearerAuth(bearerToken));
            }

            var response = request.body(questionIds).retrieve()
                    .body(new ParameterizedTypeReference<ApiResponse<QuestionDTO[]>>() {
                    });

            if (response != null && response.getData() != null) {
                log.debug("Successfully fetched {} questions from admin service",
                        response.getData().length);
                return Arrays.asList(response.getData());
            }

            log.warn("No questions returned from admin service");
            return List.of();
        } catch (RestClientException exception) {
            log.error("Failed to fetch questions from admin service", exception);
            return List.of();
        }
    }
}
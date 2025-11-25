package com.abhishek.resultService.service.publisher;

import com.abhishek.resultService.dto.event.ResultPublishedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationPublisher {

    private final JmsTemplate jmsTemplate;

    private static final String QUEUE_RESULT_PUBLISHED = "result.published";

    public void publishResultPublishedEvent(ResultPublishedEvent event) {
        try {
            log.info("Publishing RESULT_PUBLISHED event for candidate: {}", event.getCandidateName());
            jmsTemplate.convertAndSend(QUEUE_RESULT_PUBLISHED, event);
            log.debug("Event published successfully: {}", event);
        } catch (Exception e) {
            log.error("Failed to publish RESULT_PUBLISHED event", e);
        }
    }
}

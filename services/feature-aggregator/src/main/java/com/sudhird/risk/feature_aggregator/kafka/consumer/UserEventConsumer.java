package com.sudhird.risk.feature_aggregator.kafka.consumer;

import com.sudhird.risk.feature_aggregator.model.UserEvent;
import com.sudhird.risk.feature_aggregator.service.FeatureService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class UserEventConsumer {

    private final FeatureService featureService;

    public UserEventConsumer(FeatureService featureService) {
        this.featureService = featureService;
    }

    @KafkaListener(topics = "user-events", containerFactory = "kafkaListenerContainerFactory")
    public void consume(UserEvent event, Acknowledgment ack) {

        featureService.process(event);
        ack.acknowledge(); // at-least-once
    }
}

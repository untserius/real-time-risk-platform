package com.sudhird.risk.feature_aggregator.kafka;

import com.sudhird.risk.feature_aggregator.model.RiskFeatureEvent;
import com.sudhird.risk.feature_aggregator.model.UserEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FeatureProducer {

    private static final String TOPIC = "risk-features";
    private final KafkaTemplate<String, RiskFeatureEvent> kafkaTemplate;

    public FeatureProducer(KafkaTemplate<String, RiskFeatureEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(UserEvent event, Map<String, Object> features) {

        RiskFeatureEvent enriched = new RiskFeatureEvent();
        enriched.setEventId(event.getEventId());
        enriched.setUserId(event.getUserId());
        enriched.setFeatures(features);

        kafkaTemplate.send(TOPIC, event.getUserId(), enriched);
    }
}

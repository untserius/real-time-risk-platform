package com.sudhird.risk.feature_aggregator.kafka.producer;

import com.sudhird.risk.feature_aggregator.model.RiskDecision;
import com.sudhird.risk.feature_aggregator.model.RiskDecisionEvent;
import com.sudhird.risk.feature_aggregator.model.UserEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DecisionProducer {

    private static final String TOPIC = "risk-decisions";

    private final KafkaTemplate<String, RiskDecisionEvent> kafkaTemplate;

    public DecisionProducer(KafkaTemplate<String, RiskDecisionEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(UserEvent event, RiskDecision decision) {

        RiskDecisionEvent out = new RiskDecisionEvent();
        out.setEventId(event.getEventId());
        out.setUserId(event.getUserId());
        out.setDecision(decision.getDecision());
        out.setScore(decision.getScore());

        kafkaTemplate.send(TOPIC, event.getUserId(), out);
    }
}

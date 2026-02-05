package com.sudhird.risk.websocket_alerts.consumer;

import com.sudhird.risk.websocket_alerts.handler.AlertWebSocketHandler;
import com.sudhird.risk.websocket_alerts.model.RiskDecisionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class RiskDecisionConsumer {

    @Autowired
    private AlertWebSocketHandler handler;

    private final ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(topics = "risk-decisions", groupId = "websocket-alerts")
    public void consume(RiskDecisionEvent event) throws Exception {
        System.out.println("Consumed decision: " + event.getDecision());
        if (!"ALLOW".equals(event.getDecision())) {
            String s = mapper.writeValueAsString(event);
            handler.broadcast(s);
        }
    }
}

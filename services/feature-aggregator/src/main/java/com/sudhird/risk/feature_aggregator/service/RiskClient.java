package com.sudhird.risk.feature_aggregator.service;

import com.sudhird.risk.feature_aggregator.model.RiskDecision;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class RiskClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public RiskDecision score(String userId, Map<String, Object> features) {
        Map<String, Object> request = Map.of(
                "userId", userId,
                "features", features
        );

        return restTemplate.postForObject("http://localhost:5000/score", request, RiskDecision.class);
    }
}

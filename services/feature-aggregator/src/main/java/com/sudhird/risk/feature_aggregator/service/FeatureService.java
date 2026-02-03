package com.sudhird.risk.feature_aggregator.service;

import com.sudhird.risk.feature_aggregator.kafka.producer.DecisionProducer;
import com.sudhird.risk.feature_aggregator.kafka.producer.FeatureProducer;
import com.sudhird.risk.feature_aggregator.model.RiskDecision;
import com.sudhird.risk.feature_aggregator.model.UserEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
public class FeatureService {

    private final RedisTemplate<String, String> redisTemplate;
    private final FeatureProducer producer;

    @Autowired
    private DecisionProducer decisionProducer;

    @Autowired
    private RiskClient riskClient;

    public FeatureService(RedisTemplate<String, String> redisTemplate,
                          FeatureProducer producer) {
        this.redisTemplate = redisTemplate;
        this.producer = producer;
    }

    public void process(UserEvent event) {

        String userId = event.getUserId();

        int login5m = increment("login:5m:" + userId, 300);
        int login1h = increment("login:1h:" + userId, 3600);

        Map<String, Object> features = Map.of(
                "login_5m", login5m,
                "login_1h", login1h
        );

        RiskDecision decision = riskClient.score(userId, features);

        producer.publish(event, features);
        decisionProducer.publish(event, decision);
    }

    private int increment(String key, int ttlSeconds) {
        Long count = redisTemplate.opsForValue().increment(key);

        if (count != null && count == 1) {
            redisTemplate.expire(key, Duration.ofSeconds(ttlSeconds));
        }
        return count == null ? 0 : count.intValue();
    }
}


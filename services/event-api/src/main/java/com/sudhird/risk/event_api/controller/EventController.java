package com.sudhird.risk.event_api.controller;

import com.sudhird.risk.event_api.kafka.EventProducer;
import com.sudhird.risk.event_api.model.UserEvent;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventProducer producer;

    public EventController(EventProducer producer) {
        this.producer = producer;
    }

    @PostMapping
    public ResponseEntity<Void> publish(@Valid @RequestBody UserEvent event) {
        producer.send(event);
        return ResponseEntity.accepted().build();
    }
}

# Real-Time Fraud Detection & Risk Scoring Platform

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Python](https://img.shields.io/badge/Python-3.11-blue.svg)](https://www.python.org/)

>Production-grade event-driven system for real-time fraud detection using streaming data pipelines and ML inference.

## Overview

Event-driven fraud detection platform built with Kafka, Redis, and distributed microservices architecture. Processes user activity events in real-time, computes behavioral features through sliding-window aggregation, scores risk via ML inference, and delivers instant alerts via WebSocket.

## Capabilities

- Real-time event ingestion via HTTP
- Kafka-based streaming pipeline for event processing
- Redis-backed sliding-window feature aggregation
- ML inference service for risk scoring
- Asynchronous decision publishing to multiple consumers
- Live WebSocket alerts for risky activity monitoring

## Architecture

### Backend
- Java 17
- Spring Boot
- Apache Kafka
- Redis
- PostgreSQL

### ML / Scoring
- Python 3.11
- FastAPI
- Scikit-learn

### Real-Time Delivery
- WebSockets

### Infrastructure
- Docker & Docker Compose

## Services

### 1. Event API
Port: `8080`

- Accepts user activity events over HTTP
- Publishes events to Kafka
- Non-blocking (202 Accepted)

Endpoint:
```bash
POST /api/events
Content-Type: application/json
```

### 2. Feature Aggregator
Port: `8081`

- Consumes user events from Kafka
- Maintains real-time counters using Redis
- Calls ML service for scoring
- Publishes final decisions to Kafka

### 3. Risk Engine
Port: `5000`

- Lightweight ML inference service
- Stateless REST API
- Returns risk score and decision (`ALLOW` / `CHALLENGE` / `BLOCK`)

Response format:
```json
{
  "userId": "u204",
  "riskScore": 0.87,
  "decision": "BLOCK"
}
```

### 4. WebSocket Alerts
Port: `8082`

- Consumes risk-decisions from Kafka
- Pushes live alerts for risky decisions
- Powers real-time dashboard

WebSocket endpoint:
```
ws://localhost:8082/alerts
```

### 5. Live Dashboard

- HTML + JavaScript interface
- Connects via WebSocket
- Displays `BLOCK` and `CHALLENGE` alerts in real-time

## Getting Started

Prerequisites: Docker, Docker Compose

**Start all services:**
```bash
docker-compose up -d
```

**Verify services:**
```bash
docker-compose ps
```

**Access dashboard:**
Open `dashboard.html` in your browser

Service endpoints:
- Event API: http://localhost:8080
- Feature Aggregator: http://localhost:8081
- WebSocket Alerts: http://localhost:8082
- Risk Engine: http://localhost:5000

## Usage

Send event:
```bash
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -d '{
    "eventId": "e203",
    "userId": "u204",
    "eventType": "LOGIN",
    "timestamp": 1710000203
  }'
```

Response:
```json
{
  "status": "accepted",
  "eventId": "e203"
}
```

High-risk events trigger real-time alerts on the dashboard showing user ID, event type, risk score, and decision.

## Development

Run services individually:

```bash
# Event API
cd event-api && ./mvnw spring-boot:run

# Feature Aggregator
cd feature-aggregator && ./mvnw spring-boot:run

# Risk Engine
cd risk-engine && pip install -r requirements.txt && uvicorn main:app --port 5000

# WebSocket Alerts
cd websocket-alerts && ./mvnw spring-boot:run
```

Run tests:
```bash
./mvnw test  # Java services
pytest       # Python service
```

## System Flow

```
User Activity Event
        ↓
   [Event API] → Kafka (user-events topic)
        ↓
[Feature Aggregator]
        ├─→ Redis (real-time features)
        ├─→ [Risk Engine] (ML inference)
        └─→ Kafka (risk-decisions topic)
                ↓
        [WebSocket Alerts]
                ↓
        [Live Dashboard]
```

## Event Types

- `LOGIN` - User login attempt
- `TRANSACTION` - Financial transaction
- `ACCOUNT_UPDATE` - Account modification
- `PASSWORD_RESET` - Password change request
- `API_CALL` - API access event

## Risk Decisions

| Decision | Risk Score | Action |
|----------|-----------|--------|
| `ALLOW` | 0.0 - 0.3 | Normal processing |
| `CHALLENGE` | 0.3 - 0.7 | Additional verification |
| `BLOCK` | 0.7 - 1.0 | Transaction blocked |

## Monitoring

Kafka topics:
- `user-events` - Raw incoming events
- `risk-decisions` - Scored events with decisions

Redis keys:
- `user:{userId}:events:{eventType}:1h` - Sliding window counters
- `user:{userId}:total:1h` - Total event count

Logs:
```bash
docker-compose logs -f              # All services
docker-compose logs -f event-api    # Specific service
```

## License

MIT
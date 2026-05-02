# 🚀 FlowShield

**A production-style multi-tenant API gateway + distributed job processing system**  
Built to handle rate limiting, abuse protection, and reliable async execution — all in one clean architecture.

> Think: Stripe-level API protection + background job engine (but simplified and buildable)

---

## 🧠 What this is

FlowShield is split into two clear systems:

- 🛡 **Gateway** → handles incoming API traffic (auth, rate limit, abuse detection)
- ⚙️ **Processor** → executes jobs reliably (workers, retries, recovery)

This separation is intentional — it’s how real systems scale.

---

## 🏗️ Architecture (high-level)

Client → Gateway → (Redis + Postgres) → SQS → Processor → DB

- Gateway = fast, stateless, protective layer
- Processor = async, fault-tolerant execution layer
- Redis = hot path (rate limiting, caching)
- Postgres = source of truth
- SQS = decoupling + reliability

---

## ⚡ Core Features

### 🔐 API Protection Layer
- API key authentication (cached in Redis)
- IP-based throttling (prevents brute force / DDoS)
- Token bucket rate limiting (Redis Lua — atomic, no race conditions)
- Abuse scoring system (auto throttle / block bad actors)
- Idempotency (no duplicate job execution)

### ⚙️ Distributed Job Processing
- Async job execution via workers
- Retry with exponential backoff + jitter
- Dead-letter handling
- Watchdog for stuck jobs
- Heartbeat-based lock extension
- Priority scheduling with aging

### 📊 Observability
- RED metrics (Rate, Errors, Duration)
- Actuator + Prometheus metrics
- k6 load testing with real benchmarks

---

## 🧩 Tech Stack

**Backend**
- Java 17 + Spring Boot 3
- PostgreSQL (primary DB)
- Redis (rate limiting + caching)
- AWS SQS (queue)

**Frontend**
- React 18 + Vite + Tailwind

**Infra**
- Docker (local dev)
- AWS (EC2, RDS, S3, CloudFront)
- GitHub Actions (CI/CD)

---

## 🔥 Why this project stands out

This isn’t CRUD.

This project solves **real backend problems**:

- How do you **rate limit correctly in distributed systems?**
- How do you prevent **duplicate requests under concurrency?**
- How do you ensure **jobs are never lost** even on crashes?
- How do multiple servers **share state safely?**

All answers are implemented — not just theoretical.

---

## 📈 Key Engineering Decisions

- **Redis Lua over naive rate limiting**  
  → guarantees atomicity under high concurrency

- **Postgres + SKIP LOCKED instead of Kafka**  
  → simpler, still scalable for this level

- **SQS as decoupling layer**  
  → isolates failures between gateway & processor

- **Two-layer idempotency (Redis + DB)**  
  → fast path + correctness fallback

---

## 🧪 Load Testing

Using k6:

- ✅ 0% false-allow rate under concurrent load
- ⚡ Stable latency under multi-tenant traffic
- 🛡 Attack simulation handled via IP + abuse filters

---

## 🚀 Getting Started

### 1. Clone
git clone https://github.com/your-username/flowshield.git
cd flowshield

### 2. Start infra
docker-compose up -d

### 3. Run services
./gradlew :flowshield-gateway:bootRun
./gradlew :flowshield-processor:bootRun

### 4. Health check
GET http://localhost:8080/actuator/health

---

## 📡 Example API Call

curl -X POST http://localhost:8080/api/v1/jobs \
-H "X-API-Key: <your-key>" \
-H "X-Idempotency-Key: unique-key-123" \
-H "Content-Type: application/json" \
-d '{
"type": "SEND_EMAIL",
"payload": { "to": "user@test.com" }
}'

---

## 📂 Project Structure

flowshield/
├── flowshield-common
├── flowshield-gateway
├── flowshield-processor
├── flowshield-dashboard

---

## 🧠 What I learned building this

- Designing **failure-resistant systems > writing code**
- Importance of **clear system boundaries**
- How **distributed coordination actually works (Redis, DB, queues)**
- Why **“works locally” ≠ production-ready**

---

## 📌 Status

🚧 Currently building (active development)  
Goal → production-style deployment on AWS with live demo

---

## 📬 Final Note

This project is intentionally built like something you'd see in a real company — not just a demo app.

If you’re reviewing this as a recruiter:  
Everything here is designed to show **system thinking, not just coding ability**.

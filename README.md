# URL Shortener + Analytics

Interview-ready Java/Spring Boot URL shortener demonstrating REST APIs, PostgreSQL persistence, Redis cache-aside, redirects, analytics and Docker.

## Features
- Generate unique 7-character short codes
- HTTP redirect from short URL to original URL
- Redis cache-aside lookup with 24h TTL
- PostgreSQL as source of truth
- Recent public URLs on the homepage
- Click counter and analytics endpoint
- Validation and error handling
- Docker Compose for local one-command startup

## Run
```bash
docker compose up --build
```
Open http://localhost:8080

## API
- `POST /api/urls` body: `{ "url": "https://example.com" }`
- `GET /{code}` redirect
- `GET /api/urls/recent`
- `GET /api/urls/{code}/analytics`
- `GET /actuator/health`

## Architecture
Browser → Spring Boot REST API → Redis (fast lookup) → PostgreSQL (durable source of truth)

The project intentionally keeps the public demo simple while providing enough backend depth to discuss cache-aside, persistence, collision handling, redirects, consistency and scaling.
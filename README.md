# Weather API

A Spring Boot REST API that provides current weather conditions and multi-day forecasts for any location, specified as either a free-text address or geographic coordinates.

---

## Running Locally

### Prerequisites

| Requirement | Version            |
|-------------|--------------------|
| Java | 25                 |
| Gradle | (wrapper included) |

### External API Keys

The application requires a **Geoapify** API key for address-to-coordinates geocoding.

1. Register for a free key at [https://www.geoapify.com](https://www.geoapify.com)
2. Set the key as an environment variable before starting:

```bash
export GEOAPIFY_API_KEY=your_api_key_here
```

> The `application.properties` contains `weather.api.geoapify.api-key=${GEOAPIFY_API_KEY}`. The Open-Meteo API requires no key.

### Starting the Application

```bash
./gradlew bootRun
```

The application starts on port **8080** by default.

### Swagger UI

Once running, the interactive API documentation is available at:

```
http://localhost:8080/swagger-ui.html
```

---

## Running Tests

### Unit Tests

```bash
./gradlew test
```

### Integration Tests

Integration tests use WireMock to stub external API calls. No live API key is required.

```bash
./gradlew integrationTest
```

### All Tests

```bash
./gradlew check
```

---

## REST API

### `GET /weather`

Returns current weather conditions and a multi-day forecast for a given location.

**You must provide either `address` OR both `lat` and `lon`.**

#### Query Parameters

| Parameter | Type    | Required | Default | Description |
|-----------|---------|----------|---------|-------------|
| `address` | string  | conditional | — | Free-text address (e.g. `Bucharest, Romania`) |
| `lat`     | float   | conditional | — | Latitude (-90 to 90) |
| `lon`     | float   | conditional | — | Longitude (-180 to 180) |
| `days`    | integer | no       | `5`     | Number of forecast days (1–10) |

#### Example — by address

```bash
curl "http://localhost:8080/weather?address=Bucharest&days=3"
```

#### Example — by coordinates

```bash
curl "http://localhost:8080/weather?lat=52.52&lon=13.41&days=5"
```

#### Response `200 OK`

```json
{
  "location": {
    "name": "Bucharest, Romania",
    "lat": 44.43,
    "lon": 26.1
  },
  "current": {
    "temperature": 22.5,
    "humidity": 55.0,
    "wind_speed": 4.2,
    "condition": "Clear sky",
    "timestamp": "2026-05-01T16:00:00Z"
  },
  "forecast": [
    {
      "date": "2026-05-01",
      "min_temp": 15.0,
      "max_temp": 26.0,
      "condition": "Clear sky",
      "precipitation_chance": 5.0
    }
  ]
}
```

#### Response `400 Bad Request`

Returned when neither an address nor coordinates are provided, or when parameter values are invalid.

```json
{
  "error": "Bad Request",
  "message": "You must provide either an address or both lat and lon."
}
```

#### Response `502 Bad Gateway`

Returned when an upstream provider (Geoapify or Open-Meteo) is unavailable.

```json
{
  "error": "External Dependency Error",
  "message": "Provider failure"
}
```

---

## Cache Configuration

Cache settings are externalized in `application.properties`:

| Property | Default | Description |
|----------|---------|-------------|
| `weather.cache.weather.limit` | `1000` | Max entries in the weather cache |
| `weather.cache.geo.limit` | `1000` | Max entries in the geocoding cache |
| `weather.cache.expire.duration` | `10m` | TTL for all cache entries |

# Stock Market News API

A Spring Boot API for stock market news and intraday trading recommendations.

## Quick Start

### Windows
```bash
# Start the API
scripts\dev-start.bat

# Start in debug mode (port 5005)
scripts\dev-debug.bat

# Test endpoints
scripts\dev-test.bat

# Run integration tests
scripts\integration-test.bat

# Clean up processes
scripts\dev-clean.bat
```

### Linux/Mac
```bash
# Start the API
./scripts/dev-start.sh

# Start in debug mode (port 5005)
./scripts/dev-debug.sh

# Test endpoints
./scripts/dev-test.sh

# Run integration tests
./scripts/integration-test.sh

# Clean up processes
./scripts/dev-clean.sh
```

## API Endpoints

- `GET /api/v1/news` - Get stock market news
- `GET /api/v1/recommendations` - Get intraday trading recommendations
- `GET /actuator/health` - Health check

## Development

- **Port**: 8080
- **Debug Port**: 5005
- **Mock Data**: All endpoints return mock data for development
- **Logging**: Debug level enabled for com.stockmarket package
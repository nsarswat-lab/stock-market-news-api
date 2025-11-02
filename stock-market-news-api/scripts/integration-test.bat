@echo off
echo 🧪 Running Stock Market News API Integration Tests...
set BASE_URL=http://localhost:8080

echo.
echo ✅ Testing Health Endpoint:
curl -s "%BASE_URL%/actuator/health"
echo.

echo.
echo ✅ Testing Stock News Endpoint:
curl -s "%BASE_URL%/api/v1/news" | findstr "BACKEND_MOCK"
if %errorlevel% == 0 (
    echo ✅ News endpoint working with mock data
) else (
    echo ❌ News endpoint test failed
)

echo.
echo ✅ Testing Recommendations Endpoint:
curl -s "%BASE_URL%/api/v1/recommendations" | findstr "BACKEND_MOCK"
if %errorlevel% == 0 (
    echo ✅ Recommendations endpoint working with mock data
) else (
    echo ❌ Recommendations endpoint test failed
)

echo.
echo 🎯 Integration test completed!
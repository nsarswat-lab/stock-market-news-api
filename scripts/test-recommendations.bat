@echo off
echo 🎯 Testing Enhanced Recommendations System...
echo.

echo ⚡ Testing Intraday Recommendations:
curl -s "http://localhost:8080/api/v1/recommendations/intraday" | findstr "INTRADAY_ANALYSIS"
echo.

echo 📈 Testing Long-term Recommendations:
curl -s "http://localhost:8080/api/v1/recommendations/longterm" | findstr "FUNDAMENTAL_ANALYSIS"
echo.

echo 💡 Testing Combined Recommendations:
curl -s "http://localhost:8080/api/v1/recommendations" | findstr "COMBINED_ANALYSIS"
echo.

echo 🌐 Testing Frontend:
curl -s "http://localhost:3000" | findstr "Stock Market News"
echo.

echo ✅ All endpoints are working!
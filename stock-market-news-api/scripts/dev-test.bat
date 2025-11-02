@echo off
set BASE_URL=http://localhost:8080
echo 📈 Testing Stock Market News API endpoints...
echo.
echo Testing /api/v1/news:
curl -s "%BASE_URL%/api/v1/news"
echo.
echo.
echo Testing /api/v1/recommendations:
curl -s "%BASE_URL%/api/v1/recommendations"
echo.
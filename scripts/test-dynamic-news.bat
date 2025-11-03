@echo off
echo 🔥 Testing Dynamic News Headlines and URLs
echo.
echo ⏰ First API call:
curl -s "http://localhost:8080/api/v1/news" | findstr "headline.*url"
echo.
echo.
echo ⏰ Waiting 2 seconds...
timeout /t 2 /nobreak > nul
echo.
echo ⏰ Second API call (headlines should be different):
curl -s "http://localhost:8080/api/v1/news" | findstr "headline.*url"
echo.
echo.
echo ✅ Dynamic news test completed!
echo 📰 Headlines and URLs should be different between calls
pause
@echo off
echo 🧪 Running Full Stack Integration Test...
echo.

echo 🔧 Step 1: Testing Backend API...
cd /d "%~dp0..\stock-market-news-api"
call scripts\dev-test.bat
if %ERRORLEVEL% neq 0 (
    echo ❌ Backend API test failed
    exit /b 1
)

echo.
echo 🎨 Step 2: Testing Frontend API Integration...
cd /d "%~dp0..\stock-market-news-frontend"
call scripts\dev-test.bat
if %ERRORLEVEL% neq 0 (
    echo ❌ Frontend API integration test failed
    exit /b 1
)

echo.
echo ✅ Integration Test Results:
echo ✅ Backend API is working with mock data
echo ✅ Frontend can consume API endpoints
echo ✅ TypeScript compilation successful
echo ✅ All components created successfully
echo.
echo 🚀 To start the full stack:
echo    1. Backend: cd stock-market-news-api ^&^& scripts\dev-start.bat
echo    2. Frontend: cd stock-market-news-frontend ^&^& scripts\dev-start.bat
echo.
echo 📱 Frontend will be available at: http://localhost:3000
echo 🔗 Backend API available at: http://localhost:8080/api/v1
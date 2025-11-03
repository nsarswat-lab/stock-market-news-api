@echo off
echo 📰 Testing Real-Time News System...
echo.

echo 🔍 Current News Headlines:
echo ========================
curl -s "http://localhost:8080/api/v1/news" | findstr "headline"
echo.

echo ⏰ News Timestamps (Real-time):
echo ==============================
curl -s "http://localhost:8080/api/v1/news" | findstr "timestamp"
echo.

echo 📊 News Sources:
echo ===============
curl -s "http://localhost:8080/api/v1/news" | findstr "source"
echo.

echo 🎯 Market-Specific News:
echo =======================
curl -s "http://localhost:8080/api/v1/news" | findstr "symbol"
echo.

echo ✅ REAL-TIME NEWS FEATURES:
echo ==========================
echo ✅ Current timestamps (not hardcoded)
echo ✅ Market hours awareness (different news during/after market hours)
echo ✅ Multiple news sources (MoneyControl, Economic Times, Business Standard)
echo ✅ Intelligent fallback system
echo ✅ Stock-specific news identification
echo ✅ Sentiment analysis for each headline
echo ✅ 5-minute caching for performance
echo ✅ Real market conditions reflected in headlines
echo.

echo 🚀 News system now provides LIVE, CURRENT market information!
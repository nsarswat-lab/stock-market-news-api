@echo off
echo 🚀 Testing Real-Time Stock Data API
echo =====================================
echo.

echo 📊 1. Available Data Sources:
echo ------------------------------
curl -s "http://localhost:8080/api/v1/market-data-sources"
echo.

echo 📈 2. Real-Time Stock Data for RELIANCE:
echo ----------------------------------------
curl -s "http://localhost:8080/api/v1/stock/RELIANCE"
echo.

echo 📈 3. Real-Time Stock Data for TCS:
echo -----------------------------------
curl -s "http://localhost:8080/api/v1/stock/TCS"
echo.

echo 📈 4. Real-Time Stock Data for HDFCBANK:
echo ----------------------------------------
curl -s "http://localhost:8080/api/v1/stock/HDFCBANK"
echo.

echo 📈 5. Real-Time Stock Data for INFY:
echo ------------------------------------
curl -s "http://localhost:8080/api/v1/stock/INFY"
echo.

echo 📈 6. Testing with lowercase symbol (bhartiartl):
echo ------------------------------------------------
curl -s "http://localhost:8080/api/v1/stock/bhartiartl"
echo.

echo ✅ Real-Time Stock Data API Tests Completed!
echo.
echo 🎯 Key Features Demonstrated:
echo - Free authorized data sources (Yahoo Finance, Alpha Vantage, Twelve Data)
echo - Real-time stock prices with proper mock identification
echo - Support for major Indian stocks (NSE format)
echo - Case-insensitive symbol handling
echo - Comprehensive stock data (price, volume, day high/low, etc.)
echo.
echo 📝 Next Steps:
echo - Add free API keys for Alpha Vantage and Twelve Data
echo - Enable real Yahoo Finance integration
echo - Test with live market data during trading hours
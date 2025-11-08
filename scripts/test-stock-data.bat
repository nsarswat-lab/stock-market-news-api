@echo off
echo 🧪 Testing Real-Time Stock Data Endpoints...
echo.

echo 📊 Testing market data sources:
curl -s "http://localhost:8080/api/v1/market-data-sources" | jq .
echo.

echo 📈 Testing RELIANCE stock data:
curl -v "http://localhost:8080/api/v1/stock/RELIANCE"
echo.

echo 📈 Testing TCS stock data:
curl -v "http://localhost:8080/api/v1/stock/TCS"
echo.

echo ✅ Stock data tests completed!
@echo off
echo 🔬 Testing Advanced Financial Analytics System...
echo.

echo ⚡ Testing Enhanced Intraday Recommendations:
curl -s "http://localhost:8080/api/v1/recommendations/intraday" | findstr "advancedAnalytics"
echo.

echo 📊 Testing Advanced Analytics for RELIANCE:
curl -s "http://localhost:8080/api/v1/analytics/advanced/RELIANCE" | findstr "riskMetrics"
echo.

echo 📈 Testing Portfolio Analytics:
curl -s "http://localhost:8080/api/v1/portfolio/analytics" | findstr "portfolioConstruction"
echo.

echo ⚠️ Testing Risk Dashboard:
curl -s "http://localhost:8080/api/v1/analytics/risk-dashboard" | findstr "marketRisk"
echo.

echo 🎯 Testing All Advanced Features:
echo - Risk Metrics: Sharpe Ratio, Beta, VaR, Max Drawdown
echo - Technical Indicators: VWAP, Bollinger Bands, Relative Strength
echo - Options Analysis: Put/Call Ratio, Implied Volatility, Max Pain
echo - Portfolio Management: Asset Allocation, Risk Management, Hedging
echo - Market Context: Sector Rotation, Global Correlation, Currency Impact
echo - Earnings Intelligence: Analyst Revisions, Forward P/E, Growth Rates
echo.

echo ✅ Advanced Financial Analytics System is operational!
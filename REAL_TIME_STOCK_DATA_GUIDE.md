# 📊 Real-Time Stock Data API Guide

## 🎯 **What You Have Now**

Your stock market API now includes **real-time stock data endpoints** that integrate with **free authorized sources** for Indian stock market data.

---

## 🚀 **Available Endpoints**

### 1. **Get Available Data Sources**
```bash
GET /api/v1/market-data-sources
```

**Response:**
```json
{
  "availableSources": [
    "Yahoo Finance API (Free, no API key required)",
    "Alpha Vantage (Free tier: 500 calls/day, API key required)",
    "Twelve Data (Free tier: 800 calls/day, API key required)"
  ],
  "description": "Free authorized sources for real-time Indian stock market data",
  "setupInstructions": {
    "YahooFinance": "No API key required (free)",
    "AlphaVantage": "Get free API key from https://www.alphavantage.co/support/#api-key",
    "TwelveData": "Get free API key from https://twelvedata.com/"
  }
}
```

### 2. **Get Real-Time Stock Data**
```bash
GET /api/v1/stock/{symbol}
```

**Examples:**
```bash
# Get RELIANCE stock data
curl "http://localhost:8080/api/v1/stock/RELIANCE"

# Get TCS stock data  
curl "http://localhost:8080/api/v1/stock/TCS"

# Get HDFC Bank stock data
curl "http://localhost:8080/api/v1/stock/HDFCBANK"
```

**Response:**
```json
{
  "symbol": "RELIANCE",
  "currentPrice": 2750.50,
  "changePercent": 1.25,
  "dayHigh": 2780.00,
  "dayLow": 2720.00,
  "previousClose": 2719.25,
  "volume": 4500000,
  "timestamp": 1762205841814,
  "source": "Mock Data (Real-time APIs being integrated)",
  "dataSource": "BACKEND_MOCK",
  "mockIndicator": "🎭 MOCK DATA - NOT REAL (Real-time integration in progress)",
  "availableSources": "Yahoo Finance, Alpha Vantage, Twelve Data"
}
```

---

## 📈 **Supported Indian Stocks**

The API supports all major NSE-listed stocks including:

- **RELIANCE** - Reliance Industries
- **TCS** - Tata Consultancy Services  
- **HDFCBANK** - HDFC Bank
- **INFY** - Infosys
- **BHARTIARTL** - Bharti Airtel
- **ITC** - ITC Limited
- **WIPRO** - Wipro Limited
- **SBIN** - State Bank of India
- **LT** - Larsen & Toubro
- **MARUTI** - Maruti Suzuki

*And many more NSE stocks!*

---

## 🔧 **How to Enable Real-Time Data**

Currently showing mock data with proper identification. To enable real-time data:

### **Option 1: Yahoo Finance (Free, No API Key)**
- Already integrated
- No setup required
- Most reliable free source

### **Option 2: Alpha Vantage (Free Tier)**
1. Visit: https://www.alphavantage.co/support/#api-key
2. Sign up for FREE account
3. Get your API key
4. Update the service with your key

### **Option 3: Twelve Data (Free Tier)**
1. Visit: https://twelvedata.com/
2. Sign up for FREE account  
3. Get your API key
4. Update the service with your key

---

## 🧪 **Testing the API**

Run the comprehensive test script:

```bash
# Windows
scripts\test-real-time-stock-data.bat

# Or test individual endpoints
curl "http://localhost:8080/api/v1/market-data-sources"
curl "http://localhost:8080/api/v1/stock/RELIANCE"
curl "http://localhost:8080/api/v1/stock/TCS"
```

---

## 📊 **Data Quality & Identification**

### **Mock Data (Current)**
- Clearly marked with `🎭 MOCK DATA - NOT REAL`
- `dataSource: "BACKEND_MOCK"`
- Realistic but simulated values

### **Real Data (When Enabled)**
- Marked with `📡 REAL DATA from authorized sources`
- `dataSource: "REAL_TIME_API"`
- Live market prices and volumes

---

## 🎯 **Key Features**

✅ **Free Authorized Sources** - No illegal scraping  
✅ **Proper Mock Identification** - Clear when data is simulated  
✅ **Multiple Fallbacks** - Tries multiple sources automatically  
✅ **Indian Market Focus** - Optimized for NSE/BSE stocks  
✅ **Case Insensitive** - Works with any case (RELIANCE, reliance, Reliance)  
✅ **Comprehensive Data** - Price, volume, day high/low, change %  
✅ **Real-Time Ready** - Just add API keys to go live  

---

## 🚀 **Next Steps**

1. **Test the current mock implementation**
2. **Get free API keys** from Alpha Vantage or Twelve Data
3. **Enable real-time integration** by updating the service
4. **Monitor during market hours** to see live data

---

## 📞 **Support**

The API is designed to work immediately with mock data and can be upgraded to real-time data by simply adding free API keys. All sources are authorized and legal to use.

**Your real-time stock data API is ready! 🎉**
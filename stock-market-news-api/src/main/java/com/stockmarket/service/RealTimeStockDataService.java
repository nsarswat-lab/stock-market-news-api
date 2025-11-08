package com.stockmarket.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalDateTime;
import java.time.Duration;

@Service
public class RealTimeStockDataService {
    
    private static final Logger logger = LoggerFactory.getLogger(RealTimeStockDataService.class);
    private final RestTemplate restTemplate;
    
    // Cache for real-time data (1-minute cache)
    private final Map<String, Map<String, Object>> stockDataCache = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> lastFetchTime = new ConcurrentHashMap<>();
    private static final Duration CACHE_DURATION = Duration.ofMinutes(1);
    
    public RealTimeStockDataService() {
        this.restTemplate = new RestTemplate();
    }
    
    public Map<String, Object> getRealTimeStockData(String symbol) {
        logger.info("🔍 Fetching REAL-TIME data for {} from Yahoo Finance", symbol);
        
        Map<String, Object> stockData = null;
        
        // Try Yahoo Finance first (most reliable free source)
        try {
            stockData = fetchFromYahooFinance(symbol);
            if (stockData != null) {
                stockData.put("dataSource", "REAL_TIME_API");
                stockData.put("mockIndicator", "📡 REAL DATA from Yahoo Finance");
                logger.info("✅ SUCCESS: Real-time data fetched for {} at ₹{}", symbol, stockData.get("currentPrice"));
                return stockData;
            }
        } catch (Exception e) {
            logger.warn("⚠️ Yahoo Finance failed for {}: {}", symbol, e.getMessage());
        }
        
        // If Yahoo Finance fails, try Alpha Vantage
        try {
            stockData = fetchFromAlphaVantage(symbol);
            if (stockData != null) {
                stockData.put("dataSource", "REAL_TIME_API");
                stockData.put("mockIndicator", "📡 REAL DATA from Alpha Vantage");
                logger.info("✅ SUCCESS: Real-time data fetched for {} at ₹{}", symbol, stockData.get("currentPrice"));
                return stockData;
            }
        } catch (Exception e) {
            logger.warn("⚠️ Alpha Vantage failed for {}: {}", symbol, e.getMessage());
        }
        
        // If all real sources fail, return mock data with clear identification
        logger.warn("❌ ALL REAL-TIME SOURCES FAILED for {}, using mock data", symbol);
        stockData = generateMockStockData(symbol);
        stockData.put("dataSource", "BACKEND_MOCK");
        stockData.put("mockIndicator", "🎭 MOCK DATA - NOT REAL (Real-time APIs unavailable)");
        stockData.put("note", "Real-time APIs failed, showing fallback data");
        
        return stockData;
    }
    
    private Map<String, Object> fetchFromYahooFinance(String symbol) {
        try {
            String yahooSymbol = convertToYahooSymbol(symbol);
            String url = String.format("https://query1.finance.yahoo.com/v8/finance/chart/%s", yahooSymbol);
            
            logger.info("📡 CALLING Yahoo Finance API: {}", url);
            
            // Add proper headers to avoid blocking
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            headers.set("Accept", "application/json");
            headers.set("Accept-Language", "en-US,en;q=0.9");
            headers.set("Cache-Control", "no-cache");
            
            org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);
            
            org.springframework.http.ResponseEntity<Map> response = restTemplate.exchange(
                url, org.springframework.http.HttpMethod.GET, entity, Map.class);
            
            logger.info("📡 Yahoo Finance Response Status: {}", response.getStatusCode());
            
            if (response.getBody() != null) {
                logger.debug("📡 Yahoo Finance Response Keys: {}", response.getBody().keySet());
                
                if (response.getBody().containsKey("chart")) {
                    Map<String, Object> result = parseYahooResponse(response.getBody(), symbol);
                    if (result != null) {
                        logger.info("✅ Yahoo Finance SUCCESS for {}: ₹{}", symbol, result.get("currentPrice"));
                        return result;
                    }
                }
            }
            
        } catch (Exception e) {
            logger.error("❌ Yahoo Finance API ERROR for {}: {}", symbol, e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    private Map<String, Object> fetchFromAlphaVantage(String symbol) {
        try {
            logger.debug("📡 Trying Alpha Vantage API for {}", symbol);
            
            // Alpha Vantage (free tier: 5 calls/minute, 500/day)
            // Note: Add your free API key from https://www.alphavantage.co/support/#api-key
            String apiKey = "demo"; // Replace with actual free API key
            String avSymbol = convertToAlphaVantageSymbol(symbol);
            String url = String.format(
                "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=%s&apikey=%s",
                avSymbol, apiKey
            );
            
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response != null && response.containsKey("Global Quote")) {
                return parseAlphaVantageResponse(response, symbol);
            }
            
        } catch (Exception e) {
            logger.debug("⚠️ Alpha Vantage API failed for {}: {}", symbol, e.getMessage());
        }
        
        return null;
    }
    
    private Map<String, Object> fetchFromTwelveData(String symbol) {
        try {
            logger.debug("📡 Trying Twelve Data API for {}", symbol);
            
            // Twelve Data (free tier: 800 calls/day)
            // Note: Get free API key from https://twelvedata.com/
            String apiKey = "demo"; // Replace with actual free API key
            String tdSymbol = convertToTwelveDataSymbol(symbol);
            String url = String.format(
                "https://api.twelvedata.com/quote?symbol=%s&apikey=%s",
                tdSymbol, apiKey
            );
            
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response != null && response.containsKey("close")) {
                return parseTwelveDataResponse(response, symbol);
            }
            
        } catch (Exception e) {
            logger.debug("⚠️ Twelve Data API failed for {}: {}", symbol, e.getMessage());
        }
        
        return null;
    }
    
    private Map<String, Object> parseYahooResponse(Map<String, Object> response, String symbol) {
        try {
            logger.debug("📊 Parsing Yahoo Finance response for {}", symbol);
            
            Map<String, Object> chart = (Map<String, Object>) response.get("chart");
            List<Map<String, Object>> result = (List<Map<String, Object>>) chart.get("result");
            
            if (result != null && !result.isEmpty()) {
                Map<String, Object> data = result.get(0);
                Map<String, Object> meta = (Map<String, Object>) data.get("meta");
                
                logger.debug("📊 Yahoo meta keys: {}", meta.keySet());
                
                // Extract real-time data
                Object currentPrice = meta.get("regularMarketPrice");
                Object previousClose = meta.get("previousClose");
                Object dayHigh = meta.get("regularMarketDayHigh");
                Object dayLow = meta.get("regularMarketDayLow");
                Object volume = meta.get("regularMarketVolume");
                
                if (currentPrice != null) {
                    double price = ((Number) currentPrice).doubleValue();
                    double prevClose = previousClose != null ? ((Number) previousClose).doubleValue() : price;
                    double changePercent = ((price - prevClose) / prevClose) * 100;
                    
                    Map<String, Object> stockData = new HashMap<>();
                    stockData.put("symbol", symbol);
                    stockData.put("currentPrice", Math.round(price * 100.0) / 100.0);
                    stockData.put("changePercent", Math.round(changePercent * 100.0) / 100.0);
                    stockData.put("dayHigh", dayHigh != null ? Math.round(((Number) dayHigh).doubleValue() * 100.0) / 100.0 : price * 1.02);
                    stockData.put("dayLow", dayLow != null ? Math.round(((Number) dayLow).doubleValue() * 100.0) / 100.0 : price * 0.98);
                    stockData.put("previousClose", Math.round(prevClose * 100.0) / 100.0);
                    stockData.put("volume", volume != null ? ((Number) volume).longValue() : 1000000L);
                    stockData.put("timestamp", System.currentTimeMillis());
                    stockData.put("source", "Yahoo Finance API");
                    
                    logger.info("✅ Parsed Yahoo data for {}: ₹{} ({}%)", symbol, stockData.get("currentPrice"), stockData.get("changePercent"));
                    return stockData;
                }
            }
        } catch (Exception e) {
            logger.error("❌ Error parsing Yahoo response for {}: {}", symbol, e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    private Map<String, Object> parseAlphaVantageResponse(Map<String, Object> response, String symbol) {
        try {
            Map<String, Object> quote = (Map<String, Object>) response.get("Global Quote");
            
            return Map.of(
                "symbol", symbol,
                "currentPrice", Double.parseDouble((String) quote.get("05. price")),
                "changePercent", Double.parseDouble(((String) quote.get("10. change percent")).replace("%", "")),
                "dayHigh", Double.parseDouble((String) quote.get("03. high")),
                "dayLow", Double.parseDouble((String) quote.get("04. low")),
                "previousClose", Double.parseDouble((String) quote.get("08. previous close")),
                "volume", Long.parseLong((String) quote.get("06. volume")),
                "timestamp", System.currentTimeMillis(),
                "source", "Alpha Vantage"
            );
        } catch (Exception e) {
            logger.warn("⚠️ Error parsing Alpha Vantage response: {}", e.getMessage());
            return null;
        }
    }
    
    private Map<String, Object> parseTwelveDataResponse(Map<String, Object> response, String symbol) {
        try {
            return Map.of(
                "symbol", symbol,
                "currentPrice", Double.parseDouble((String) response.get("close")),
                "changePercent", Double.parseDouble((String) response.get("percent_change")),
                "dayHigh", Double.parseDouble((String) response.get("high")),
                "dayLow", Double.parseDouble((String) response.get("low")),
                "previousClose", Double.parseDouble((String) response.get("previous_close")),
                "volume", Long.parseLong((String) response.get("volume")),
                "timestamp", System.currentTimeMillis(),
                "source", "Twelve Data"
            );
        } catch (Exception e) {
            logger.warn("⚠️ Error parsing Twelve Data response: {}", e.getMessage());
            return null;
        }
    }
    
    private String convertToYahooSymbol(String symbol) {
        // Convert to Yahoo Finance format (add .NS for NSE stocks)
        return switch (symbol) {
            case "RELIANCE" -> "RELIANCE.NS";
            case "HDFCBANK" -> "HDFCBANK.NS";
            case "TCS" -> "TCS.NS";
            case "INFY" -> "INFY.NS";
            case "BHARTIARTL" -> "BHARTIARTL.NS";
            case "ITC" -> "ITC.NS";
            default -> symbol + ".NS";
        };
    }
    
    private String convertToAlphaVantageSymbol(String symbol) {
        // Alpha Vantage uses different format for Indian stocks
        return convertToYahooSymbol(symbol); // Same as Yahoo for Indian stocks
    }
    
    private String convertToTwelveDataSymbol(String symbol) {
        // Twelve Data format
        return convertToYahooSymbol(symbol); // Same as Yahoo for Indian stocks
    }
    
    private double calculateChangePercent(Map<String, Object> meta) {
        try {
            double current = ((Number) meta.get("regularMarketPrice")).doubleValue();
            double previous = ((Number) meta.get("previousClose")).doubleValue();
            return ((current - previous) / previous) * 100;
        } catch (Exception e) {
            return 0.0;
        }
    }
    
    private Map<String, Object> generateMockStockData(String symbol) {
        // Generate realistic mock data when real APIs are unavailable
        Random random = new Random();
        double basePrice = getBasePrice(symbol);
        double changePercent = (random.nextDouble() - 0.5) * 4; // -2% to +2%
        double currentPrice = basePrice * (1 + changePercent / 100);
        
        return Map.of(
            "symbol", symbol,
            "currentPrice", Math.round(currentPrice * 100.0) / 100.0,
            "changePercent", Math.round(changePercent * 100.0) / 100.0,
            "dayHigh", Math.round((currentPrice * 1.02) * 100.0) / 100.0,
            "dayLow", Math.round((currentPrice * 0.98) * 100.0) / 100.0,
            "previousClose", Math.round(basePrice * 100.0) / 100.0,
            "volume", random.nextInt(5000000) + 1000000,
            "timestamp", System.currentTimeMillis(),
            "source", "Mock Data Generator"
        );
    }
    
    private double getBasePrice(String symbol) {
        // Realistic base prices for Indian stocks (approximate)
        return switch (symbol) {
            case "RELIANCE" -> 2750.0;
            case "HDFCBANK" -> 1680.0;
            case "TCS" -> 4100.0;
            case "INFY" -> 1480.0;
            case "BHARTIARTL" -> 950.0;
            case "ITC" -> 420.0;
            default -> 1000.0;
        };
    }
    
    private boolean isDataFresh(String symbol) {
        LocalDateTime lastFetch = lastFetchTime.get(symbol);
        return lastFetch != null && 
               Duration.between(lastFetch, LocalDateTime.now()).compareTo(CACHE_DURATION) < 0;
    }
    
    public List<String> getAvailableDataSources() {
        return Arrays.asList(
            "Yahoo Finance API (Free, no API key required)",
            "Alpha Vantage (Free tier: 500 calls/day, API key required)", 
            "Twelve Data (Free tier: 800 calls/day, API key required)"
        );
    }
}
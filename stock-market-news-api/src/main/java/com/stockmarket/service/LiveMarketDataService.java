package com.stockmarket.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResourceAccessException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalDateTime;
import java.time.Duration;

@Service
public class LiveMarketDataService {
    
    private static final Logger logger = LoggerFactory.getLogger(LiveMarketDataService.class);
    private final RestTemplate restTemplate;
    
    // Cache for market data with timestamp
    private final Map<String, MarketData> marketDataCache = new ConcurrentHashMap<>();
    private static final Duration CACHE_DURATION = Duration.ofMinutes(1); // 1-minute cache
    
    @Value("${alphavantage.api.key:demo}")
    private String alphaVantageKey;
    
    public LiveMarketDataService() {
        this.restTemplate = new RestTemplate();
    }
    
    public double getCurrentPrice(String symbol) {
        MarketData data = getMarketData(symbol);
        return data != null ? data.currentPrice : getFallbackPrice(symbol);
    }
    
    public long getCurrentVolume(String symbol) {
        MarketData data = getMarketData(symbol);
        return data != null ? data.volume : getFallbackVolume(symbol);
    }
    
    public Map<String, Object> getCompleteMarketData(String symbol) {
        MarketData data = getMarketData(symbol);
        if (data == null) {
            return getFallbackMarketData(symbol);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("currentPrice", data.currentPrice);
        result.put("volume", data.volume);
        result.put("dayHigh", data.dayHigh);
        result.put("dayLow", data.dayLow);
        result.put("previousClose", data.previousClose);
        result.put("changePercent", data.changePercent);
        result.put("lastUpdated", data.lastUpdated);
        result.put("dataSource", data.dataSource);
        
        return result;
    }
    
    private MarketData getMarketData(String symbol) {
        // Check cache first
        MarketData cached = marketDataCache.get(symbol);
        if (cached != null && !isCacheExpired(cached)) {
            logger.debug("📊 Using cached data for {}", symbol);
            return cached;
        }
        
        // Try multiple data sources
        MarketData data = null;
        
        // 1. Try Alpha Vantage API
        data = fetchFromAlphaVantage(symbol);
        if (data != null) {
            marketDataCache.put(symbol, data);
            return data;
        }
        
        // 2. Try Yahoo Finance API (free alternative)
        data = fetchFromYahooFinance(symbol);
        if (data != null) {
            marketDataCache.put(symbol, data);
            return data;
        }
        
        // 3. Try NSE India API (for Indian stocks)
        data = fetchFromNSEIndia(symbol);
        if (data != null) {
            marketDataCache.put(symbol, data);
            return data;
        }
        
        logger.warn("⚠️ Failed to fetch live data for {}, using fallback", symbol);
        return null;
    }
    
    private MarketData fetchFromAlphaVantage(String symbol) {
        try {
            String url = String.format(
                "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=%s.BSE&apikey=%s",
                symbol, alphaVantageKey
            );
            
            logger.debug("🌐 Fetching from Alpha Vantage: {}", symbol);
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response != null && response.containsKey("Global Quote")) {
                Map<String, Object> quote = (Map<String, Object>) response.get("Global Quote");
                return parseAlphaVantageData(quote, symbol);
            }
            
        } catch (ResourceAccessException e) {
            logger.debug("🌐 Alpha Vantage API unavailable for {}: {}", symbol, e.getMessage());
        } catch (Exception e) {
            logger.warn("⚠️ Error fetching from Alpha Vantage for {}: {}", symbol, e.getMessage());
        }
        
        return null;
    }
    
    private MarketData fetchFromYahooFinance(String symbol) {
        try {
            // Yahoo Finance API endpoint (free)
            String yahooSymbol = getYahooSymbol(symbol);
            String url = String.format(
                "https://query1.finance.yahoo.com/v8/finance/chart/%s",
                yahooSymbol
            );
            
            logger.debug("🌐 Fetching from Yahoo Finance: {}", symbol);
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response != null && response.containsKey("chart")) {
                return parseYahooFinanceData(response, symbol);
            }
            
        } catch (ResourceAccessException e) {
            logger.debug("🌐 Yahoo Finance API unavailable for {}: {}", symbol, e.getMessage());
        } catch (Exception e) {
            logger.warn("⚠️ Error fetching from Yahoo Finance for {}: {}", symbol, e.getMessage());
        }
        
        return null;
    }
    
    private MarketData fetchFromNSEIndia(String symbol) {
        try {
            // NSE India API (unofficial endpoint)
            String url = String.format(
                "https://www.nseindia.com/api/quote-equity?symbol=%s",
                symbol
            );
            
            logger.debug("🌐 Fetching from NSE India: {}", symbol);
            
            // Add headers to mimic browser request
            Map<String, String> headers = Map.of(
                "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                "Accept", "application/json"
            );
            
            // Note: NSE API requires proper headers and may block automated requests
            // This is a simplified example - in production, use proper NSE data feeds
            
        } catch (Exception e) {
            logger.debug("🌐 NSE India API unavailable for {}: {}", symbol, e.getMessage());
        }
        
        return null;
    }
    
    private MarketData parseAlphaVantageData(Map<String, Object> quote, String symbol) {
        try {
            MarketData data = new MarketData();
            data.symbol = symbol;
            data.currentPrice = Double.parseDouble((String) quote.get("05. price"));
            data.volume = Long.parseLong((String) quote.get("06. volume"));
            data.dayHigh = Double.parseDouble((String) quote.get("03. high"));
            data.dayLow = Double.parseDouble((String) quote.get("04. low"));
            data.previousClose = Double.parseDouble((String) quote.get("08. previous close"));
            
            String changePercent = (String) quote.get("10. change percent");
            data.changePercent = Double.parseDouble(changePercent.replace("%", ""));
            
            data.lastUpdated = LocalDateTime.now();
            data.dataSource = "Alpha Vantage";
            
            logger.info("📊 Live data fetched for {}: ₹{} ({}%)", symbol, data.currentPrice, data.changePercent);
            return data;
            
        } catch (Exception e) {
            logger.warn("⚠️ Error parsing Alpha Vantage data for {}: {}", symbol, e.getMessage());
            return null;
        }
    }
    
    private MarketData parseYahooFinanceData(Map<String, Object> response, String symbol) {
        try {
            Map<String, Object> chart = (Map<String, Object>) response.get("chart");
            List<Map<String, Object>> result = (List<Map<String, Object>>) chart.get("result");
            
            if (result != null && !result.isEmpty()) {
                Map<String, Object> data = result.get(0);
                Map<String, Object> meta = (Map<String, Object>) data.get("meta");
                
                MarketData marketData = new MarketData();
                marketData.symbol = symbol;
                marketData.currentPrice = ((Number) meta.get("regularMarketPrice")).doubleValue();
                marketData.volume = ((Number) meta.get("regularMarketVolume")).longValue();
                marketData.dayHigh = ((Number) meta.get("regularMarketDayHigh")).doubleValue();
                marketData.dayLow = ((Number) meta.get("regularMarketDayLow")).doubleValue();
                marketData.previousClose = ((Number) meta.get("previousClose")).doubleValue();
                
                double change = marketData.currentPrice - marketData.previousClose;
                marketData.changePercent = (change / marketData.previousClose) * 100;
                
                marketData.lastUpdated = LocalDateTime.now();
                marketData.dataSource = "Yahoo Finance";
                
                logger.info("📊 Live data fetched for {}: ₹{} ({}%)", symbol, marketData.currentPrice, marketData.changePercent);
                return marketData;
            }
            
        } catch (Exception e) {
            logger.warn("⚠️ Error parsing Yahoo Finance data for {}: {}", symbol, e.getMessage());
        }
        
        return null;
    }
    
    private String getYahooSymbol(String symbol) {
        // Convert NSE symbols to Yahoo Finance format
        return switch (symbol) {
            case "RELIANCE" -> "RELIANCE.NS";
            case "HDFCBANK" -> "HDFCBANK.NS";
            case "TCS" -> "TCS.NS";
            case "INFY" -> "INFY.NS";
            case "BHARTIARTL" -> "BHARTIARTL.NS";
            case "ADANIGREEN" -> "ADANIGREEN.NS";
            case "ITC" -> "ITC.NS";
            default -> symbol + ".NS";
        };
    }
    
    private boolean isCacheExpired(MarketData data) {
        return Duration.between(data.lastUpdated, LocalDateTime.now()).compareTo(CACHE_DURATION) > 0;
    }
    
    // Fallback methods for when APIs are unavailable
    private double getFallbackPrice(String symbol) {
        logger.debug("📊 Using fallback price for {}", symbol);
        return switch (symbol) {
            case "RELIANCE" -> 2742.85;
            case "HDFCBANK" -> 1685.40;
            case "TCS" -> 4127.65;
            case "INFY" -> 1481.20;
            case "BHARTIARTL" -> 948.75;
            case "ADANIGREEN" -> 1456.30;
            case "ITC" -> 418.95;
            default -> 1000.0;
        };
    }
    
    private long getFallbackVolume(String symbol) {
        return switch (symbol) {
            case "RELIANCE" -> 4850000L;
            case "HDFCBANK" -> 3650000L;
            case "TCS" -> 1980000L;
            case "INFY" -> 4200000L;
            case "BHARTIARTL" -> 2800000L;
            case "ADANIGREEN" -> 1500000L;
            case "ITC" -> 3200000L;
            default -> 1000000L;
        };
    }
    
    private Map<String, Object> getFallbackMarketData(String symbol) {
        Map<String, Object> data = new HashMap<>();
        data.put("currentPrice", getFallbackPrice(symbol));
        data.put("volume", getFallbackVolume(symbol));
        data.put("dayHigh", getFallbackPrice(symbol) * 1.02);
        data.put("dayLow", getFallbackPrice(symbol) * 0.98);
        data.put("previousClose", getFallbackPrice(symbol) * 0.995);
        data.put("changePercent", 0.5);
        data.put("lastUpdated", LocalDateTime.now());
        data.put("dataSource", "Fallback Data");
        return data;
    }
    
    // Inner class for market data
    private static class MarketData {
        String symbol;
        double currentPrice;
        long volume;
        double dayHigh;
        double dayLow;
        double previousClose;
        double changePercent;
        LocalDateTime lastUpdated;
        String dataSource;
    }
}
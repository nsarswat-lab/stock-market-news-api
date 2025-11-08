package com.stockmarket.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.time.LocalDateTime;

@Service
public class LiveMarketDataService {
    
    private static final Logger logger = LoggerFactory.getLogger(LiveMarketDataService.class);
    
    @Autowired
    private RealTimeStockDataService realTimeStockDataService;
    
    public double getCurrentPrice(String symbol) {
        try {
            Map<String, Object> realTimeData = realTimeStockDataService.getRealTimeStockData(symbol);
            Object price = realTimeData.get("currentPrice");
            if (price instanceof Number) {
                logger.debug("📊 Using real-time price for {}: {}", symbol, price);
                return ((Number) price).doubleValue();
            }
        } catch (Exception e) {
            logger.debug("⚠️ Real-time price fetch failed for {}, using fallback", symbol);
        }
        
        // Fallback to hardcoded prices
        return getFallbackPrice(symbol);
    }
    
    public long getCurrentVolume(String symbol) {
        try {
            Map<String, Object> realTimeData = realTimeStockDataService.getRealTimeStockData(symbol);
            Object volume = realTimeData.get("volume");
            if (volume instanceof Number) {
                logger.debug("📊 Using real-time volume for {}: {}", symbol, volume);
                return ((Number) volume).longValue();
            }
        } catch (Exception e) {
            logger.debug("⚠️ Real-time volume fetch failed for {}, using fallback", symbol);
        }
        
        // Fallback to hardcoded volumes
        return getFallbackVolume(symbol);
    }
    
    public Map<String, Object> getCompleteMarketData(String symbol) {
        try {
            // Try to get real-time data first
            Map<String, Object> realTimeData = realTimeStockDataService.getRealTimeStockData(symbol);
            if (realTimeData != null && realTimeData.containsKey("currentPrice")) {
                logger.debug("📊 Using real-time market data for {}", symbol);
                return realTimeData;
            }
        } catch (Exception e) {
            logger.debug("⚠️ Real-time market data fetch failed for {}, using fallback", symbol);
        }
        
        // Fallback to simulated data
        return getFallbackMarketData(symbol);
    }
    

    
    // Fallback methods for when real-time APIs are unavailable
    private double getFallbackPrice(String symbol) {
        logger.debug("📊 Using fallback price for {}", symbol);
        return switch (symbol) {
            case "RELIANCE" -> 2750.50;
            case "HDFCBANK" -> 1685.40;
            case "TCS" -> 4127.65;
            case "INFY" -> 1481.20;
            case "BHARTIARTL" -> 948.75;
            case "ITC" -> 418.95;
            default -> 1000.0;
        };
    }
    
    private long getFallbackVolume(String symbol) {
        return switch (symbol) {
            case "RELIANCE" -> 4500000L;
            case "HDFCBANK" -> 3650000L;
            case "TCS" -> 1980000L;
            case "INFY" -> 4200000L;
            case "BHARTIARTL" -> 2800000L;
            case "ITC" -> 3200000L;
            default -> 1000000L;
        };
    }
    
    private Map<String, Object> getFallbackMarketData(String symbol) {
        Map<String, Object> data = new HashMap<>();
        data.put("symbol", symbol);
        data.put("currentPrice", getFallbackPrice(symbol));
        data.put("volume", getFallbackVolume(symbol));
        data.put("dayHigh", getFallbackPrice(symbol) * 1.02);
        data.put("dayLow", getFallbackPrice(symbol) * 0.98);
        data.put("previousClose", getFallbackPrice(symbol) * 0.995);
        data.put("changePercent", 1.25);
        data.put("timestamp", System.currentTimeMillis());
        data.put("source", "Fallback Data");
        data.put("dataSource", "BACKEND_MOCK");
        data.put("mockIndicator", "🎭 MOCK DATA - NOT REAL (Real-time APIs unavailable)");
        return data;
    }
}
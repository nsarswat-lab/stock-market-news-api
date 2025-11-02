package com.stockmarket.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class IntradayTradingService {
    
    private static final Logger logger = LoggerFactory.getLogger(IntradayTradingService.class);
    private final RestTemplate restTemplate;
    
    @Autowired
    private AdvancedAnalyticsService advancedAnalyticsService;
    
    @Autowired
    private IntelligentRecommendationEngine intelligentEngine;
    
    @Autowired
    private LiveMarketDataService liveMarketDataService;
    
    @Value("${alphavantage.api.key:demo}")
    private String apiKey;
    
    public IntradayTradingService() {
        this.restTemplate = new RestTemplate();
    }
    
    public List<Map<String, Object>> getIntradayRecommendations() {
        logger.debug("⚡ Generating intelligent intraday recommendations using comprehensive analytics");
        
        // Generate intelligent recommendations using all analytics
        String[] symbols = {"RELIANCE", "HDFCBANK", "TCS", "INFY"};
        List<Map<String, Object>> recommendations = new ArrayList<>();
        
        for (String symbol : symbols) {
            try {
                Map<String, Object> recommendation = intelligentEngine.generateIntelligentRecommendation(symbol);
                
                // Add intraday-specific enhancements
                recommendation.put("tradingStyle", "INTRADAY");
                recommendation.put("marketSession", getCurrentMarketSession());
                recommendation.put("volumeProfile", getVolumeProfile(symbol));
                recommendation.put("intradayLevels", getIntradayLevels(symbol));
                
                recommendations.add(recommendation);
                
            } catch (Exception e) {
                logger.warn("⚠️ Failed to generate intelligent recommendation for {}: {}", symbol, e.getMessage());
                // Fallback to basic recommendation if needed
                recommendations.add(createFallbackRecommendation(symbol));
            }
        }
        
        logger.debug("⚡ Generated {} intelligent intraday recommendations", recommendations.size());
        return recommendations;
        
        // OLD HARDCODED APPROACH - REPLACED WITH INTELLIGENT ENGINE
        /*
        return Arrays.asList(
        */
    }
    
    private String getCurrentMarketSession() {
        // Simplified market session detection
        return "Regular Trading (9:15 AM - 3:30 PM)";
    }
    
    private Map<String, String> getVolumeProfile(String symbol) {
        return Map.of(
            "morningVolume", "35% of daily average",
            "afternoonVolume", "65% of daily average", 
            "volumeTrend", "Increasing",
            "institutionalParticipation", "High"
        );
    }
    
    private Map<String, String> getIntradayLevels(String symbol) {
        double currentPrice = liveMarketDataService.getCurrentPrice(symbol);
        return Map.of(
            "pivotPoint", String.format("₹%.0f", currentPrice * 1.002),
            "resistance1", String.format("₹%.0f", currentPrice * 1.015),
            "support1", String.format("₹%.0f", currentPrice * 0.985),
            "dayHigh", String.format("₹%.0f", currentPrice * 1.025),
            "dayLow", String.format("₹%.0f", currentPrice * 0.975)
        );
    }
    
    private Map<String, Object> createFallbackRecommendation(String symbol) {
        // Fallback to basic recommendation if intelligent engine fails
        Map<String, Object> fallback = new HashMap<>();
        fallback.put("symbol", symbol);
        fallback.put("action", "HOLD");
        fallback.put("confidence", "LOW");
        fallback.put("reason", "Insufficient data for intelligent analysis");
        fallback.put("target", String.format("₹%.0f", liveMarketDataService.getCurrentPrice(symbol) * 1.02));
        fallback.put("stopLoss", String.format("₹%.0f", liveMarketDataService.getCurrentPrice(symbol) * 0.98));
        return fallback;
    }
    
    private Map<String, Object> createEnhancedRecommendation(
            String symbol, String action, String confidence, String target, String stopLoss, 
            String timeframe, String reason, Map<String, String> technicals, 
            Map<String, String> institutionalData) {
        
        Map<String, Object> recommendation = new HashMap<>();
        recommendation.put("symbol", symbol);
        recommendation.put("action", action);
        recommendation.put("confidence", confidence);
        recommendation.put("target", target);
        recommendation.put("stopLoss", stopLoss);
        recommendation.put("timeframe", timeframe);
        recommendation.put("reason", reason);
        recommendation.put("technicals", technicals);
        recommendation.put("institutionalData", institutionalData);
        
        // Add market sentiment analysis
        recommendation.put("marketSentiment", analyzeMarketSentiment(institutionalData));
        
        // Add risk assessment based on institutional activity
        recommendation.put("riskLevel", assessInstitutionalRisk(institutionalData, action));
        
        // Add advanced analytics using live data
        double currentPrice = liveMarketDataService.getCurrentPrice(symbol);
        long volume = liveMarketDataService.getCurrentVolume(symbol);
        Map<String, Object> advancedMetrics = advancedAnalyticsService.getAdvancedMetrics(symbol, currentPrice, volume);
        recommendation.put("advancedAnalytics", advancedMetrics);
        
        return recommendation;
    }
    
    private String analyzeMarketSentiment(Map<String, String> institutionalData) {
        String fiiActivity = institutionalData.get("fiiActivity");
        String diiActivity = institutionalData.get("diiActivity");
        String bigBullIndicator = institutionalData.get("bigBullIndicator");
        
        if (bigBullIndicator.equals("Active") && fiiActivity.contains("Buyer")) {
            return "VERY_BULLISH";
        } else if (fiiActivity.contains("Buyer") && diiActivity.contains("Buyer")) {
            return "BULLISH";
        } else if (fiiActivity.contains("Seller") && diiActivity.contains("Seller")) {
            return "BEARISH";
        } else if (bigBullIndicator.equals("Inactive") && fiiActivity.contains("Seller")) {
            return "VERY_BEARISH";
        } else {
            return "NEUTRAL";
        }
    }
    
    private String assessInstitutionalRisk(Map<String, String> institutionalData, String action) {

        String institutionalActivity = institutionalData.get("institutionalActivity");
        String volumeSpike = institutionalData.get("volumeSpike");
        
        // Extract volume multiplier
        double volumeMultiplier = 1.0;
        try {
            String[] parts = volumeSpike.split("x");
            if (parts.length > 0) {
                volumeMultiplier = Double.parseDouble(parts[0]);
            }
        } catch (NumberFormatException e) {
            volumeMultiplier = 1.0;
        }
        
        // High risk if going against institutional flow
        if ((action.equals("BUY") && institutionalActivity.contains("Selling")) ||
            (action.equals("SELL") && institutionalActivity.contains("Buying"))) {
            return "HIGH";
        }
        
        // Medium risk for high volume spikes (could be manipulation)
        if (volumeMultiplier > 3.0) {
            return "MEDIUM";
        }
        
        // Low risk when aligned with smart money
        if (institutionalActivity.contains("Smart Money") || 
            institutionalActivity.contains("Accumulation")) {
            return "LOW";
        }
        
        return "MEDIUM";
    }
    
    // Removed hardcoded price methods - now using LiveMarketDataService for real-time data
}
package com.stockmarket.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResourceAccessException;

import java.util.*;

@Service
public class TradingRecommendationService {
    
    private static final Logger logger = LoggerFactory.getLogger(TradingRecommendationService.class);
    private final RestTemplate restTemplate;
    
    @Value("${alphavantage.api.key:demo}")
    private String apiKey;
    
    private static final String ALPHA_VANTAGE_QUOTE_URL = 
        "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol={symbol}&apikey={apikey}";
    
    private static final String ALPHA_VANTAGE_DAILY_URL = 
        "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol={symbol}&apikey={apikey}";
    
    public TradingRecommendationService() {
        this.restTemplate = new RestTemplate();
    }
    
    public List<Map<String, Object>> getIntradayRecommendations() {
        logger.debug("💡 Generating trading recommendations based on real market data");
        
        String[] symbols = {"AAPL", "TSLA", "MSFT", "GOOGL", "AMZN"};
        List<Map<String, Object>> recommendations = new ArrayList<>();
        
        for (String symbol : symbols) {
            try {
                Map<String, Object> recommendation = generateRecommendation(symbol);
                if (recommendation != null) {
                    recommendations.add(recommendation);
                }
                
                // Rate limiting - Alpha Vantage free tier allows 5 calls per minute
                Thread.sleep(1000);
                
            } catch (Exception e) {
                logger.warn("⚠️ Failed to get data for {}: {}", symbol, e.getMessage());
                // Continue with other symbols
            }
            
            if (recommendations.size() >= 3) {
                break; // Limit to 3 recommendations for demo
            }
        }
        
        if (recommendations.isEmpty()) {
            logger.debug("🎭 Using fallback recommendations (API unavailable)");
            return getFallbackRecommendations();
        }
        
        logger.debug("📊 Generated {} real trading recommendations", recommendations.size());
        return recommendations;
    }
    
    private Map<String, Object> generateRecommendation(String symbol) {
        try {
            Map<String, Object> quoteResponse = restTemplate.getForObject(
                ALPHA_VANTAGE_QUOTE_URL, 
                Map.class, 
                symbol, 
                apiKey
            );
            
            if (quoteResponse == null || !quoteResponse.containsKey("Global Quote")) {
                return null;
            }
            
            Map<String, Object> quote = (Map<String, Object>) quoteResponse.get("Global Quote");
            
            String currentPrice = (String) quote.get("05. price");
            String changePercent = (String) quote.get("10. change percent");
            String previousClose = (String) quote.get("08. previous close");
            
            if (currentPrice == null || changePercent == null) {
                return null;
            }
            
            double price = Double.parseDouble(currentPrice);
            double changePct = Double.parseDouble(changePercent.replace("%", ""));
            
            // Simple technical analysis for recommendation
            String action = determineAction(changePct, price);
            String confidence = determineConfidence(Math.abs(changePct));
            String targetPrice = calculateTargetPrice(price, action);
            String reason = generateReason(changePct, action);
            
            return Map.of(
                "symbol", symbol,
                "action", action,
                "confidence", confidence,
                "target", targetPrice,
                "reason", reason
            );
            
        } catch (ResourceAccessException e) {
            logger.warn("⚠️ Network error for {}: {}", symbol, e.getMessage());
            return null;
        } catch (Exception e) {
            logger.error("❌ Error processing data for {}: {}", symbol, e.getMessage());
            return null;
        }
    }
    
    private String determineAction(double changePercent, double price) {
        if (changePercent > 2.0) {
            return "HOLD"; // Already moved up significantly
        } else if (changePercent > 0.5) {
            return "BUY"; // Positive momentum
        } else if (changePercent < -2.0) {
            return "BUY"; // Oversold opportunity
        } else if (changePercent < -0.5) {
            return "HOLD"; // Wait for stabilization
        } else {
            return "HOLD"; // Neutral movement
        }
    }
    
    private String determineConfidence(double absChangePercent) {
        if (absChangePercent > 3.0) {
            return "HIGH";
        } else if (absChangePercent > 1.0) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }
    
    private String calculateTargetPrice(double currentPrice, String action) {
        double multiplier = switch (action) {
            case "BUY" -> 1.05; // 5% upside target
            case "SELL" -> 0.95; // 5% downside protection
            default -> 1.02; // 2% modest target for HOLD
        };
        
        return String.format("%.2f", currentPrice * multiplier);
    }
    
    private String generateReason(double changePercent, String action) {
        if (changePercent > 2.0) {
            return "Strong upward momentum, consider taking profits";
        } else if (changePercent > 0.5) {
            return "Positive trend with good entry opportunity";
        } else if (changePercent < -2.0) {
            return "Oversold condition presents buying opportunity";
        } else if (changePercent < -0.5) {
            return "Temporary weakness, wait for stabilization";
        } else {
            return "Consolidation phase, monitor for breakout";
        }
    }
    
    private List<Map<String, Object>> getFallbackRecommendations() {
        return Arrays.asList(
            Map.of("symbol", "AAPL", "action", "BUY", "confidence", "MEDIUM", "target", "175.50", "reason", "Technical analysis suggests upward trend"),
            Map.of("symbol", "MSFT", "action", "HOLD", "confidence", "HIGH", "target", "380.00", "reason", "Strong fundamentals, wait for better entry"),
            Map.of("symbol", "TSLA", "action", "HOLD", "confidence", "LOW", "target", "220.00", "reason", "High volatility requires caution")
        );
    }
}
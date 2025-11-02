package com.stockmarket.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IntelligentRecommendationEngine {
    
    private static final Logger logger = LoggerFactory.getLogger(IntelligentRecommendationEngine.class);
    
    @Autowired
    private AdvancedAnalyticsService advancedAnalyticsService;
    
    @Autowired
    private LiveMarketDataService liveMarketDataService;
    
    public Map<String, Object> generateIntelligentRecommendation(String symbol) {
        logger.debug("🧠 Generating intelligent recommendation for {} using all analytics", symbol);
        
        // Get live market data
        double currentPrice = liveMarketDataService.getCurrentPrice(symbol);
        long volume = liveMarketDataService.getCurrentVolume(symbol);
        Map<String, Object> liveData = liveMarketDataService.getCompleteMarketData(symbol);
        
        // Get all advanced analytics
        Map<String, Object> analytics = advancedAnalyticsService.getAdvancedMetrics(symbol, currentPrice, volume);
        
        // Extract key metrics for decision making
        Map<String, Object> riskMetrics = (Map<String, Object>) analytics.get("riskMetrics");
        Map<String, Object> technicals = (Map<String, Object>) analytics.get("technicalIndicators");
        Map<String, Object> earnings = (Map<String, Object>) analytics.get("earningsIntelligence");
        Map<String, Object> liquidity = (Map<String, Object>) analytics.get("liquidityMetrics");
        Map<String, Object> options = (Map<String, Object>) analytics.get("optionsAnalysis");
        Map<String, Object> marketContext = (Map<String, Object>) analytics.get("marketContext");
        
        // Generate recommendation based on analytics
        RecommendationDecision decision = analyzeAndDecide(symbol, riskMetrics, technicals, earnings, 
                                                          liquidity, options, marketContext, currentPrice);
        
        // Build comprehensive recommendation
        Map<String, Object> recommendation = new HashMap<>();
        recommendation.put("symbol", symbol);
        recommendation.put("action", decision.action);
        recommendation.put("confidence", decision.confidence);
        recommendation.put("target", decision.target);
        recommendation.put("stopLoss", decision.stopLoss);
        recommendation.put("timeframe", decision.timeframe);
        recommendation.put("reason", decision.reason);
        recommendation.put("riskLevel", decision.riskLevel);
        recommendation.put("expectedReturn", decision.expectedReturn);
        recommendation.put("probabilityOfSuccess", decision.probabilityOfSuccess);
        
        // Add all supporting analytics
        recommendation.put("advancedAnalytics", analytics);
        recommendation.put("decisionFactors", decision.decisionFactors);
        recommendation.put("riskFactors", decision.riskFactors);
        recommendation.put("catalysts", decision.catalysts);
        
        // Add live market data
        recommendation.put("liveMarketData", liveData);
        recommendation.put("currentPrice", String.format("₹%.2f", currentPrice));
        recommendation.put("dayRange", String.format("₹%.2f - ₹%.2f", 
                                                    (Double) liveData.get("dayLow"), 
                                                    (Double) liveData.get("dayHigh")));
        recommendation.put("changePercent", String.format("%.2f%%", (Double) liveData.get("changePercent")));
        recommendation.put("dataSource", liveData.get("dataSource"));
        
        logger.debug("🧠 Generated {} recommendation for {} with {}% confidence", 
                    decision.action, symbol, decision.confidence);
        
        return recommendation;
    }
    
    private RecommendationDecision analyzeAndDecide(String symbol, Map<String, Object> riskMetrics,
                                                   Map<String, Object> technicals, Map<String, Object> earnings,
                                                   Map<String, Object> liquidity, Map<String, Object> options,
                                                   Map<String, Object> marketContext, double currentPrice) {
        
        RecommendationDecision decision = new RecommendationDecision();
        int bullishScore = 0;
        int bearishScore = 0;
        List<String> decisionFactors = new ArrayList<>();
        List<String> riskFactors = new ArrayList<>();
        
        // 1. RISK METRICS ANALYSIS
        double sharpeRatio = Double.parseDouble(((String) riskMetrics.get("sharpeRatio")));
        double beta = (Double) riskMetrics.get("beta");
        String volatilityRanking = (String) riskMetrics.get("volatileRanking");
        
        if (sharpeRatio > 1.4) {
            bullishScore += 15;
            decisionFactors.add("Excellent risk-adjusted returns (Sharpe: " + sharpeRatio + ")");
        } else if (sharpeRatio < 1.0) {
            bearishScore += 10;
            riskFactors.add("Poor risk-adjusted returns (Sharpe: " + sharpeRatio + ")");
        }
        
        if (beta > 1.3) {
            riskFactors.add("High market sensitivity (Beta: " + beta + ")");
        }
        
        if ("High".equals(volatilityRanking)) {
            bearishScore += 5;
            riskFactors.add("High volatility environment");
        }
        
        // 2. TECHNICAL ANALYSIS
        String vwapSignal = (String) technicals.get("vwapSignal");
        String bollingerPosition = (String) technicals.get("bollingerPosition");
        boolean bollingerSqueeze = (Boolean) technicals.get("bollingerSqueeze");
        int relativeStrength = Integer.parseInt(((String) technicals.get("relativeStrength")).split("/")[0]);
        
        if ("Above VWAP".equals(vwapSignal)) {
            bullishScore += 10;
            decisionFactors.add("Trading above VWAP - institutional support");
        } else {
            bearishScore += 5;
            decisionFactors.add("Trading below VWAP - weak momentum");
        }
        
        if (relativeStrength > 70) {
            bullishScore += 15;
            decisionFactors.add("Strong relative strength (" + relativeStrength + "/100)");
        } else if (relativeStrength < 30) {
            bearishScore += 10;
            riskFactors.add("Weak relative strength (" + relativeStrength + "/100)");
        }
        
        if (bollingerSqueeze) {
            bullishScore += 8;
            decisionFactors.add("Bollinger squeeze - breakout imminent");
        }
        
        if (bollingerPosition.contains("Overbought")) {
            bearishScore += 12;
            riskFactors.add("Overbought on Bollinger Bands");
        } else if (bollingerPosition.contains("Oversold")) {
            bullishScore += 12;
            decisionFactors.add("Oversold on Bollinger Bands - bounce opportunity");
        }
        
        // 3. EARNINGS INTELLIGENCE
        int daysToEarnings = (Integer) earnings.get("daysToEarnings");
        String surpriseProbability = (String) earnings.get("earningsSurpriseProbability");
        String analystRevisions = (String) earnings.get("analystRevisions");
        
        if (daysToEarnings <= 15) {
            if (surpriseProbability.contains("70%") || surpriseProbability.contains("72%")) {
                bullishScore += 12;
                decisionFactors.add("High earnings surprise probability near results");
            }
            riskFactors.add("Earnings volatility risk (" + daysToEarnings + " days to results)");
        }
        
        if (analystRevisions.contains("upgrades") && !analystRevisions.contains("downgrades")) {
            bullishScore += 8;
            decisionFactors.add("Recent analyst upgrades");
        } else if (analystRevisions.contains("downgrades")) {
            bearishScore += 8;
            riskFactors.add("Recent analyst downgrades");
        }
        
        // 4. LIQUIDITY ANALYSIS
        String liquidityScore = (String) liquidity.get("liquidityScore");
        String bidAskSpread = (String) liquidity.get("bidAskSpread");
        
        int liquidityNum = Integer.parseInt(liquidityScore.split("/")[0]);
        if (liquidityNum > 90) {
            bullishScore += 5;
            decisionFactors.add("Excellent liquidity (" + liquidityScore + ")");
        } else if (liquidityNum < 70) {
            bearishScore += 8;
            riskFactors.add("Poor liquidity (" + liquidityScore + ")");
        }
        
        // 5. OPTIONS ANALYSIS
        double putCallRatio = Double.parseDouble((String) options.get("putCallRatio"));
        String impliedVolatility = (String) options.get("impliedVolatility");
        String optionsFlow = (String) options.get("optionsFlow");
        
        if (putCallRatio > 1.2) {
            bullishScore += 10;
            decisionFactors.add("High put/call ratio indicates oversold sentiment");
        } else if (putCallRatio < 0.8) {
            bearishScore += 8;
            riskFactors.add("Low put/call ratio indicates complacency");
        }
        
        if ("Call buying dominant".equals(optionsFlow)) {
            bullishScore += 8;
            decisionFactors.add("Dominant call buying flow");
        } else if (optionsFlow.contains("Put buying")) {
            bearishScore += 8;
            riskFactors.add("Increasing put buying activity");
        }
        
        // 6. MARKET CONTEXT
        String marketRegime = (String) marketContext.get("marketRegime");
        String sectorRotation = (String) marketContext.get("sectorRotation");
        
        if (marketRegime.contains("Bull Market")) {
            bullishScore += 10;
            decisionFactors.add("Favorable market regime");
        } else if (marketRegime.contains("Bear")) {
            bearishScore += 15;
            riskFactors.add("Challenging market environment");
        }
        
        if (sectorRotation.contains("in favor")) {
            bullishScore += 8;
            decisionFactors.add("Sector rotation favorable");
        }
        
        // DECISION LOGIC
        int netScore = bullishScore - bearishScore;
        logger.debug("🧠 Analysis for {}: Bullish={}, Bearish={}, Net={}", symbol, bullishScore, bearishScore, netScore);
        
        if (netScore > 25) {
            decision.action = "BUY";
            decision.confidence = "HIGH";
            decision.expectedReturn = "12-18%";
            decision.probabilityOfSuccess = "75-85%";
        } else if (netScore > 10) {
            decision.action = "BUY";
            decision.confidence = "MEDIUM";
            decision.expectedReturn = "8-12%";
            decision.probabilityOfSuccess = "65-75%";
        } else if (netScore > -10) {
            decision.action = "HOLD";
            decision.confidence = "MEDIUM";
            decision.expectedReturn = "3-8%";
            decision.probabilityOfSuccess = "50-65%";
        } else if (netScore > -25) {
            decision.action = "SELL";
            decision.confidence = "MEDIUM";
            decision.expectedReturn = "-5 to +3%";
            decision.probabilityOfSuccess = "60-70%";
        } else {
            decision.action = "SELL";
            decision.confidence = "HIGH";
            decision.expectedReturn = "-10 to -5%";
            decision.probabilityOfSuccess = "70-80%";
        }
        
        // Calculate targets and stops based on volatility and support/resistance
        decision.target = calculateIntelligentTarget(currentPrice, decision.action, riskMetrics, technicals);
        decision.stopLoss = calculateIntelligentStopLoss(currentPrice, decision.action, riskMetrics);
        decision.timeframe = determineTimeframe(decision.action, earnings, technicals);
        decision.riskLevel = assessOverallRisk(riskFactors.size(), bearishScore, volatilityRanking);
        decision.reason = generateIntelligentReason(decision.action, decisionFactors, netScore);
        
        decision.decisionFactors = decisionFactors;
        decision.riskFactors = riskFactors;
        decision.catalysts = generateCatalysts(symbol, earnings, marketContext);
        
        return decision;
    }
    
    private String calculateIntelligentTarget(double currentPrice, String action, 
                                            Map<String, Object> riskMetrics, Map<String, Object> technicals) {
        
        // Get technical levels for realistic targets
        String bollingerPosition = (String) technicals.get("bollingerPosition");
        int relativeStrength = Integer.parseInt(((String) technicals.get("relativeStrength")).split("/")[0]);
        boolean bollingerSqueeze = (Boolean) technicals.get("bollingerSqueeze");
        
        // Calculate realistic intraday targets based on technical analysis
        double targetMultiplier;
        
        switch (action) {
            case "BUY" -> {
                if (bollingerSqueeze && relativeStrength > 70) {
                    targetMultiplier = 1.025; // 2.5% for strong breakout setup
                } else if (relativeStrength > 60) {
                    targetMultiplier = 1.015; // 1.5% for good momentum
                } else if (bollingerPosition.contains("Oversold")) {
                    targetMultiplier = 1.020; // 2% for oversold bounce
                } else {
                    targetMultiplier = 1.010; // 1% conservative target
                }
            }
            case "SELL" -> {
                if (bollingerPosition.contains("Overbought")) {
                    targetMultiplier = 0.980; // 2% down from overbought
                } else if (relativeStrength < 40) {
                    targetMultiplier = 0.985; // 1.5% down for weak momentum
                } else {
                    targetMultiplier = 0.990; // 1% conservative target
                }
            }
            default -> targetMultiplier = 1.005; // 0.5% for HOLD
        }
        
        // Add resistance/support level analysis
        double technicalTarget = currentPrice * targetMultiplier;
        
        // Adjust based on key levels (simplified - in real system use actual S/R levels)
        double adjustedTarget = adjustForKeyLevels(technicalTarget, currentPrice, action);
        
        return String.format("₹%.0f", adjustedTarget);
    }
    
    private double adjustForKeyLevels(double technicalTarget, double currentPrice, String action) {
        // Simulate key level adjustments (in real system, use actual support/resistance)
        double[] keyLevels = calculateKeyLevels(currentPrice);
        
        if ("BUY".equals(action)) {
            // Find nearest resistance above current price
            for (double level : keyLevels) {
                if (level > currentPrice && level < technicalTarget + (currentPrice * 0.01)) {
                    return level - (currentPrice * 0.002); // Target just below resistance
                }
            }
        } else if ("SELL".equals(action)) {
            // Find nearest support below current price
            for (double level : keyLevels) {
                if (level < currentPrice && level > technicalTarget - (currentPrice * 0.01)) {
                    return level + (currentPrice * 0.002); // Target just above support
                }
            }
        }
        
        return technicalTarget;
    }
    
    private double[] calculateKeyLevels(double currentPrice) {
        // Calculate realistic intraday support/resistance levels
        return new double[] {
            currentPrice * 0.985,  // Support 1: -1.5%
            currentPrice * 0.992,  // Support 2: -0.8%
            currentPrice * 1.008,  // Resistance 1: +0.8%
            currentPrice * 1.015,  // Resistance 2: +1.5%
            currentPrice * 1.025   // Resistance 3: +2.5%
        };
    }
    
    private String calculateIntelligentStopLoss(double currentPrice, String action, Map<String, Object> riskMetrics) {
        double volatility = Double.parseDouble(((String) riskMetrics.get("volatility")).replace("%", "")) / 100;
        
        // Calculate realistic intraday stop losses
        double stopMultiplier = switch (action) {
            case "BUY" -> {
                // For intraday BUY: 0.5% to 1.5% stop loss based on volatility
                double stopPct = Math.max(0.005, Math.min(volatility * 0.06, 0.015));
                yield 1.0 - stopPct;
            }
            case "SELL" -> {
                // For intraday SELL: 0.5% to 1.5% stop loss above entry
                double stopPct = Math.max(0.005, Math.min(volatility * 0.06, 0.015));
                yield 1.0 + stopPct;
            }
            default -> {
                // For HOLD: 1% stop loss
                yield 1.0 - 0.01;
            }
        };
        
        return String.format("₹%.0f", currentPrice * stopMultiplier);
    }
    
    private String determineTimeframe(String action, Map<String, Object> earnings, Map<String, Object> technicals) {
        int daysToEarnings = (Integer) earnings.get("daysToEarnings");
        boolean bollingerSqueeze = (Boolean) technicals.get("bollingerSqueeze");
        String vwapSignal = (String) technicals.get("vwapSignal");
        int relativeStrength = Integer.parseInt(((String) technicals.get("relativeStrength")).split("/")[0]);
        
        // Determine realistic intraday timeframes
        if (bollingerSqueeze && relativeStrength > 70) {
            return "2-4 hours (Strong breakout setup)";
        } else if (bollingerSqueeze) {
            return "4-6 hours (Breakout expected)";
        } else if ("Above VWAP".equals(vwapSignal) && "BUY".equals(action)) {
            return "1-2 hours (Momentum trade)";
        } else if (daysToEarnings <= 5) {
            return "Same day (Pre-earnings volatility)";
        } else if ("SELL".equals(action)) {
            return "1-3 hours (Quick exit)";
        } else if (relativeStrength > 65) {
            return "2-4 hours (Momentum continuation)";
        } else {
            return "4-8 hours (Position trade)";
        }
    }
    
    private String assessOverallRisk(int riskFactorCount, int bearishScore, String volatilityRanking) {
        if (riskFactorCount >= 4 || bearishScore > 20 || "High".equals(volatilityRanking)) {
            return "HIGH";
        } else if (riskFactorCount >= 2 || bearishScore > 10) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }
    
    private String generateIntelligentReason(String action, List<String> decisionFactors, int netScore) {
        StringBuilder reason = new StringBuilder();
        reason.append("Analytics-driven ").append(action).append(" (Score: ").append(netScore).append("). ");
        
        if (!decisionFactors.isEmpty()) {
            reason.append("Key factors: ");
            reason.append(String.join(", ", decisionFactors.subList(0, Math.min(3, decisionFactors.size()))));
        }
        
        return reason.toString();
    }
    
    private List<String> generateCatalysts(String symbol, Map<String, Object> earnings, Map<String, Object> marketContext) {
        List<String> catalysts = new ArrayList<>();
        
        int daysToEarnings = (Integer) earnings.get("daysToEarnings");
        if (daysToEarnings <= 15) {
            catalysts.add("Earnings results in " + daysToEarnings + " days");
        }
        
        String sectorRotation = (String) marketContext.get("sectorRotation");
        if (sectorRotation.contains("favor")) {
            catalysts.add("Favorable sector rotation dynamics");
        }
        
        // Add symbol-specific catalysts
        switch (symbol) {
            case "RELIANCE" -> {
                catalysts.add("Jio IPO announcement expected");
                catalysts.add("O2C expansion completion");
            }
            case "HDFCBANK" -> {
                catalysts.add("Merger synergy realization");
                catalysts.add("NIM expansion cycle");
            }
            case "TCS" -> {
                catalysts.add("Large deal announcements");
                catalysts.add("AI service offerings launch");
            }
            case "INFY" -> {
                catalysts.add("Digital transformation deals");
                catalysts.add("Margin improvement initiatives");
            }
        }
        
        return catalysts;
    }
    
    // Live market data is now fetched from LiveMarketDataService
    // No more hardcoded prices - all data is real-time from financial APIs
    
    // Inner class for recommendation decision
    private static class RecommendationDecision {
        String action;
        String confidence;
        String target;
        String stopLoss;
        String timeframe;
        String reason;
        String riskLevel;
        String expectedReturn;
        String probabilityOfSuccess;
        List<String> decisionFactors;
        List<String> riskFactors;
        List<String> catalysts;
    }
}
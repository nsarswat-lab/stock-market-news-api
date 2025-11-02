package com.stockmarket.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PortfolioAnalyticsService {
    
    private static final Logger logger = LoggerFactory.getLogger(PortfolioAnalyticsService.class);
    
    public Map<String, Object> getPortfolioRecommendations() {
        logger.debug("📊 Generating portfolio-level recommendations and risk analysis");
        
        Map<String, Object> portfolio = new HashMap<>();
        
        // Portfolio Construction
        portfolio.put("portfolioConstruction", getPortfolioConstruction());
        
        // Risk Management
        portfolio.put("riskManagement", getRiskManagement());
        
        // Sector Allocation
        portfolio.put("sectorAllocation", getSectorAllocation());
        
        // Hedging Strategies
        portfolio.put("hedgingStrategies", getHedgingStrategies());
        
        // Market Timing
        portfolio.put("marketTiming", getMarketTiming());
        
        return portfolio;
    }
    
    private Map<String, Object> getPortfolioConstruction() {
        Map<String, Object> construction = new HashMap<>();
        
        construction.put("recommendedAllocation", Arrays.asList(
            Map.of(
                "asset", "Large Cap Equity",
                "allocation", "60%",
                "rationale", "Core stability with dividend yield",
                "examples", "RELIANCE, HDFCBANK, TCS"
            ),
            Map.of(
                "asset", "Mid Cap Growth",
                "allocation", "25%",
                "rationale", "Growth potential with higher volatility",
                "examples", "Sector leaders in emerging themes"
            ),
            Map.of(
                "asset", "Defensive/Cash",
                "allocation", "15%",
                "rationale", "Liquidity buffer for opportunities",
                "examples", "Liquid funds, short-term debt"
            )
        ));
        
        construction.put("diversificationScore", "85/100 (Well diversified)");
        construction.put("concentrationRisk", "Low - No single stock >8%");
        construction.put("correlationAnalysis", "Average correlation: 0.45 (Acceptable)");
        
        return construction;
    }
    
    private Map<String, Object> getRiskManagement() {
        Map<String, Object> risk = new HashMap<>();
        
        risk.put("portfolioVaR", Map.of(
            "1Day_95pct", "2.1%",
            "1Day_99pct", "3.2%",
            "1Week_95pct", "4.8%",
            "explanation", "Maximum expected loss at given confidence levels"
        ));
        
        risk.put("stressTestScenarios", Arrays.asList(
            Map.of(
                "scenario", "Market Crash (-20%)",
                "portfolioImpact", "-16.5%",
                "worstStock", "Mid-cap growth stocks",
                "bestStock", "Defensive dividend stocks"
            ),
            Map.of(
                "scenario", "Interest Rate Spike (+200bps)",
                "portfolioImpact", "-8.2%",
                "worstStock", "Banking stocks",
                "bestStock", "IT export stocks"
            ),
            Map.of(
                "scenario", "Oil Price Shock (+50%)",
                "portfolioImpact", "+2.1%",
                "worstStock", "Aviation, Auto",
                "bestStock", "Oil & Gas, Refineries"
            )
        ));
        
        risk.put("stopLossLevels", Map.of(
            "portfolioLevel", "15% trailing stop",
            "individualStocks", "8-12% based on volatility",
            "sectorLevel", "20% sector allocation limit"
        ));
        
        return risk;
    }
    
    private Map<String, Object> getSectorAllocation() {
        Map<String, Object> sectors = new HashMap<>();
        
        sectors.put("currentAllocation", Arrays.asList(
            Map.of("sector", "Banking & Financial", "current", "28%", "target", "25%", "action", "Reduce"),
            Map.of("sector", "Information Technology", "current", "22%", "target", "25%", "action", "Increase"),
            Map.of("sector", "Energy & Utilities", "current", "18%", "target", "20%", "action", "Maintain"),
            Map.of("sector", "Consumer Goods", "current", "15%", "target", "15%", "action", "Maintain"),
            Map.of("sector", "Healthcare & Pharma", "current", "8%", "target", "10%", "action", "Increase"),
            Map.of("sector", "Others", "current", "9%", "target", "5%", "action", "Reduce")
        ));
        
        sectors.put("sectorRotationSignals", Arrays.asList(
            "IT sector showing relative strength vs global peers",
            "Banking sector consolidation phase - selective buying",
            "Energy transition creating opportunities in renewables",
            "Healthcare defensive characteristics attractive"
        ));
        
        return sectors;
    }
    
    private Map<String, Object> getHedgingStrategies() {
        Map<String, Object> hedging = new HashMap<>();
        
        hedging.put("recommendedHedges", Arrays.asList(
            Map.of(
                "strategy", "Index Put Options",
                "instrument", "Nifty 50 Put (18,500 strike)",
                "cost", "0.8% of portfolio",
                "protection", "Downside below 18,500",
                "timeframe", "1-3 months"
            ),
            Map.of(
                "strategy", "Currency Hedge",
                "instrument", "USD/INR Forwards",
                "rationale", "Protect IT stock gains from rupee strength",
                "coverage", "50% of IT exposure"
            ),
            Map.of(
                "strategy", "Sector Rotation",
                "instrument", "Banking vs IT pair trade",
                "rationale", "Hedge sector concentration risk",
                "implementation", "Long IT ETF, Short Banking ETF"
            )
        ));
        
        hedging.put("hedgingCost", "1.2% annually (Reasonable for protection)");
        hedging.put("hedgeEffectiveness", "85% correlation in stress scenarios");
        
        return hedging;
    }
    
    private Map<String, Object> getMarketTiming() {
        Map<String, Object> timing = new HashMap<>();
        
        timing.put("marketPhase", Map.of(
            "current", "Late Bull Market",
            "characteristics", "High valuations, strong momentum, increasing volatility",
            "duration", "Estimated 6-12 months remaining",
            "riskLevel", "Elevated"
        ));
        
        timing.put("tacticalSignals", Arrays.asList(
            Map.of(
                "signal", "VIX Level",
                "current", "16.5",
                "interpretation", "Complacency - potential volatility spike ahead",
                "action", "Reduce leverage, increase hedges"
            ),
            Map.of(
                "signal", "FII Flows",
                "current", "₹12,500 Cr net buying (YTD)",
                "interpretation", "Strong but slowing momentum",
                "action", "Monitor for flow reversal"
            ),
            Map.of(
                "signal", "Earnings Yield vs Bond Yield",
                "current", "4.2% vs 7.1%",
                "interpretation", "Equity risk premium compressed",
                "action", "Selective stock picking over index"
            )
        ));
        
        timing.put("rebalancingTriggers", Arrays.asList(
            "Monthly rebalancing if allocation drift >5%",
            "Immediate rebalancing if VIX >25",
            "Quarterly review of sector allocations",
            "Event-driven rebalancing (earnings, policy changes)"
        ));
        
        return timing;
    }
    
    public Map<String, Object> getAlertSystem() {
        Map<String, Object> alerts = new HashMap<>();
        
        alerts.put("riskAlerts", Arrays.asList(
            Map.of(
                "type", "CONCENTRATION_RISK",
                "message", "Banking sector allocation exceeded 30%",
                "severity", "MEDIUM",
                "action", "Consider reducing HDFCBANK position"
            ),
            Map.of(
                "type", "VOLATILITY_SPIKE",
                "message", "Portfolio volatility increased to 18.5%",
                "severity", "HIGH", 
                "action", "Review hedging strategies"
            )
        ));
        
        alerts.put("opportunityAlerts", Arrays.asList(
            Map.of(
                "type", "OVERSOLD_QUALITY",
                "message", "TCS trading below 200-day MA with strong fundamentals",
                "severity", "LOW",
                "action", "Consider accumulation on weakness"
            ),
            Map.of(
                "type", "SECTOR_ROTATION",
                "message", "IT sector showing relative strength breakout",
                "severity", "MEDIUM",
                "action", "Increase IT allocation to target 25%"
            )
        ));
        
        return alerts;
    }
}
package com.stockmarket.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IndianMarketFactorsService {
    
    private static final Logger logger = LoggerFactory.getLogger(IndianMarketFactorsService.class);
    
    public List<Map<String, Object>> getMarketFactors() {
        logger.debug("📊 Analyzing key factors affecting Indian stock market");
        
        return Arrays.asList(
            // Monetary Policy Factors
            Map.of(
                "category", "Monetary Policy",
                "factor", "RBI Repo Rate",
                "current", "6.50%",
                "impact", "NEUTRAL",
                "description", "RBI maintaining status quo, inflation within target range",
                "relevance", "Affects banking sector margins and overall liquidity"
            ),
            
            // Currency & Commodities
            Map.of(
                "category", "Currency",
                "factor", "USD-INR Exchange Rate",
                "current", "83.25",
                "impact", "NEGATIVE",
                "description", "Rupee under pressure due to FII outflows and crude oil imports",
                "relevance", "Impacts IT exporters positively, oil importers negatively"
            ),
            
            Map.of(
                "category", "Commodities",
                "factor", "Crude Oil Prices",
                "current", "USD 85/barrel",
                "impact", "NEGATIVE",
                "description", "Rising crude prices increase India's import bill and inflation",
                "relevance", "Critical for OMCs, airlines, and overall inflation trajectory"
            ),
            
            // Institutional Flows
            Map.of(
                "category", "Institutional Flows",
                "factor", "FII Net Investment",
                "current", "-Rs 15,000 Cr (MTD)",
                "impact", "NEGATIVE",
                "description", "Foreign investors reducing exposure due to global uncertainty",
                "relevance", "Major driver of market sentiment and liquidity"
            ),
            
            Map.of(
                "category", "Institutional Flows", 
                "factor", "DII Net Investment",
                "current", "+Rs 18,000 Cr (MTD)",
                "impact", "POSITIVE",
                "description", "Domestic institutions continue strong buying, offsetting FII outflows",
                "relevance", "Provides market stability and reduces volatility"
            ),
            
            // Economic Indicators
            Map.of(
                "category", "Economic Growth",
                "factor", "GDP Growth Rate",
                "current", "7.2% (Q2 FY24)",
                "impact", "POSITIVE",
                "description", "India remains fastest growing major economy despite global headwinds",
                "relevance", "Supports corporate earnings growth and market valuations"
            ),
            
            Map.of(
                "category", "Inflation",
                "factor", "CPI Inflation",
                "current", "5.8%",
                "impact", "NEUTRAL",
                "description", "Within RBI's tolerance band but above target of 4%",
                "relevance", "Key factor for RBI policy decisions and consumer spending"
            ),
            
            // Sector-Specific Factors
            Map.of(
                "category", "Agriculture",
                "factor", "Monsoon Forecast",
                "current", "Normal (98% of LPA)",
                "impact", "POSITIVE",
                "description", "Good monsoon supports rural demand and agricultural output",
                "relevance", "Critical for FMCG, auto, and rural-focused sectors"
            ),
            
            Map.of(
                "category", "Technology",
                "factor", "Global IT Spending",
                "current", "Cautious",
                "impact", "NEGATIVE",
                "description", "Clients reducing discretionary spending, focus on cost optimization",
                "relevance", "Affects IT services companies' growth and margins"
            ),
            
            // Regulatory & Policy
            Map.of(
                "category", "Policy",
                "factor", "Government Capex",
                "current", "Rs 10 Lakh Cr (FY24)",
                "impact", "POSITIVE",
                "description", "Continued focus on infrastructure development and PLI schemes",
                "relevance", "Benefits capital goods, cement, steel, and infrastructure sectors"
            )
        );
    }
    
    public Map<String, Object> getMarketOutlook() {
        return Map.of(
            "outlook", "CAUTIOUSLY_OPTIMISTIC",
            "timeHorizon", "6-12 months",
            "keyRisks", Arrays.asList(
                "Global recession fears impacting exports",
                "Persistent FII outflows due to China reopening",
                "Geopolitical tensions affecting crude oil prices",
                "Monsoon failure risk for rural demand"
            ),
            "keyOpportunities", Arrays.asList(
                "India's relative economic outperformance",
                "Strong domestic institutional flows",
                "Government's infrastructure push",
                "Corporate earnings recovery in key sectors",
                "Potential for inclusion in global bond indices"
            ),
            "recommendedSectors", Arrays.asList(
                "Banking (post-merger integration)",
                "Infrastructure & Capital Goods",
                "Pharmaceuticals (global demand)",
                "Renewable Energy (policy support)"
            ),
            "sectorsToAvoid", Arrays.asList(
                "IT Services (demand slowdown)",
                "Metals (global growth concerns)",
                "Real Estate (interest rate sensitivity)"
            )
        );
    }
}
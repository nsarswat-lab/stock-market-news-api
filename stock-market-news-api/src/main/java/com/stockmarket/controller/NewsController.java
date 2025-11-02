package com.stockmarket.controller;

import com.stockmarket.service.StockNewsService;
import com.stockmarket.service.IntradayTradingService;
import com.stockmarket.service.LongTermInvestmentService;
import com.stockmarket.service.IndianMarketFactorsService;
import com.stockmarket.service.AdvancedAnalyticsService;
import com.stockmarket.service.PortfolioAnalyticsService;
import com.stockmarket.service.LiveMarketDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class NewsController {
    
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    
    @Autowired
    private StockNewsService stockNewsService;
    

    
    @Autowired
    private IntradayTradingService intradayTradingService;
    
    @Autowired
    private LongTermInvestmentService longTermInvestmentService;
    
    @Autowired
    private IndianMarketFactorsService marketFactorsService;
    
    @Autowired
    private AdvancedAnalyticsService advancedAnalyticsService;
    
    @Autowired
    private PortfolioAnalyticsService portfolioAnalyticsService;
    
    @Autowired
    private LiveMarketDataService liveMarketDataService;
    
    @GetMapping("/news")
    public ResponseEntity<Map<String, Object>> getStockNews() {
        logger.debug("📈 Getting stock news from real API sources");
        
        List<Map<String, Object>> news = stockNewsService.getStockNews();
        
        Map<String, Object> response = Map.of(
            "dataSource", "REAL_API",
            "mockType", "live-data",
            "mockIndicator", "📡 LIVE MARKET DATA",
            "news", news,
            "timestamp", System.currentTimeMillis()
        );
        
        logger.debug("📰 REAL API: Returning {} news items", news.size());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/recommendations/intraday")
    public ResponseEntity<Map<String, Object>> getIntradayRecommendations() {
        logger.debug("⚡ Getting intraday trading recommendations");
        
        List<Map<String, Object>> recommendations = intradayTradingService.getIntradayRecommendations();
        
        Map<String, Object> response = Map.of(
            "dataSource", "INTRADAY_ANALYSIS",
            "mockType", "technical-analysis",
            "mockIndicator", "⚡ INTRADAY TRADING SIGNALS",
            "recommendations", recommendations,
            "tradingStyle", "INTRADAY",
            "timeframe", "Minutes to Hours",
            "timestamp", System.currentTimeMillis()
        );
        
        logger.debug("⚡ INTRADAY: Returning {} recommendations", recommendations.size());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/recommendations/longterm")
    public ResponseEntity<Map<String, Object>> getLongTermRecommendations() {
        logger.debug("📈 Getting long-term investment recommendations");
        
        List<Map<String, Object>> recommendations = longTermInvestmentService.getLongTermRecommendations();
        
        Map<String, Object> response = Map.of(
            "dataSource", "FUNDAMENTAL_ANALYSIS",
            "mockType", "investment-analysis",
            "mockIndicator", "📈 LONG-TERM INVESTMENT ANALYSIS",
            "recommendations", recommendations,
            "tradingStyle", "LONG_TERM",
            "timeframe", "12-36 Months",
            "timestamp", System.currentTimeMillis()
        );
        
        logger.debug("📈 LONG-TERM: Returning {} recommendations", recommendations.size());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/recommendations")
    public ResponseEntity<Map<String, Object>> getAllRecommendations() {
        logger.debug("💡 Getting combined trading and investment recommendations");
        
        List<Map<String, Object>> intradayRecs = intradayTradingService.getIntradayRecommendations();
        List<Map<String, Object>> longTermRecs = longTermInvestmentService.getLongTermRecommendations();
        
        Map<String, Object> response = Map.of(
            "dataSource", "COMBINED_ANALYSIS",
            "mockType", "comprehensive-analysis",
            "mockIndicator", "🎯 COMPREHENSIVE MARKET ANALYSIS",
            "intradayRecommendations", intradayRecs,
            "longTermRecommendations", longTermRecs,
            "timestamp", System.currentTimeMillis()
        );
        
        logger.debug("💡 COMBINED: Returning {} intraday + {} long-term recommendations", 
                    intradayRecs.size(), longTermRecs.size());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/market-factors")
    public ResponseEntity<Map<String, Object>> getIndianMarketFactors() {
        logger.debug("🇮🇳 Getting Indian market factors and analysis");
        
        List<Map<String, Object>> factors = marketFactorsService.getMarketFactors();
        Map<String, Object> outlook = marketFactorsService.getMarketOutlook();
        
        Map<String, Object> response = Map.of(
            "dataSource", "INDIAN_MARKET_ANALYSIS",
            "mockType", "market-intelligence",
            "mockIndicator", "🇮🇳 INDIAN MARKET INTELLIGENCE",
            "factors", factors,
            "outlook", outlook,
            "timestamp", System.currentTimeMillis()
        );
        
        logger.debug("🇮🇳 INDIAN MARKET: Returning {} factors", factors.size());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/analytics/advanced/{symbol}")
    public ResponseEntity<Map<String, Object>> getAdvancedAnalytics(@PathVariable String symbol) {
        logger.debug("🔬 Getting advanced analytics for {}", symbol);
        
        double currentPrice = liveMarketDataService.getCurrentPrice(symbol);
        long volume = liveMarketDataService.getCurrentVolume(symbol);
        Map<String, Object> analytics = advancedAnalyticsService.getAdvancedMetrics(symbol, currentPrice, volume);
        
        Map<String, Object> response = Map.of(
            "dataSource", "ADVANCED_ANALYTICS",
            "mockType", "quantitative-analysis",
            "mockIndicator", "🔬 ADVANCED FINANCIAL ANALYTICS",
            "symbol", symbol,
            "analytics", analytics,
            "timestamp", System.currentTimeMillis()
        );
        
        logger.debug("🔬 ADVANCED: Returning analytics for {}", symbol);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/portfolio/analytics")
    public ResponseEntity<Map<String, Object>> getPortfolioAnalytics() {
        logger.debug("📊 Getting portfolio-level analytics and recommendations");
        
        Map<String, Object> portfolio = portfolioAnalyticsService.getPortfolioRecommendations();
        Map<String, Object> alerts = portfolioAnalyticsService.getAlertSystem();
        
        Map<String, Object> response = Map.of(
            "dataSource", "PORTFOLIO_ANALYTICS",
            "mockType", "portfolio-management",
            "mockIndicator", "📊 PORTFOLIO MANAGEMENT SYSTEM",
            "portfolio", portfolio,
            "alerts", alerts,
            "timestamp", System.currentTimeMillis()
        );
        
        logger.debug("📊 PORTFOLIO: Returning comprehensive portfolio analytics");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/analytics/risk-dashboard")
    public ResponseEntity<Map<String, Object>> getRiskDashboard() {
        logger.debug("⚠️ Getting comprehensive risk dashboard");
        
        Map<String, Object> riskDashboard = new HashMap<>();
        
        // Market Risk Indicators
        riskDashboard.put("marketRisk", Map.of(
            "vixLevel", "16.5 (Complacency Zone)",
            "marketRegime", "Late Bull Market",
            "volatilityTrend", "Increasing",
            "correlationRisk", "Rising cross-asset correlation"
        ));
        
        // Liquidity Risk
        riskDashboard.put("liquidityRisk", Map.of(
            "marketDepth", "Normal but declining",
            "bidAskSpreads", "Widening in mid-caps",
            "volumeProfile", "Concentrated in large caps",
            "liquidityScore", "75/100"
        ));
        
        // Concentration Risk
        riskDashboard.put("concentrationRisk", Map.of(
            "topHoldings", "Top 5 stocks: 45% of portfolio",
            "sectorConcentration", "Banking: 28%, IT: 22%",
            "geographicRisk", "India: 85%, Global: 15%",
            "riskScore", "Medium"
        ));
        
        Map<String, Object> response = Map.of(
            "dataSource", "RISK_MANAGEMENT",
            "mockType", "risk-analytics",
            "mockIndicator", "⚠️ COMPREHENSIVE RISK DASHBOARD",
            "riskDashboard", riskDashboard,
            "timestamp", System.currentTimeMillis()
        );
        
        logger.debug("⚠️ RISK: Returning comprehensive risk dashboard");
        return ResponseEntity.ok(response);
    }
    
    // Removed hardcoded price methods - now using LiveMarketDataService for real-time data
}
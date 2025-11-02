package com.stockmarket.controller;

import com.stockmarket.service.StockNewsService;
import com.stockmarket.service.TradingRecommendationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class NewsController {
    
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    
    @Autowired
    private StockNewsService stockNewsService;
    
    @Autowired
    private TradingRecommendationService recommendationService;
    
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
    
    @GetMapping("/recommendations")
    public ResponseEntity<Map<String, Object>> getIntradayRecommendations() {
        logger.debug("💡 Getting intraday recommendations from real market data");
        
        List<Map<String, Object>> recommendations = recommendationService.getIntradayRecommendations();
        
        Map<String, Object> response = Map.of(
            "dataSource", "REAL_API",
            "mockType", "live-analysis",
            "mockIndicator", "📊 LIVE TRADING ANALYSIS",
            "recommendations", recommendations,
            "timestamp", System.currentTimeMillis()
        );
        
        logger.debug("💡 REAL API: Returning {} recommendations", recommendations.size());
        return ResponseEntity.ok(response);
    }
}
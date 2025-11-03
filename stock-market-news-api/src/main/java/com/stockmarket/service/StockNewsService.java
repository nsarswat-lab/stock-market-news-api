package com.stockmarket.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class StockNewsService {
    
    private static final Logger logger = LoggerFactory.getLogger(StockNewsService.class);
    private final RestTemplate restTemplate;
    
    @Autowired
    private RealTimeNewsService realTimeNewsService;
    
    @Autowired
    private ActualNewsScrapingService actualNewsScrapingService;
    
    @Value("${alphavantage.api.key:demo}")
    private String apiKey;
    
    private static final String ALPHA_VANTAGE_NEWS_URL = 
        "https://www.alphavantage.co/query?function=NEWS_SENTIMENT&tickers={tickers}&apikey={apikey}";
    
    public StockNewsService() {
        this.restTemplate = new RestTemplate();
    }
    
    public List<Map<String, Object>> getStockNews() {
        logger.debug("🔍 Fetching latest stock news from multiple real-time sources");
        
        // First try actual news scraping
        try {
            List<Map<String, Object>> scrapedNews = actualNewsScrapingService.scrapeLatestNews();
            if (scrapedNews != null && !scrapedNews.isEmpty()) {
                logger.info("📰 Successfully scraped {} actual news articles", scrapedNews.size());
                return scrapedNews;
            }
        } catch (Exception e) {
            logger.warn("⚠️ News scraping failed: {}, trying real-time service", e.getMessage());
        }
        
        // Fallback to real-time news service
        try {
            List<Map<String, Object>> realTimeNews = realTimeNewsService.getLatestStockNews();
            if (realTimeNews != null && !realTimeNews.isEmpty()) {
                logger.info("📰 Successfully fetched {} real-time news articles", realTimeNews.size());
                return realTimeNews;
            }
        } catch (Exception e) {
            logger.warn("⚠️ Real-time news service failed: {}, trying Alpha Vantage fallback", e.getMessage());
        }
        
        // Fallback to Alpha Vantage API
        try {
            // Focus on Indian market and global factors affecting India
            String tickers = "RELIANCE.BSE,TCS.BSE,INFY.BSE,HDFCBANK.BSE,ITC.BSE";
            
            Map<String, Object> response = restTemplate.getForObject(
                ALPHA_VANTAGE_NEWS_URL, 
                Map.class, 
                tickers, 
                apiKey
            );
            
            if (response != null) {
                logger.debug("🔍 API Response keys: {}", response.keySet());
                
                // Check if API returned an error or rate limit message
                if (response.containsKey("Information")) {
                    String info = (String) response.get("Information");
                    logger.warn("⚠️ Alpha Vantage API Info: {}", info);
                    logger.debug("🎭 Using fallback due to API limitation");
                    return getFallbackNews();
                }
                
                if (response.containsKey("feed")) {
                    List<Map<String, Object>> feed = (List<Map<String, Object>>) response.get("feed");
                    logger.debug("📰 Found {} articles in feed", feed.size());
                    List<Map<String, Object>> processedNews = processNewsData(feed);
                    
                    // If no news was processed, use fallback
                    if (processedNews.isEmpty()) {
                        logger.warn("⚠️ Alpha Vantage API returned empty processed feed, using fallback");
                        return getFallbackNews();
                    }
                    
                    return processedNews;
                } else {
                    logger.warn("⚠️ Alpha Vantage API response missing 'feed' key. Available keys: {}", response.keySet());
                    return getFallbackNews();
                }
            } else {
                logger.warn("⚠️ Alpha Vantage API returned null response, using fallback");
                return getFallbackNews();
            }
            
        } catch (ResourceAccessException e) {
            logger.warn("⚠️ API call failed (network/timeout), using fallback: {}", e.getMessage());
            return getFallbackNews();
        } catch (Exception e) {
            logger.error("❌ Error fetching news from Alpha Vantage: {}", e.getMessage());
            return getFallbackNews();
        }
    }
    
    private List<Map<String, Object>> processNewsData(List<Map<String, Object>> feed) {
        List<Map<String, Object>> processedNews = new ArrayList<>();
        
        for (int i = 0; i < Math.min(feed.size(), 6); i++) {
            Map<String, Object> article = feed.get(i);
            
            String title = (String) article.get("title");
            String summary = (String) article.get("summary");
            String url = (String) article.get("url");
            List<Map<String, Object>> tickerSentiment = 
                (List<Map<String, Object>>) article.get("ticker_sentiment");
            
            // Extract primary ticker and sentiment
            String symbol = "MARKET";
            String sentiment = "neutral";
            
            if (tickerSentiment != null && !tickerSentiment.isEmpty()) {
                Map<String, Object> primaryTicker = tickerSentiment.get(0);
                symbol = (String) primaryTicker.get("ticker");
                
                String sentimentScore = (String) primaryTicker.get("ticker_sentiment_score");
                if (sentimentScore != null) {
                    double score = Double.parseDouble(sentimentScore);
                    sentiment = score > 0.1 ? "positive" : score < -0.1 ? "negative" : "neutral";
                }
            }
            
            Map<String, Object> newsItem = Map.of(
                "id", String.valueOf(i + 1),
                "symbol", symbol,
                "headline", title != null ? title.substring(0, Math.min(title.length(), 100)) : "Market Update",
                "sentiment", sentiment,
                "source", "alpha-vantage",
                "url", url != null ? url : "https://www.alphavantage.co"
            );
            
            processedNews.add(newsItem);
        }
        
        logger.debug("📰 Processed {} real news articles", processedNews.size());
        return processedNews;
    }
    
    private List<Map<String, Object>> getFallbackNews() {
        logger.debug("🎭 Using enhanced fallback news data (all APIs unavailable)");
        
        // Use the real-time news service fallback which has current timestamps
        try {
            return realTimeNewsService.getLatestStockNews();
        } catch (Exception e) {
            logger.warn("⚠️ Even real-time service fallback failed, using static fallback");
            
            return Arrays.asList(
                Map.of("id", "static-1", "symbol", "NIFTY50", "headline", "Indian equity markets show resilience amid global volatility", "sentiment", "positive", "source", "Market Intelligence", "url", "https://www.nseindia.com", "timestamp", System.currentTimeMillis()),
                Map.of("id", "static-2", "symbol", "RELIANCE", "headline", "Reliance Industries maintains strong operational performance", "sentiment", "positive", "source", "Market Intelligence", "url", "https://www.ril.com", "timestamp", System.currentTimeMillis()),
                Map.of("id", "static-3", "symbol", "TCS", "headline", "IT sector outlook remains positive on digital transformation demand", "sentiment", "positive", "source", "Market Intelligence", "url", "https://www.tcs.com", "timestamp", System.currentTimeMillis()),
                Map.of("id", "static-4", "symbol", "HDFCBANK", "headline", "Banking sector consolidation creates opportunities for leaders", "sentiment", "neutral", "source", "Market Intelligence", "url", "https://www.hdfcbank.com", "timestamp", System.currentTimeMillis()),
                Map.of("id", "static-5", "symbol", "MARKET", "headline", "FII inflows support Indian markets, domestic participation strong", "sentiment", "positive", "source", "Market Intelligence", "url", "https://www.bseindia.com", "timestamp", System.currentTimeMillis())
            );
        }
    }
}
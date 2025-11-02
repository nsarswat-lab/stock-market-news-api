package com.stockmarket.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class StockNewsService {
    
    private static final Logger logger = LoggerFactory.getLogger(StockNewsService.class);
    private final RestTemplate restTemplate;
    
    @Value("${alphavantage.api.key:demo}")
    private String apiKey;
    
    private static final String ALPHA_VANTAGE_NEWS_URL = 
        "https://www.alphavantage.co/query?function=NEWS_SENTIMENT&tickers={tickers}&apikey={apikey}";
    
    public StockNewsService() {
        this.restTemplate = new RestTemplate();
    }
    
    public List<Map<String, Object>> getStockNews() {
        logger.debug("🔍 Fetching real stock news from Alpha Vantage API");
        
        try {
            // Try with just AAPL first to see if we get any data
            String tickers = "AAPL";
            
            Map<String, Object> response = restTemplate.getForObject(
                ALPHA_VANTAGE_NEWS_URL, 
                Map.class, 
                tickers, 
                apiKey
            );
            
            if (response != null) {
                logger.debug("🔍 API Response keys: {}", response.keySet());
                
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
                "source", "alpha-vantage"
            );
            
            processedNews.add(newsItem);
        }
        
        logger.debug("📰 Processed {} real news articles", processedNews.size());
        return processedNews;
    }
    
    private List<Map<String, Object>> getFallbackNews() {
        logger.debug("🎭 Using fallback news data (API unavailable)");
        
        return Arrays.asList(
            Map.of("id", "1", "symbol", "AAPL", "headline", "Apple Stock Shows Strong Performance", "sentiment", "positive", "source", "fallback"),
            Map.of("id", "2", "symbol", "TSLA", "headline", "Tesla Announces New Model Updates", "sentiment", "positive", "source", "fallback"),
            Map.of("id", "3", "symbol", "MSFT", "headline", "Microsoft Cloud Revenue Continues Growth", "sentiment", "positive", "source", "fallback"),
            Map.of("id", "4", "symbol", "GOOGL", "headline", "Google AI Developments Drive Interest", "sentiment", "positive", "source", "fallback"),
            Map.of("id", "5", "symbol", "AMZN", "headline", "Amazon Logistics Expansion Plans", "sentiment", "neutral", "source", "fallback")
        );
    }
}
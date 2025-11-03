package com.stockmarket.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class RealNewsAggregatorService {
    
    private static final Logger logger = LoggerFactory.getLogger(RealNewsAggregatorService.class);
    private final RestTemplate restTemplate;
    
    public RealNewsAggregatorService() {
        this.restTemplate = new RestTemplate();
    }
    
    public List<Map<String, Object>> fetchRealNews() {
        logger.info("🔍 Fetching real news from actual Indian financial news platforms");
        
        List<Map<String, Object>> allNews = new ArrayList<>();
        
        // Fetch from multiple real RSS feeds
        allNews.addAll(fetchMoneyControlRSS());
        allNews.addAll(fetchEconomicTimesRSS());
        allNews.addAll(fetchBusinessStandardRSS());
        allNews.addAll(fetchLiveMintRSS());
        
        // If RSS fails, use NewsAPI as backup
        if (allNews.isEmpty()) {
            logger.warn("⚠️ RSS feeds failed, trying NewsAPI");
            allNews.addAll(fetchFromNewsAPI());
        }
        
        // Process and clean the news
        allNews = processAndFilterNews(allNews);
        
        logger.info("📰 Successfully fetched {} real news articles from platforms", allNews.size());
        return allNews;
    }
    
    private List<Map<String, Object>> fetchMoneyControlRSS() {
        try {
            logger.debug("📡 Fetching from MoneyControl RSS feed");
            
            // MoneyControl's actual RSS feed for market reports
            String rssUrl = "https://www.moneycontrol.com/rss/marketreports.xml";
            String rssContent = restTemplate.getForObject(rssUrl, String.class);
            
            if (rssContent != null) {
                return parseRSSContent(rssContent, "MoneyControl", "https://www.moneycontrol.com");
            }
            
        } catch (Exception e) {
            logger.debug("⚠️ MoneyControl RSS failed: {}", e.getMessage());
        }
        
        return new ArrayList<>();
    }
    
    private List<Map<String, Object>> fetchEconomicTimesRSS() {
        try {
            logger.debug("📡 Fetching from Economic Times RSS feed");
            
            // Economic Times actual RSS feed for markets
            String rssUrl = "https://economictimes.indiatimes.com/markets/rssfeeds/1977021501.cms";
            String rssContent = restTemplate.getForObject(rssUrl, String.class);
            
            if (rssContent != null) {
                return parseRSSContent(rssContent, "Economic Times", "https://economictimes.indiatimes.com");
            }
            
        } catch (Exception e) {
            logger.debug("⚠️ Economic Times RSS failed: {}", e.getMessage());
        }
        
        return new ArrayList<>();
    }
    
    private List<Map<String, Object>> fetchBusinessStandardRSS() {
        try {
            logger.debug("📡 Fetching from Business Standard RSS feed");
            
            // Business Standard actual RSS feed for markets
            String rssUrl = "https://www.business-standard.com/rss/markets-106.rss";
            String rssContent = restTemplate.getForObject(rssUrl, String.class);
            
            if (rssContent != null) {
                return parseRSSContent(rssContent, "Business Standard", "https://www.business-standard.com");
            }
            
        } catch (Exception e) {
            logger.debug("⚠️ Business Standard RSS failed: {}", e.getMessage());
        }
        
        return new ArrayList<>();
    }
    
    private List<Map<String, Object>> fetchLiveMintRSS() {
        try {
            logger.debug("📡 Fetching from LiveMint RSS feed");
            
            // LiveMint actual RSS feed for markets
            String rssUrl = "https://www.livemint.com/rss/markets";
            String rssContent = restTemplate.getForObject(rssUrl, String.class);
            
            if (rssContent != null) {
                return parseRSSContent(rssContent, "LiveMint", "https://www.livemint.com");
            }
            
        } catch (Exception e) {
            logger.debug("⚠️ LiveMint RSS failed: {}", e.getMessage());
        }
        
        return new ArrayList<>();
    }
    
    private List<Map<String, Object>> parseRSSContent(String rssContent, String sourceName, String baseUrl) {
        List<Map<String, Object>> newsItems = new ArrayList<>();
        
        try {
            // Parse RSS XML using regex (in production, use proper XML parser like DOM/SAX)
            Pattern itemPattern = Pattern.compile("<item>(.*?)</item>", Pattern.DOTALL);
            Pattern titlePattern = Pattern.compile("<title><!\\[CDATA\\[(.*?)\\]\\]></title>|<title>(.*?)</title>", Pattern.DOTALL);
            Pattern linkPattern = Pattern.compile("<link>(.*?)</link>", Pattern.DOTALL);
            Pattern descPattern = Pattern.compile("<description><!\\[CDATA\\[(.*?)\\]\\]></description>|<description>(.*?)</description>", Pattern.DOTALL);
            Pattern pubDatePattern = Pattern.compile("<pubDate>(.*?)</pubDate>", Pattern.DOTALL);
            
            Matcher itemMatcher = itemPattern.matcher(rssContent);
            int count = 0;
            
            while (itemMatcher.find() && count < 5) {
                String itemContent = itemMatcher.group(1);
                
                String title = extractWithPattern(titlePattern, itemContent);
                String link = extractWithPattern(linkPattern, itemContent);
                String description = extractWithPattern(descPattern, itemContent);
                String pubDate = extractWithPattern(pubDatePattern, itemContent);
                
                if (title != null && link != null && isStockRelated(title)) {
                    Map<String, Object> newsItem = new HashMap<>();
                    newsItem.put("id", sourceName.toLowerCase().replace(" ", "") + "-" + count);
                    newsItem.put("symbol", extractSymbolFromTitle(title));
                    newsItem.put("headline", cleanText(title));
                    newsItem.put("sentiment", analyzeSentiment(title + " " + (description != null ? description : "")));
                    newsItem.put("source", sourceName);
                    newsItem.put("url", cleanURL(link)); // Clean real article URL from RSS
                    newsItem.put("description", description != null ? cleanText(description).substring(0, Math.min(cleanText(description).length(), 150)) + "..." : "");
                    newsItem.put("publishedAt", pubDate);
                    newsItem.put("timestamp", System.currentTimeMillis());
                    
                    newsItems.add(newsItem);
                    count++;
                    
                    logger.debug("📰 Parsed real news: {} from {}", title, sourceName);
                }
            }
            
        } catch (Exception e) {
            logger.warn("⚠️ Error parsing RSS from {}: {}", sourceName, e.getMessage());
        }
        
        return newsItems;
    }
    
    private List<Map<String, Object>> fetchFromNewsAPI() {
        try {
            logger.debug("📡 Fetching from NewsAPI as backup");
            
            // NewsAPI for Indian business news (free tier)
            String url = "https://newsapi.org/v2/top-headlines?country=in&category=business&pageSize=10";
            
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            if (response != null && response.containsKey("articles")) {
                List<Map<String, Object>> articles = (List<Map<String, Object>>) response.get("articles");
                return processNewsAPIArticles(articles);
            }
            
        } catch (Exception e) {
            logger.debug("⚠️ NewsAPI failed: {}", e.getMessage());
        }
        
        return new ArrayList<>();
    }
    
    private List<Map<String, Object>> processNewsAPIArticles(List<Map<String, Object>> articles) {
        List<Map<String, Object>> processedNews = new ArrayList<>();
        
        for (int i = 0; i < Math.min(articles.size(), 5); i++) {
            Map<String, Object> article = articles.get(i);
            
            String title = (String) article.get("title");
            String url = (String) article.get("url");
            String description = (String) article.get("description");
            Map<String, Object> source = (Map<String, Object>) article.get("source");
            String sourceName = source != null ? (String) source.get("name") : "NewsAPI";
            String publishedAt = (String) article.get("publishedAt");
            
            if (title != null && url != null && isStockRelated(title)) {
                Map<String, Object> newsItem = Map.of(
                    "id", "newsapi-" + i,
                    "symbol", extractSymbolFromTitle(title),
                    "headline", title,
                    "sentiment", analyzeSentiment(title + " " + (description != null ? description : "")),
                    "source", sourceName,
                    "url", url, // Real article URL from NewsAPI
                    "description", description != null ? description.substring(0, Math.min(description.length(), 150)) + "..." : "",
                    "publishedAt", publishedAt,
                    "timestamp", System.currentTimeMillis()
                );
                
                processedNews.add(newsItem);
                logger.debug("📰 Processed NewsAPI article: {} from {}", title, sourceName);
            }
        }
        
        return processedNews;
    }
    
    private String extractWithPattern(Pattern pattern, String content) {
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            // Try CDATA first, then regular content
            String cdataContent = matcher.group(1);
            if (cdataContent != null && !cdataContent.trim().isEmpty()) {
                return cdataContent.trim();
            }
            if (matcher.groupCount() > 1) {
                String regularContent = matcher.group(2);
                if (regularContent != null && !regularContent.trim().isEmpty()) {
                    return regularContent.trim();
                }
            }
        }
        return null;
    }
    
    private String cleanText(String text) {
        if (text == null) return "";
        
        // Remove HTML tags and clean up text
        return text.replaceAll("<[^>]+>", "")
                  .replaceAll("&amp;", "&")
                  .replaceAll("&lt;", "<")
                  .replaceAll("&gt;", ">")
                  .replaceAll("&quot;", "\"")
                  .replaceAll("&#39;", "'")
                  .replaceAll("\\s+", " ")
                  .trim();
    }
    
    private String cleanURL(String url) {
        if (url == null) return "";
        
        // Clean URL by removing CDATA tags and other XML artifacts
        return url.replaceAll("<!\\[CDATA\\[", "")
                  .replaceAll("\\]\\]>", "")
                  .replaceAll("<[^>]+>", "")
                  .replaceAll("&amp;", "&")
                  .replaceAll("&lt;", "<")
                  .replaceAll("&gt;", ">")
                  .trim();
    }
    
    private boolean isStockRelated(String title) {
        if (title == null) return false;
        
        String titleLower = title.toLowerCase();
        String[] stockKeywords = {
            "stock", "market", "nifty", "sensex", "share", "equity", "trading", "investment",
            "reliance", "tcs", "hdfc", "infosys", "bharti", "adani", "itc", "wipro",
            "bse", "nse", "ipo", "earnings", "dividend", "fii", "dii", "mutual fund",
            "rupee", "banking", "finance", "corporate", "quarterly", "results"
        };
        
        for (String keyword : stockKeywords) {
            if (titleLower.contains(keyword)) {
                return true;
            }
        }
        
        return false;
    }
    
    private String extractSymbolFromTitle(String title) {
        if (title == null) return "MARKET";
        
        String titleUpper = title.toUpperCase();
        String[] symbols = {"RELIANCE", "TCS", "HDFC", "INFOSYS", "INFY", "BHARTI", "ADANI", "ITC", "WIPRO"};
        
        for (String symbol : symbols) {
            if (titleUpper.contains(symbol)) {
                return symbol;
            }
        }
        
        // Check for market indices
        if (titleUpper.contains("NIFTY")) return "NIFTY50";
        if (titleUpper.contains("SENSEX")) return "SENSEX";
        
        return "MARKET";
    }
    
    private String analyzeSentiment(String text) {
        if (text == null) return "neutral";
        
        String textLower = text.toLowerCase();
        
        String[] positiveWords = {
            "gain", "rise", "up", "high", "strong", "beat", "win", "growth", "positive",
            "surge", "rally", "boost", "jump", "soar", "climb", "advance", "outperform"
        };
        
        String[] negativeWords = {
            "fall", "drop", "down", "low", "weak", "miss", "loss", "decline", "negative",
            "plunge", "crash", "slump", "tumble", "slide", "retreat", "underperform"
        };
        
        int positiveScore = 0;
        int negativeScore = 0;
        
        for (String word : positiveWords) {
            if (textLower.contains(word)) positiveScore++;
        }
        
        for (String word : negativeWords) {
            if (textLower.contains(word)) negativeScore++;
        }
        
        if (positiveScore > negativeScore) return "positive";
        if (negativeScore > positiveScore) return "negative";
        return "neutral";
    }
    
    private List<Map<String, Object>> processAndFilterNews(List<Map<String, Object>> allNews) {
        // Remove duplicates and filter for quality
        Set<String> seenUrls = new HashSet<>();
        List<Map<String, Object>> uniqueNews = new ArrayList<>();
        
        for (Map<String, Object> news : allNews) {
            String url = (String) news.get("url");
            String headline = (String) news.get("headline");
            
            if (url != null && headline != null && !seenUrls.contains(url) && isQualityNews(headline)) {
                seenUrls.add(url);
                uniqueNews.add(news);
            }
        }
        
        // Sort by timestamp (most recent first) and limit to 8 items
        uniqueNews.sort((a, b) -> {
            Long timeA = (Long) a.getOrDefault("timestamp", 0L);
            Long timeB = (Long) b.getOrDefault("timestamp", 0L);
            return timeB.compareTo(timeA);
        });
        
        return uniqueNews.subList(0, Math.min(uniqueNews.size(), 8));
    }
    
    private boolean isQualityNews(String headline) {
        if (headline == null || headline.length() < 20) return false;
        
        // Filter out low-quality headlines
        String headlineLower = headline.toLowerCase();
        String[] excludeKeywords = {"click here", "watch video", "breaking:", "live:", "advertisement"};
        
        for (String exclude : excludeKeywords) {
            if (headlineLower.contains(exclude)) {
                return false;
            }
        }
        
        return true;
    }
}
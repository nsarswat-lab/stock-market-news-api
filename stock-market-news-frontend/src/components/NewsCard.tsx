import React from 'react';
import { NewsItem } from '../types/api';

interface NewsCardProps {
  news: NewsItem;
}

const NewsCard: React.FC<NewsCardProps> = ({ news }) => {
  const getSentimentEmoji = (sentiment: string) => {
    switch (sentiment) {
      case 'positive': return '📈';
      case 'negative': return '📉';
      default: return '➡️';
    }
  };

  const getSentimentClass = (sentiment: string) => {
    switch (sentiment) {
      case 'positive': return 'sentiment-positive';
      case 'negative': return 'sentiment-negative';
      default: return 'sentiment-neutral';
    }
  };

  return (
    <div className="news-card">
      <div className="news-header">
        <span className="stock-symbol">{news.symbol}</span>
        <span className={`sentiment ${getSentimentClass(news.sentiment)}`}>
          {getSentimentEmoji(news.sentiment)} {news.sentiment}
        </span>
      </div>
      <h3 className="news-headline">{news.headline}</h3>
      <div className="news-footer">
        <span className="news-source">📰 {news.source}</span>
      </div>
    </div>
  );
};

export default NewsCard;
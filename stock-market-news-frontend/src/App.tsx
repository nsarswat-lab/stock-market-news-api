import React, { useState, useEffect } from 'react';
import './App.css';
import { NewsItem, Recommendation, ApiResponse } from './types/api';
import NewsCard from './components/NewsCard';
import RecommendationCard from './components/RecommendationCard';
import LoadingSpinner from './components/LoadingSpinner';

const API_BASE_URL = 'http://localhost:8080/api/v1';

function App() {
  const [news, setNews] = useState<NewsItem[]>([]);
  const [recommendations, setRecommendations] = useState<Recommendation[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchNews = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/news`);
      if (!response.ok) throw new Error('Failed to fetch news');
      const data: ApiResponse<NewsItem[]> = await response.json();
      setNews(data.news || []);
    } catch (err) {
      console.error('Error fetching news:', err);
      setError('Failed to load news');
    }
  };

  const fetchRecommendations = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/recommendations`);
      if (!response.ok) throw new Error('Failed to fetch recommendations');
      const data: ApiResponse<Recommendation[]> = await response.json();
      setRecommendations(data.recommendations || []);
    } catch (err) {
      console.error('Error fetching recommendations:', err);
      setError('Failed to load recommendations');
    }
  };

  const refreshData = async () => {
    setLoading(true);
    setError(null);
    try {
      await Promise.all([fetchNews(), fetchRecommendations()]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    refreshData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  if (loading) {
    return <LoadingSpinner />;
  }

  return (
    <div className="app">
      <header className="app-header">
        <h1>📈 Stock Market News & Recommendations</h1>
        <button onClick={refreshData} className="refresh-btn">
          🔄 Refresh Data
        </button>
      </header>

      {error && (
        <div className="error-banner">
          ⚠️ {error}
        </div>
      )}

      <main className="app-main">
        <section className="news-section">
          <h2>📰 Latest Stock News</h2>
          <div className="cards-grid">
            {news.length > 0 ? (
              news.map((item) => (
                <NewsCard key={item.id} news={item} />
              ))
            ) : (
              <p className="no-data">No news available</p>
            )}
          </div>
        </section>

        <section className="recommendations-section">
          <h2>💡 Trading Recommendations</h2>
          <div className="cards-grid">
            {recommendations.length > 0 ? (
              recommendations.map((rec, index) => (
                <RecommendationCard key={`${rec.symbol}-${index}`} recommendation={rec} />
              ))
            ) : (
              <p className="no-data">No recommendations available</p>
            )}
          </div>
        </section>
      </main>
    </div>
  );
}

export default App;

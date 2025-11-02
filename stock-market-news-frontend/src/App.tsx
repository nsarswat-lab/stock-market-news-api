import React, { useState, useEffect } from 'react';
import './App.css';
import { NewsItem, Recommendation, ApiResponse } from './types/api';
import NewsCard from './components/NewsCard';
import RecommendationCard from './components/RecommendationCard';
import LoadingSpinner from './components/LoadingSpinner';

const API_BASE_URL = 'http://localhost:8080/api/v1';

function App() {
  const [news, setNews] = useState<NewsItem[]>([]);
  const [intradayRecommendations, setIntradayRecommendations] = useState<Recommendation[]>([]);
  const [longTermRecommendations, setLongTermRecommendations] = useState<Recommendation[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [activeTab, setActiveTab] = useState<'intraday' | 'longterm'>('intraday');

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

  const fetchIntradayRecommendations = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/recommendations/intraday`);
      if (!response.ok) throw new Error('Failed to fetch intraday recommendations');
      const data: ApiResponse<Recommendation[]> = await response.json();
      setIntradayRecommendations(data.recommendations || []);
    } catch (err) {
      console.error('Error fetching intraday recommendations:', err);
      setError('Failed to load intraday recommendations');
    }
  };

  const fetchLongTermRecommendations = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/recommendations/longterm`);
      if (!response.ok) throw new Error('Failed to fetch long-term recommendations');
      const data: ApiResponse<Recommendation[]> = await response.json();
      setLongTermRecommendations(data.recommendations || []);
    } catch (err) {
      console.error('Error fetching long-term recommendations:', err);
      setError('Failed to load long-term recommendations');
    }
  };

  const refreshData = async () => {
    setLoading(true);
    setError(null);
    try {
      await Promise.all([fetchNews(), fetchIntradayRecommendations(), fetchLongTermRecommendations()]);
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
          <div className="recommendations-header">
            <h2>💡 Trading & Investment Recommendations</h2>
            <div className="tab-buttons">
              <button 
                className={`tab-btn ${activeTab === 'intraday' ? 'active' : ''}`}
                onClick={() => setActiveTab('intraday')}
              >
                ⚡ Intraday Trading
              </button>
              <button 
                className={`tab-btn ${activeTab === 'longterm' ? 'active' : ''}`}
                onClick={() => setActiveTab('longterm')}
              >
                📈 Long-term Investment
              </button>
            </div>
          </div>
          
          <div className="tab-content">
            {activeTab === 'intraday' && (
              <div className="cards-grid">
                {intradayRecommendations.length > 0 ? (
                  intradayRecommendations.map((rec, index) => (
                    <RecommendationCard 
                      key={`intraday-${rec.symbol}-${index}`} 
                      recommendation={rec} 
                      type="intraday"
                    />
                  ))
                ) : (
                  <p className="no-data">No intraday recommendations available</p>
                )}
              </div>
            )}
            
            {activeTab === 'longterm' && (
              <div className="cards-grid">
                {longTermRecommendations.length > 0 ? (
                  longTermRecommendations.map((rec, index) => (
                    <RecommendationCard 
                      key={`longterm-${rec.symbol}-${index}`} 
                      recommendation={rec} 
                      type="longterm"
                    />
                  ))
                ) : (
                  <p className="no-data">No long-term recommendations available</p>
                )}
              </div>
            )}
          </div>
        </section>
      </main>
    </div>
  );
}

export default App;

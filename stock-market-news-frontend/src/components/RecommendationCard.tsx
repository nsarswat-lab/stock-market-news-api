import React from 'react';
import { Recommendation } from '../types/api';

interface RecommendationCardProps {
  recommendation: Recommendation;
  type?: 'intraday' | 'longterm';
}

const RecommendationCard: React.FC<RecommendationCardProps> = ({ recommendation, type = 'intraday' }) => {
  const getActionEmoji = (action: string) => {
    switch (action) {
      case 'BUY': return '🟢';
      case 'SELL': return '🔴';
      case 'HOLD': return '🟡';
      case 'AVOID': return '⚫';
      default: return '⚪';
    }
  };

  const getActionClass = (action: string) => {
    switch (action) {
      case 'BUY': return 'action-buy';
      case 'SELL': return 'action-sell';
      case 'HOLD': return 'action-hold';
      case 'AVOID': return 'action-avoid';
      default: return 'action-neutral';
    }
  };

  const getConfidenceClass = (confidence: string) => {
    switch (confidence) {
      case 'HIGH': return 'confidence-high';
      case 'MEDIUM': return 'confidence-medium';
      case 'LOW': return 'confidence-low';
      default: return 'confidence-neutral';
    }
  };

  return (
    <div className={`recommendation-card ${type}-card`}>
      <div className="recommendation-header">
        <span className="stock-symbol">{recommendation.symbol}</span>
        <span className={`action ${getActionClass(recommendation.action)}`}>
          {getActionEmoji(recommendation.action)} {recommendation.action}
        </span>
      </div>
      
      <div className="recommendation-details">
        <div className="target-price">
          🎯 Target: {recommendation.target}
        </div>
        <div className={`confidence ${getConfidenceClass(recommendation.confidence)}`}>
          📊 Confidence: {recommendation.confidence}
        </div>
      </div>

      {recommendation.timeframe && (
        <div className="timeframe">
          ⏱️ Timeframe: {recommendation.timeframe}
        </div>
      )}

      {recommendation.stopLoss && (
        <div className="stop-loss">
          🛑 Stop Loss: {recommendation.stopLoss}
        </div>
      )}

      {recommendation.currentPrice && recommendation.upside && (
        <div className="price-info">
          <span>💰 Current: {recommendation.currentPrice}</span>
          <span className="upside">📈 Upside: {recommendation.upside}</span>
        </div>
      )}

      <div className="recommendation-reason">
        💭 {recommendation.reason}
      </div>

      {type === 'intraday' && recommendation.technicals && (
        <div className="technicals">
          <h4>📊 Technical Analysis</h4>
          <div className="technical-grid">
            {recommendation.technicals.rsi && (
              <div className="tech-item">RSI: {recommendation.technicals.rsi}</div>
            )}
            {recommendation.technicals.macd && (
              <div className="tech-item">MACD: {recommendation.technicals.macd}</div>
            )}
            {recommendation.technicals.volume && (
              <div className="tech-item">Volume: {recommendation.technicals.volume}</div>
            )}
            {recommendation.technicals.support && (
              <div className="tech-item">Support: {recommendation.technicals.support}</div>
            )}
            {recommendation.technicals.resistance && (
              <div className="tech-item">Resistance: {recommendation.technicals.resistance}</div>
            )}
          </div>
        </div>
      )}

      {type === 'intraday' && recommendation.institutionalData && (
        <div className="institutional-activity">
          <h4>🏛️ Institutional Activity</h4>
          <div className="institutional-grid">
            {recommendation.institutionalData.bigBullIndicator && (
              <div className={`institutional-item ${recommendation.institutionalData.bigBullIndicator.toLowerCase()}`}>
                🐂 Big Bulls: {recommendation.institutionalData.bigBullIndicator}
              </div>
            )}
            {recommendation.institutionalData.fiiActivity && (
              <div className="institutional-item">
                🌍 FII: {recommendation.institutionalData.fiiActivity}
              </div>
            )}
            {recommendation.institutionalData.diiActivity && (
              <div className="institutional-item">
                🏦 DII: {recommendation.institutionalData.diiActivity}
              </div>
            )}
            {recommendation.institutionalData.volumeSpike && (
              <div className="institutional-item">
                📈 Volume: {recommendation.institutionalData.volumeSpike}
              </div>
            )}
            {recommendation.institutionalData.blockDeals && (
              <div className="institutional-item">
                🎯 Block Deals: {recommendation.institutionalData.blockDeals}
              </div>
            )}
          </div>
          {recommendation.marketSentiment && (
            <div className={`market-sentiment ${recommendation.marketSentiment.toLowerCase()}`}>
              📊 Market Sentiment: {recommendation.marketSentiment.replace('_', ' ')}
            </div>
          )}
        </div>
      )}

      {type === 'longterm' && recommendation.fundamentals && (
        <div className="fundamentals">
          <h4>📈 Fundamentals</h4>
          <div className="fundamental-grid">
            {recommendation.fundamentals.pe && (
              <div className="fund-item">P/E: {recommendation.fundamentals.pe}</div>
            )}
            {recommendation.fundamentals.roe && (
              <div className="fund-item">ROE: {recommendation.fundamentals.roe}</div>
            )}
            {recommendation.fundamentals.debtToEquity && (
              <div className="fund-item">D/E: {recommendation.fundamentals.debtToEquity}</div>
            )}
            {recommendation.fundamentals.marketCap && (
              <div className="fund-item">Market Cap: {recommendation.fundamentals.marketCap}</div>
            )}
          </div>
        </div>
      )}

      {type === 'longterm' && recommendation.institutionalData && (
        <div className="institutional-holdings">
          <h4>🏛️ Institutional Holdings</h4>
          <div className="holdings-grid">
            {recommendation.institutionalData.institutionalHolding && (
              <div className="holding-item">
                📊 Total: {recommendation.institutionalData.institutionalHolding}
              </div>
            )}
            {recommendation.institutionalData.fiiHolding && (
              <div className="holding-item">
                🌍 FII: {recommendation.institutionalData.fiiHolding}
              </div>
            )}
            {recommendation.institutionalData.diiHolding && (
              <div className="holding-item">
                🏦 DII: {recommendation.institutionalData.diiHolding}
              </div>
            )}
            {recommendation.smartMoneyScore && (
              <div className={`smart-money-score grade-${recommendation.smartMoneyScore.toLowerCase().replace('+', 'plus')}`}>
                🎯 Smart Money: {recommendation.smartMoneyScore}
              </div>
            )}
          </div>
          {recommendation.institutionalData.bigInvestors && (
            <div className="big-investors">
              💼 Key Investors: {recommendation.institutionalData.bigInvestors}
            </div>
          )}
          {recommendation.institutionalData.recentActivity && (
            <div className="recent-activity">
              📈 Recent: {recommendation.institutionalData.recentActivity}
            </div>
          )}
        </div>
      )}

      {type === 'longterm' && recommendation.catalysts && recommendation.catalysts.length > 0 && (
        <div className="catalysts">
          <h4>🚀 Key Catalysts</h4>
          <ul>
            {recommendation.catalysts.slice(0, 3).map((catalyst, index) => (
              <li key={index}>{catalyst}</li>
            ))}
          </ul>
        </div>
      )}

      {recommendation.advancedAnalytics && (
        <div className="advanced-analytics">
          <h4>🔬 Advanced Analytics</h4>
          
          {recommendation.advancedAnalytics.riskMetrics && (
            <div className="risk-metrics">
              <h5>⚠️ Risk Metrics</h5>
              <div className="metrics-grid">
                {recommendation.advancedAnalytics.riskMetrics.sharpeRatio && (
                  <div className="metric-item">
                    Sharpe: {recommendation.advancedAnalytics.riskMetrics.sharpeRatio}
                  </div>
                )}
                {recommendation.advancedAnalytics.riskMetrics.beta && (
                  <div className="metric-item">
                    Beta: {recommendation.advancedAnalytics.riskMetrics.beta}
                  </div>
                )}
                {recommendation.advancedAnalytics.riskMetrics.volatility && (
                  <div className="metric-item">
                    Vol: {recommendation.advancedAnalytics.riskMetrics.volatility}
                  </div>
                )}
              </div>
            </div>
          )}

          {recommendation.advancedAnalytics.technicalIndicators && (
            <div className="technical-advanced">
              <h5>📊 Advanced Technicals</h5>
              <div className="metrics-grid">
                {recommendation.advancedAnalytics.technicalIndicators.vwap && (
                  <div className="metric-item">
                    VWAP: {recommendation.advancedAnalytics.technicalIndicators.vwap}
                  </div>
                )}
                {recommendation.advancedAnalytics.technicalIndicators.relativeStrength && (
                  <div className="metric-item">
                    RS: {recommendation.advancedAnalytics.technicalIndicators.relativeStrength}
                  </div>
                )}
              </div>
            </div>
          )}

          {recommendation.advancedAnalytics.optionsAnalysis && (
            <div className="options-analysis">
              <h5>📈 Options Flow</h5>
              <div className="metrics-grid">
                {recommendation.advancedAnalytics.optionsAnalysis.putCallRatio && (
                  <div className="metric-item">
                    P/C: {recommendation.advancedAnalytics.optionsAnalysis.putCallRatio}
                  </div>
                )}
                {recommendation.advancedAnalytics.optionsAnalysis.impliedVolatility && (
                  <div className="metric-item">
                    IV: {recommendation.advancedAnalytics.optionsAnalysis.impliedVolatility}
                  </div>
                )}
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default RecommendationCard;
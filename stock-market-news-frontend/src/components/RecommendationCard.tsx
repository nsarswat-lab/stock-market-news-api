import React from 'react';
import { Recommendation } from '../types/api';

interface RecommendationCardProps {
  recommendation: Recommendation;
}

const RecommendationCard: React.FC<RecommendationCardProps> = ({ recommendation }) => {
  const getActionEmoji = (action: string) => {
    switch (action) {
      case 'BUY': return '🟢';
      case 'SELL': return '🔴';
      case 'HOLD': return '🟡';
      default: return '⚪';
    }
  };

  const getActionClass = (action: string) => {
    switch (action) {
      case 'BUY': return 'action-buy';
      case 'SELL': return 'action-sell';
      case 'HOLD': return 'action-hold';
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
    <div className="recommendation-card">
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
      <div className="recommendation-reason">
        💭 {recommendation.reason}
      </div>
    </div>
  );
};

export default RecommendationCard;
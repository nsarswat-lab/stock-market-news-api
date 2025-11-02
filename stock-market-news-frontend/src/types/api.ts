export interface NewsItem {
  id: string;
  symbol: string;
  headline: string;
  sentiment: 'positive' | 'negative' | 'neutral';
  source: string;
  url: string;
}

export interface Recommendation {
  symbol: string;
  action: 'BUY' | 'SELL' | 'HOLD';
  confidence: 'HIGH' | 'MEDIUM' | 'LOW';
  target: string;
  reason: string;
}

export interface ApiResponse<T> {
  dataSource: string;
  mockType: string;
  mockIndicator: string;
  news?: T;
  recommendations?: T;
  timestamp: number;
}
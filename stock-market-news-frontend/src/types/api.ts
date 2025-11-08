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
  action: 'BUY' | 'SELL' | 'HOLD' | 'AVOID';
  confidence: 'HIGH' | 'MEDIUM' | 'LOW';
  target: string;
  reason: string;
  timeframe?: string;
  stopLoss?: string;
  currentPrice?: string | number;
  changePercent?: string | number;
  dayHigh?: string | number;
  dayLow?: string | number;
  previousClose?: string | number;
  currentVolume?: string | number;
  upside?: string;
  technicals?: {
    rsi?: string;
    macd?: string;
    volume?: string;
    support?: string;
    resistance?: string;
  };
  fundamentals?: {
    pe?: string;
    roe?: string;
    debtToEquity?: string;
    marketCap?: string;
  };
  catalysts?: string[];
  institutionalData?: {
    institutionalActivity?: string;
    retailSentiment?: string;
    massActivity?: string;
    bigBullIndicator?: string;
    fiiActivity?: string;
    diiActivity?: string;
    volumeSpike?: string;
    blockDeals?: string;
    institutionalHolding?: string;
    fiiHolding?: string;
    diiHolding?: string;
    promoterHolding?: string;
    pledgedShares?: string;
    bigInvestors?: string;
    recentActivity?: string;
    analystCoverage?: string;
    institutionalSentiment?: string;
    smartMoneyFlow?: string;
  };
  marketSentiment?: string;
  riskLevel?: string;
  smartMoneyScore?: string;
  investmentGrade?: string;
  advancedAnalytics?: {
    riskMetrics?: any;
    technicalIndicators?: any;
    marketContext?: any;
    earningsIntelligence?: any;
    liquidityMetrics?: any;
    optionsAnalysis?: any;
  };
}

export interface AdvancedAnalytics {
  symbol: string;
  analytics: {
    riskMetrics: any;
    technicalIndicators: any;
    marketContext: any;
    earningsIntelligence: any;
    liquidityMetrics: any;
    optionsAnalysis: any;
  };
}

export interface PortfolioAnalytics {
  portfolio: {
    portfolioConstruction: any;
    riskManagement: any;
    sectorAllocation: any;
    hedgingStrategies: any;
    marketTiming: any;
  };
  alerts: {
    riskAlerts: any[];
    opportunityAlerts: any[];
  };
}

export interface ApiResponse<T> {
  dataSource: string;
  mockType: string;
  mockIndicator: string;
  news?: T;
  recommendations?: T;
  intradayRecommendations?: T;
  longTermRecommendations?: T;
  tradingStyle?: string;
  timeframe?: string;
  timestamp: number;
}
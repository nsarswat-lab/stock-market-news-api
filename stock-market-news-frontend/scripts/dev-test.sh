#!/bin/bash
echo "🧪 Testing Frontend API Integration..."
FRONTEND_URL="http://localhost:3000"
API_URL="http://localhost:8080/api/v1"

echo
echo "📊 Testing API endpoints that frontend will consume:"
echo

echo "📰 Testing News API:"
curl -s "$API_URL/news" | jq .
echo

echo "💡 Testing Recommendations API:"
curl -s "$API_URL/recommendations" | jq .
echo

echo "✅ Frontend should be available at: $FRONTEND_URL"
echo "🔗 Make sure backend is running on port 8080"
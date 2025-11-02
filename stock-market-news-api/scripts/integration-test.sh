#!/bin/bash
echo "🧪 Running Stock Market News API Integration Tests..."
BASE_URL="http://localhost:8080"

echo
echo "✅ Testing Health Endpoint:"
curl -s "$BASE_URL/actuator/health"
echo

echo
echo "✅ Testing Stock News Endpoint:"
if curl -s "$BASE_URL/api/v1/news" | grep -q "BACKEND_MOCK"; then
    echo "✅ News endpoint working with mock data"
else
    echo "❌ News endpoint test failed"
fi

echo
echo "✅ Testing Recommendations Endpoint:"
if curl -s "$BASE_URL/api/v1/recommendations" | grep -q "BACKEND_MOCK"; then
    echo "✅ Recommendations endpoint working with mock data"
else
    echo "❌ Recommendations endpoint test failed"
fi

echo
echo "🎯 Integration test completed!"
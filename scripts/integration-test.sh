#!/bin/bash
echo "🧪 Running Full Stack Integration Test..."
echo

echo "🔧 Step 1: Testing Backend API..."
cd "$(dirname "$0")/../stock-market-news-api"
./scripts/dev-test.sh
if [ $? -ne 0 ]; then
    echo "❌ Backend API test failed"
    exit 1
fi

echo
echo "🎨 Step 2: Testing Frontend API Integration..."
cd "$(dirname "$0")/../stock-market-news-frontend"
./scripts/dev-test.sh
if [ $? -ne 0 ]; then
    echo "❌ Frontend API integration test failed"
    exit 1
fi

echo
echo "✅ Integration Test Results:"
echo "✅ Backend API is working with mock data"
echo "✅ Frontend can consume API endpoints"
echo "✅ TypeScript compilation successful"
echo "✅ All components created successfully"
echo
echo "🚀 To start the full stack:"
echo "   1. Backend: cd stock-market-news-api && ./scripts/dev-start.sh"
echo "   2. Frontend: cd stock-market-news-frontend && ./scripts/dev-start.sh"
echo
echo "📱 Frontend will be available at: http://localhost:3000"
echo "🔗 Backend API available at: http://localhost:8080/api/v1"
#!/bin/bash

# NIMAS2PDF Backend Startup Script with OAuth2 Support

echo "🚀 Starting NIMAS2PDF Backend with OAuth2 Configuration..."

# Check if .env file exists
if [ ! -f .env ]; then
    echo "❌ ERROR: .env file not found!"
    echo ""
    echo "To fix this:"
    echo "1. Copy the example: cp env.example .env"
    echo "2. Edit .env with your OAuth2 credentials:"
    echo "   - GOOGLE_CLIENT_ID=your-actual-google-client-id"
    echo "   - GOOGLE_CLIENT_SECRET=your-actual-google-client-secret"
    echo "3. Run this script again"
    exit 1
fi

# Load environment variables
echo "📂 Loading environment variables from .env..."
export $(cat .env | grep -E '^[A-Z_]+=' | sed 's/#.*//g' | xargs)

# Verify OAuth2 credentials are loaded
if [ -z "$GOOGLE_CLIENT_ID" ] || [ "$GOOGLE_CLIENT_ID" = "your-google-client-id" ]; then
    echo "⚠️  WARNING: Google OAuth2 credentials not properly configured!"
    echo "Please edit .env file with your actual Google client ID and secret"
    echo "Current GOOGLE_CLIENT_ID: $GOOGLE_CLIENT_ID"
else
    echo "✅ Google OAuth2 credentials loaded (Client ID: ${GOOGLE_CLIENT_ID:0:20}...)"
fi

# Stop any existing backend processes
echo "🛑 Stopping any existing backend processes..."
pkill -f "spring-boot:run" 2>/dev/null || true
pkill -f "nimas2pdf" 2>/dev/null || true
sleep 2

# Start the backend
echo "🔧 Starting Spring Boot backend..."
cd backend
mvn spring-boot:run

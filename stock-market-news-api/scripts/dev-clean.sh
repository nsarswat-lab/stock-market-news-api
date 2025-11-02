#!/bin/bash
echo "🧹 Cleaning up..."
pkill -f "java.*spring-boot" || echo "No Java processes to kill"
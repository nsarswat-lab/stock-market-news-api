#!/bin/bash
echo "🐛 Starting Stock Market News API in DEBUG mode..."
./scripts/dev-clean.sh
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
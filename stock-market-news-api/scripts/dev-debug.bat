@echo off
echo 🐛 Starting Stock Market News API in DEBUG mode...
call scripts\dev-clean.bat
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
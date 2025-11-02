# Simple Spring Boot API Setup Template

## Step 1: Minimal Configuration with Debug Support
```yaml
# application.yml
server:
  port: 8080

spring:
  application:
    name: simple-api

# Debug-friendly logging
logging:
  level:
    com.yourpackage: DEBUG
    org.springframework.web: DEBUG
  pattern:
    console: "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"

# Enable debug endpoints
management:
  endpoints:
    web:
      exposure:
        include: health,info,loggers
```

## Step 2: REST Controller with Clear Mock Data Indicators
```java
@RestController
@RequestMapping("/api/v1")
public class ItemController {
    
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);
    
    @GetMapping("/items")
    public ResponseEntity<Map<String, Object>> getItems() {
        logger.debug("🔍 Getting all items - using BACKEND MOCK data");
        
        Map<String, Object> response = Map.of(
            "dataSource", "BACKEND_MOCK",
            "mockType", "controller-generated",
            "mockIndicator", "🎭 BACKEND MOCK DATA - NOT REAL",
            "items", Arrays.asList(
                Map.of("id", "1", "name", "Backend Mock Item 1", "source", "backend-mock"),
                Map.of("id", "2", "name", "Backend Mock Item 2", "source", "backend-mock")
            ),
            "timestamp", System.currentTimeMillis()
        );
        
        logger.debug("🎭 BACKEND MOCK: Returning {} items", 
                    ((List<?>) response.get("items")).size());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/items/{id}")
    public ResponseEntity<Map<String, Object>> getItem(@PathVariable String id) {
        logger.debug("🔍 Getting item with id: {} - using BACKEND MOCK data", id);
        
        Map<String, Object> response = Map.of(
            "dataSource", "BACKEND_MOCK",
            "mockType", "controller-generated",
            "mockIndicator", "🎭 BACKEND MOCK DATA - NOT REAL",
            "item", Map.of(
                "id", id, 
                "name", "Backend Mock Item " + id, 
                "source", "backend-mock"
            ),
            "timestamp", System.currentTimeMillis()
        );
        
        logger.debug("🎭 BACKEND MOCK: Returning item for id: {}", id);
        return ResponseEntity.ok(response);
    }
}
```

## Step 3: Cross-Platform Development Scripts

### Bash Scripts (Linux/Mac)
```bash
# scripts/dev-start.sh
#!/bin/bash
echo "🚀 Starting Simple API..."
./scripts/dev-clean.sh
mvn spring-boot:run

# scripts/dev-debug.sh
#!/bin/bash
echo "🐛 Starting API in DEBUG mode..."
./scripts/dev-clean.sh
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

# scripts/dev-clean.sh  
#!/bin/bash
echo "🧹 Cleaning up..."
taskkill /F /IM java.exe 2>/dev/null || true

# scripts/dev-test.sh
#!/bin/bash
echo "🧪 Testing API..."
curl http://localhost:8080/api/v1/items
```

### Windows Batch Scripts (Windows)
```batch
REM scripts/dev-start.bat
@echo off
echo 🚀 Starting Simple API...
call scripts\dev-clean.bat
mvn spring-boot:run

REM scripts/dev-debug.bat
@echo off
echo 🐛 Starting API in DEBUG mode...
call scripts\dev-clean.bat
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

REM scripts/dev-clean.bat
@echo off
echo 🧹 Cleaning up...
taskkill /F /IM java.exe 2>nul || echo No Java processes to kill

REM scripts/dev-test.bat
@echo off
set BASE_URL=http://localhost:8080
curl -s "%BASE_URL%/api/v1/items"
```

## Step 4: Basic POM (Spring Boot Web only)
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

## Step 5: VS Code Debug Configuration (.vscode/launch.json)
```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Debug Simple API",
            "request": "attach",
            "hostName": "localhost",
            "port": "5005"
        },
        {
            "type": "java",
            "name": "Launch Spring Boot",
            "request": "launch",
            "mainClass": "com.yourpackage.Application",
            "projectName": "your-project"
        }
    ]
}
```

## Debug Workflow
### Linux/Mac:
1. Run `./scripts/dev-debug.sh` to start API with debug port
2. Test with `./scripts/dev-test.sh`

### Windows:
1. Run `scripts\dev-debug.bat` to start API with debug port  
2. Test with `scripts\dev-test.bat`

### VS Code Debug (All Platforms):
1. In VS Code: F5 → Select "Debug Simple API"
2. Set breakpoints in your controller methods
3. Debug logs will show in console with 🔍 emojis

## Task Completion Workflow (MANDATORY)
### Linux/Mac:
1. Run `./scripts/integration-test.sh` before marking any task complete

### Windows:
1. Run `scripts\integration-test.bat` before marking any task complete

### All Platforms:
2. Show test results to user
3. Ask: "✅ Integration test passed. Are you satisfied with this implementation?"
4. Wait for user confirmation
5. Only mark task complete after user approval
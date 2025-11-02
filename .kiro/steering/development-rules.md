# Development Rules and Instructions for Kiro

## Project Architecture Principles

### Simple API-First Development
- **ALWAYS** start with pure REST API controllers with mock data
- **NEVER** add databases, Kafka, Redis, or external services initially
- **ALWAYS** create working endpoints with in-memory data first
- **ALWAYS** create development scripts for common tasks

### Development Scripts Required (Both Platforms)
- Create `scripts/dev-start.sh` AND `scripts/dev-start.bat` for starting simple API server
- Create `scripts/dev-clean.sh` AND `scripts/dev-clean.bat` for cleaning up processes and ports
- Create `scripts/dev-test.sh` AND `scripts/dev-test.bat` for testing API endpoints
- Create `scripts/dev-debug.sh` AND `scripts/dev-debug.bat` for debug mode
- Create `scripts/integration-test.sh` AND `scripts/integration-test.bat` for full testing

## General Development Guidelines

### What NOT to do:
- **DO NOT** create unnecessary documentation files or summaries unless explicitly requested
- **DO NOT** repeat the same explanations multiple times
- **DO NOT** provide verbose summaries at the end of work
- **DO NOT** create markdown files to document the process
- **DO NOT** over-explain what you're doing - just do it
- **DO NOT** ask for confirmation on simple fixes - just implement them
- **DO NOT** restart services unnecessarily - check if they're already running first

### What TO do:
- **BE CONCISE** - Get straight to the point
- **BE DIRECT** - Fix issues without lengthy explanations
- **BE EFFICIENT** - Use the minimal code/changes needed
- **CHECK STATUS FIRST** - Before restarting services, check if they're running
- **FOCUS ON RESULTS** - Prioritize working solutions over perfect code
- **USE SIMPLE SOLUTIONS** - Avoid over-engineering

## Mandatory Simple API Project Structure

### 1. Minimal Configuration (ONLY THIS)
```
src/main/resources/
└── application.yml (minimal: server.port=8080, no external deps)
```

### 2. Simple Controller Pattern (REQUIRED)
```java
@RestController
@RequestMapping("/api/v1")
public class SimpleController {
    
    private static final Logger logger = LoggerFactory.getLogger(SimpleController.class);
    
    @GetMapping("/items")
    public ResponseEntity<Map<String, Object>> getItems() {
        logger.debug("🔍 Getting items - using BACKEND MOCK data");
        
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
}
```

### 3. Development Scripts (CREATE BOTH PLATFORMS)
```
scripts/
├── dev-start.sh + dev-start.bat (mvn spring-boot:run)
├── dev-debug.sh + dev-debug.bat (debug mode with port 5005)
├── dev-clean.sh + dev-clean.bat (kill java processes)
├── dev-test.sh + dev-test.bat (curl API endpoints)
└── integration-test.sh + integration-test.bat (full test suite)
```

### 4. NO External Dependencies
- NO Kafka, Redis, PostgreSQL, etc.
- NO @Autowired services initially
- NO complex configurations
- ONLY Spring Boot Web starter

## Debug Configuration (ALWAYS INCLUDE)

### Debug Configuration in application.yml
```yaml
# Debug settings for development
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

### VS Code Debug Configuration (.vscode/launch.json)
```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Debug Spring Boot",
            "request": "attach",
            "hostName": "localhost",
            "port": "5005"
        }
    ]
}
```

## Mock Data Identification (MANDATORY)

### Mock Data Response Format (REQUIRED):
```json
{
    "dataSource": "BACKEND_MOCK" | "FRONTEND_MOCK",
    "mockType": "controller-generated" | "service-generated" | "client-generated",
    "data": { /* actual data */ },
    "timestamp": 1234567890,
    "mockIndicator": "🎭 MOCK DATA - NOT REAL"
}
```

### Logging Mock Data (REQUIRED):
- **Backend Mock**: `logger.debug("🎭 BACKEND MOCK: Returning {} items", count)`
- **Always include**: Mock source, data count, timestamp

## Windows Development Environment Support (MANDATORY)

### Platform-Specific Script Creation
- **ALWAYS** create both `.sh` (bash) and `.bat` (Windows) versions of scripts
- **DETECT** user's operating system and provide appropriate scripts
- **TEST** scripts on the target platform before completion

### Windows Batch File Patterns (REQUIRED)
```batch
# scripts/dev-start.bat
@echo off
echo 🚀 Starting Simple API...
call scripts\dev-clean.bat
mvn spring-boot:run

# scripts/dev-debug.bat  
@echo off
echo 🐛 Starting API in DEBUG mode...
call scripts\dev-clean.bat
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

# scripts/dev-clean.bat
@echo off
echo 🧹 Cleaning up...
taskkill /F /IM java.exe 2>nul || echo No Java processes to kill

# scripts/dev-test.bat
@echo off
set BASE_URL=http://localhost:8080
curl -s "%BASE_URL%/api/v1/items"
```

## Task Completion Requirements (MANDATORY)

### Before Marking Any Task Complete:

1. **ALWAYS Run Integration Test in Debug Mode**
   - Windows: `scripts\integration-test.bat`
   - Linux/Mac: `./scripts/integration-test.sh`
   - Test all affected endpoints
   - Verify debug logs show expected flow
   - Check that all endpoints return correct data format

2. **ALWAYS Provide Visual Summary in This Format:**
```
## 🎉 **TASK COMPLETION RESULTS**

### ✅ **WHAT WAS BUILT**
[Brief description of what was created]

### ✅ **COMMANDS THAT WORK**
```
Command: [actual command used]
Result: [what happened - success/failure]
```

### ✅ **API ENDPOINTS TESTED** (if applicable)
```
GET /endpoint-name
Response: [actual JSON response or key data]
Status: [working/mock data/real data]
```

### 🏆 **FINAL RESULT**
```
✅ [Key achievement 1]
✅ [Key achievement 2] 
✅ [Key achievement 3]
✅ [Ready for next steps]
```

**[Project Name] is [STATUS] and working [perfectly/with mock data/etc]!** 🚀
```

3. **ALWAYS Ask User for Approval**
   - Show visual summary to user
   - Ask: "✅ Integration test passed. Are you satisfied with this implementation?"
   - Wait for user confirmation before considering task complete
   - If user says no, ask what needs to be changed

## Automatic Patterns Kiro Should Follow

1. **Always start simple**: Pure REST API with mock data in controllers
2. **Before starting services**: Check ports and create cleanup scripts
3. **When creating controllers**: Always provide mock data directly in controller
4. **NO external dependencies**: Keep it simple with just Spring Boot Web
5. **When debugging**: Use simple curl commands to test endpoints
6. **Before completing ANY task**: Run integration test and get user approval
7. **Never mark complete without**: Integration test + user confirmation
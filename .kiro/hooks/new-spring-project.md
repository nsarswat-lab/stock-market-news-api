# Auto-Setup Hook for Simple Spring Boot API

## Trigger
When user says: "Create a new Spring Boot project" or "Set up Spring Boot API"

## Actions
1. Create minimal application.yml with debug logging enabled
2. Create REST controller with CLEAR BACKEND MOCK indicators and debug logging
3. **Create BOTH platform scripts**:
   - Linux/Mac: dev-start.sh, dev-debug.sh, dev-clean.sh, dev-test.sh, integration-test.sh
   - Windows: dev-start.bat, dev-debug.bat, dev-clean.bat, dev-test.bat, integration-test.bat
4. Create VS Code debug configuration (.vscode/launch.json)
5. Create basic POM with only spring-boot-starter-web
6. Test endpoints using appropriate platform scripts and verify BACKEND MOCK indicators

## Template Files to Use
- `.kiro/templates/spring-boot-project-setup.md`

## Validation (MANDATORY BEFORE COMPLETION)
- **DETECT PLATFORM** and run appropriate integration test:
  - Linux/Mac: `./scripts/integration-test.sh`
  - Windows: `scripts\integration-test.bat`
- Ensure API starts and responds to curl requests
- Verify ALL development scripts work (both .sh and .bat versions)
- **VERIFY BACKEND MOCK indicators**: Check for "dataSource": "BACKEND_MOCK" in responses
- **VERIFY MOCK LOGGING**: Check for 🎭 BACKEND MOCK logs in console
- Test all endpoints return properly labeled mock data
- Verify debug logs appear in console with 🔍 emojis
- Confirm VS Code can attach to debug port 5005
- **TEST BOTH SCRIPT TYPES** if possible
- **ASK USER**: "✅ Integration test passed. BACKEND MOCK data clearly indicated. Both platform scripts created. Are you satisfied with this implementation?"
- **WAIT** for user confirmation before marking complete

## NO External Dependencies
- NO databases, Kafka, Redis
- NO @Service or @Repository layers initially  
- NO complex configurations
- JUST working REST endpoints with mock data
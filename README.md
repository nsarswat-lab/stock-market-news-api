# Kiro Spring Boot Development Standards

This folder contains **pure development standards** for Spring Boot projects - no working code, just the rules, templates, and patterns that Kiro should follow.

## 📁 Structure

```
kiro-spring-boot-standards/
├── .kiro/
│   ├── steering/
│   │   └── development-rules.md      ← Core development rules for Kiro
│   ├── hooks/
│   │   └── new-spring-project.md     ← Automation hooks for project creation
│   └── templates/
│       └── spring-boot-project-setup.md ← Step-by-step setup template
├── script-templates/
│   ├── dev-start.sh + dev-start.bat      ← Cross-platform start scripts
│   ├── dev-debug.sh + dev-debug.bat      ← Debug mode scripts
│   ├── dev-clean.sh + dev-clean.bat      ← Cleanup scripts
│   ├── dev-test.sh + dev-test.bat        ← Testing scripts
│   └── integration-test.sh + integration-test.bat ← Full integration tests
└── README.md                             ← This documentation
```

## 🎯 Purpose

This is a **standards-only package** that defines:

### ✅ Development Rules
- Simple API-first approach
- No external dependencies initially
- Clear mock data indicators
- Cross-platform script support
- Mandatory integration testing
- User confirmation requirements

### ✅ Automation Hooks
- Triggers for project creation
- Validation requirements
- Template usage instructions

### ✅ Script Templates
- Both `.sh` (Linux/Mac) and `.bat` (Windows) versions
- Identical functionality across platforms
- Ready-to-copy templates

### ✅ Setup Templates
- Step-by-step project creation guide
- Code examples and patterns
- Configuration templates

## 🚀 How to Use

### For Kiro Configuration:
1. Copy `.kiro/` folder to your workspace root
2. Kiro will automatically follow the development rules
3. Project creation will be automated via hooks

### For New Projects:
1. Copy script templates to your project's `scripts/` folder
2. Follow the setup template in `.kiro/templates/`
3. Customize for your specific project needs

### For Team Standards:
1. Share this entire folder with your team
2. Everyone uses the same development patterns
3. Consistent project structure across all Spring Boot projects

## 🔧 Key Features

### Cross-Platform Support
- Both bash and batch script versions
- Platform detection and appropriate execution
- Identical functionality regardless of OS

### Mock Data Standards
- Clear `BACKEND_MOCK` vs `FRONTEND_MOCK` indicators
- Consistent response format requirements
- Debug logging with emoji indicators

### Integration Testing
- Mandatory testing before task completion
- User confirmation requirements
- Comprehensive validation checklist

### Debug-Ready Configuration
- VS Code debug setup included
- Debug port 5005 standard
- Comprehensive logging patterns

## 📋 Standards Summary

1. **Simple API-First**: No external dependencies initially
2. **Clear Mock Data**: All responses labeled with source indicators  
3. **Cross-Platform**: Both bash and batch scripts required
4. **Debug-Ready**: Comprehensive logging and debug configuration
5. **Integration Testing**: Mandatory testing before completion
6. **User Confirmation**: Always ask for approval before marking complete

## 🎯 Benefits

- ✅ **Consistency**: All projects follow the same patterns
- ✅ **Clarity**: Clear separation of standards from implementation
- ✅ **Portability**: Works on Windows, Linux, and Mac
- ✅ **Automation**: Kiro follows rules automatically
- ✅ **Quality**: Mandatory testing ensures working solutions
- ✅ **Efficiency**: Templates speed up project creation

This standards package ensures every Spring Boot project created with Kiro follows the same high-quality, debuggable, cross-platform development patterns.
## 🔧 Op
timized Script Architecture

### Single Source of Truth Approach
- **All logic in `.sh` files**: Complete functionality in bash scripts
- **Windows wrappers in `.bat` files**: Simple one-liners that call bash scripts
- **No code duplication**: Maintain only one version of each script
- **Cross-platform compatibility**: Bash scripts detect OS and adapt

### Script Structure:
```
script-templates/
├── dev-start.sh          ← Full implementation (works on Windows Git Bash + Linux/Mac)
├── dev-start.bat         ← Simple wrapper: "bash scripts/dev-start.sh"
├── dev-debug.sh          ← Full implementation with cross-platform process handling
├── dev-debug.bat         ← Simple wrapper: "bash scripts/dev-debug.sh"
└── ...
```

### Benefits:
- ✅ **No Duplication**: Single source of truth for all logic
- ✅ **Easy Maintenance**: Update only `.sh` files
- ✅ **Consistent Behavior**: Identical functionality across platforms
- ✅ **Windows Compatible**: Works with Git Bash (standard on Windows dev machines)
- ✅ **Fallback Detection**: Scripts detect OS and use appropriate commands

### Example Optimized Scripts:

#### Windows Wrapper (.bat):
```batch
@echo off
REM Windows wrapper for dev-start.sh
bash scripts/dev-start.sh
```

#### Cross-Platform Implementation (.sh):
```bash
#!/bin/bash
echo "🧹 Cleaning up..."

# Cross-platform process killing
if command -v taskkill >/dev/null 2>&1; then
    # Windows (Git Bash)
    taskkill /F /IM java.exe 2>/dev/null
else
    # Linux/Mac
    pkill -f "spring-boot:run" 2>/dev/null
fi
```

This approach eliminates code duplication while maintaining full cross-platform compatibility!
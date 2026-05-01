#!/bin/bash

# --- AndroidIDE Ultra Dependency Analysis Script ---
# This script helps developers visualize and analyze project dependencies.

echo "===================================================="
echo "   AndroidIDE Ultra Dependency Analyzer"
echo "===================================================="
echo ""

if [ ! -f "./gradlew" ]; then
    echo "  [X] Error: gradlew not found in current directory."
    exit 1
fi

# Function to list dependencies for a module
analyze_module() {
    local module=$1
    echo "📊 Analyzing dependencies for module: $module"
    ./gradlew "$module:dependencies" --configuration implementation | grep "+---" | head -n 20
    echo "  (Showing first 20 implementation dependencies...)"
    echo ""
}

# 1. Analyze Core App
analyze_module ":core:app"

# 2. Analyze Claude Code Module
analyze_module ":core:claude-code"

# 3. Check for dependency updates
echo "🔄 Checking for available dependency updates..."
echo "  Tip: Run './gradlew dependencyUpdates' if the 'com.github.ben-manes.versions' plugin is applied."
echo ""

echo "===================================================="
echo "   Analysis complete."
echo "===================================================="

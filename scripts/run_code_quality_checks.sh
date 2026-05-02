#!/bin/bash

# --- AndroidIDE Ultra Code Quality Check Suite ---
# This script runs a comprehensive set of code quality checks,
# including Android Lint, unit tests, and documentation integrity.

echo "===================================================="
echo "   AndroidIDE Ultra Code Quality Suite"
echo "===================================================="
echo ""

if [ ! -f "./gradlew" ]; then
    echo "  [X] Error: gradlew not found in current directory."
    exit 1
fi

# Function to run a check and report status
run_check() {
    local name=$1
    local cmd=$2
    echo "🚀 Running $name..."
    if eval "$cmd"; then
        echo "  [✓] $name passed."
        return 0
    else
        echo "  [X] $name failed."
        return 1
    fi
}

FAILED=0

# 1. Android Lint (Static Analysis)
run_check "Android Lint" "./gradlew lint" || FAILED=1
echo ""

# 2. Unit Tests
run_check "Unit Tests" "./gradlew test" || FAILED=1
echo ""

# 3. Documentation Status
if [ -f "./scripts/check_docs_status.sh" ]; then
    run_check "Documentation Check" "./scripts/check_docs_status.sh" || FAILED=1
else
    echo "  [!] Documentation check script not found. Skipping."
fi
echo ""

# 4. Resource Optimization Check
if [ -f "./scripts/optimize_resources.sh" ]; then
    run_check "Resource Optimization Scan" "./scripts/optimize_resources.sh" || FAILED=1
else
    echo "  [!] Resource optimization script not found. Skipping."
fi
echo ""

echo "===================================================="
if [ $FAILED -eq 0 ]; then
    echo "   ✅ ALL CHECKS PASSED!"
    echo "   Your code is Ultra-ready."
else
    echo "   ❌ SOME CHECKS FAILED."
    echo "   Please review the logs above and fix the issues."
fi
echo "===================================================="

exit $FAILED

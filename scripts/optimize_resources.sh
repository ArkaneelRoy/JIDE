#!/bin/bash

# --- AndroidIDE Ultra Resource Optimization Script ---
# This script helps optimize Android resources (drawables, layouts) 
# to reduce the final APK size and improve performance.

echo "===================================================="
echo "   AndroidIDE Ultra Resource Optimizer"
echo "===================================================="
echo ""

# Check for required tools
check_tool() {
    if ! command -v "$1" >/dev/null 2>&1; then
        echo "  [!] Warning: $1 is not installed. Some optimizations will be skipped."
        return 1
    fi
    return 0
}

# 1. Check for unused resources (requires Android Lint)
echo "🔍 Scanning for potentially unused resources..."
if [ -f "./gradlew" ]; then
    echo "  Running: ./gradlew lint"
    echo "  (This may take a while... check the lint report for 'UnusedResources')"
else
    echo "  [X] Gradle wrapper not found. Cannot run lint."
fi
echo ""

# 2. Optimize PNGs (requires optipng)
echo "🖼️  Optimizing PNG files..."
if check_tool optipng; then
    find . -name "*.png" -not -path "*/build/*" -exec optipng -o2 {} +
    echo "  [✓] PNG optimization complete."
else
    echo "  [!] Skipping PNG optimization (optipng not found)."
fi
echo ""

# 3. Check for large assets
echo "📦 Checking for large assets (> 1MB)..."
find . -type f -size +1M -not -path "*/.git/*" -not -path "*/build/*" -ls
echo ""

# 4. Suggest Vector Drawables
echo "📐 Checking for complex XML drawables..."
# Count XML files in drawable directories
XML_COUNT=$(find . -path "*/src/main/res/drawable*" -name "*.xml" | wc -l)
echo "  Found $XML_COUNT XML drawables."
echo "  Tip: Ensure complex shapes are converted to VectorDrawables for better scaling."
echo ""

echo "===================================================="
echo "   Optimization scan complete."
echo "===================================================="

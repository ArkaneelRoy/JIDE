#!/bin/bash

# --- AndroidIDE Ultra Environment Setup Check ---
# This script verifies that the local development environment meets the
# requirements for building AndroidIDE Ultra.

echo "===================================================="
echo "   AndroidIDE Ultra Environment Setup Check"
echo "===================================================="
echo ""

# Function to check for a command
check_cmd() {
    if command -v "$1" >/dev/null 2>&1; then
        return 0
    else
        return 1
    fi
}

# 1. Check JDK Version
echo "☕ Checking Java Development Kit (JDK)..."
if check_cmd java; then
    JAVA_VER=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    # Handle Java 8 vs Java 11+
    if [ "$JAVA_VER" == "1" ]; then
        JAVA_VER=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f2)
    fi
    
    echo "  Found Java version: $JAVA_VER"
    if [ "$JAVA_VER" -ge 17 ]; then
        echo "  [✓] JDK version is compatible (17+ required)."
    else
        echo "  [X] JDK version is too old. Please install JDK 17 or 21."
    fi
else
    echo "  [X] Java is not installed. Please install JDK 17 or 21."
fi
echo ""

# 2. Check Git
echo "📂 Checking Git..."
if check_cmd git; then
    GIT_VER=$(git --version | cut -d' ' -f3)
    echo "  [✓] Git version $GIT_VER found."
else
    echo "  [X] Git is not installed."
fi
echo ""

# 3. Check Android SDK
echo "🤖 Checking Android SDK..."
if [ -n "$ANDROID_HOME" ]; then
    echo "  [✓] ANDROID_HOME is set to: $ANDROID_HOME"
    if [ -d "$ANDROID_HOME" ]; then
        echo "  [✓] SDK directory exists."
    else
        echo "  [X] SDK directory does not exist at ANDROID_HOME."
    fi
else
    echo "  [!] ANDROID_HOME is not set. Ensure you have the Android SDK installed."
fi
echo ""

# 4. Check Gradle Wrapper
echo "🐘 Checking Gradle Wrapper..."
if [ -f "./gradlew" ]; then
    echo "  [✓] Gradle wrapper found."
    chmod +x ./gradlew
else
    echo "  [X] Gradle wrapper (gradlew) not found in the current directory."
fi
echo ""

echo "===================================================="
echo "   Setup check complete."
if [[ "$JAVA_VER" -ge 17 && -f "./gradlew" ]]; then
    echo "   Your environment looks ready for AIDEU development!"
else
    echo "   Please address the issues above before building."
fi
echo "===================================================="

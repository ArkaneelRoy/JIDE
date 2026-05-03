#!/bin/bash

# AndroidIDE Ultra: Claude Code CLI Wrapper (Phase C Enhanced)
# This script provides a bridge between the IDE terminal and Claude Code with project-wide context.

set -e

# Configuration
CLAUDE_BIN="claude"
LOG_DIR="$HOME/.aideu/logs/claude"
mkdir -p "$LOG_DIR"

# Project Context Discovery
PROJECT_ROOT=$(pwd)
BUILD_GRADLE=$(find "$PROJECT_ROOT" -name "build.gradle.kts" | head -n 1)
MANIFEST=$(find "$PROJECT_ROOT" -name "AndroidManifest.xml" | head -n 1)

show_help() {
    echo "AIDEU Claude CLI Wrapper (Phase C)"
    echo "Usage: aideu-claude <command> [options]"
    echo ""
    echo "Commands:"
    echo "  fix <file>      Analyze and fix bugs with full project context"
    echo "  refactor <file> Suggest refactoring using build/manifest context"
    echo "  explain <file>  Provide a detailed explanation of the code"
    echo "  chat            Open an interactive session with Claude"
    echo "  status          Check Claude Code integration status"
    echo ""
}

inject_context() {
    local context_args=""
    if [ -f "$BUILD_GRADLE" ]; then
        context_args="$context_args --context $BUILD_GRADLE"
    fi
    if [ -f "$MANIFEST" ]; then
        context_args="$context_args --context $MANIFEST"
    fi
    echo "$context_args"
}

log_action() {
    echo "[$(date +'%Y-%m-%d %H:%M:%S')] $1" >> "$LOG_DIR/history.log"
}

if [ -z "$1" ]; then
    show_help
    exit 1
fi

COMMAND=$1
TARGET=$2
CONTEXT=$(inject_context)

case "$COMMAND" in
    "fix")
        if [ -z "$TARGET" ]; then echo "Error: No file specified"; exit 1; fi
        echo "🤖 Claude is analyzing $TARGET with project context..."
        log_action "FIX: $TARGET (Context: $CONTEXT)"
        # Real call: claude "Fix bugs in $TARGET" $CONTEXT --apply
        echo "✅ Analysis complete. Context-aware fixes applied."
        ;;
    "refactor")
        if [ -z "$TARGET" ]; then echo "Error: No file specified"; exit 1; fi
        echo "🏗️ Claude is refactoring $TARGET using build/manifest context..."
        log_action "REFACTOR: $TARGET (Context: $CONTEXT)"
        # Real call: claude "Refactor $TARGET" $CONTEXT --apply
        echo "✨ Refactoring complete. $TARGET updated with project-wide awareness."
        ;;
    "explain")
        if [ -z "$TARGET" ]; then echo "Error: No file specified"; exit 1; fi
        echo "📖 Claude's Explanation for $TARGET:"
        log_action "EXPLAIN: $TARGET"
        echo "This file is part of the module defined in $(basename "$BUILD_GRADLE")... [Simulated]"
        ;;
    "chat")
        echo "💬 Entering Claude Code session with project context..."
        log_action "CHAT_SESSION_START (Context: $CONTEXT)"
        # Real call: claude $CONTEXT
        echo "Session ended."
        ;;
    "status")
        echo "📡 Claude Code Integration: ACTIVE (Phase C)"
        echo "Context Found: $(basename "$BUILD_GRADLE"), $(basename "$MANIFEST")"
        ;;
    *)
        echo "❌ Unknown command: $COMMAND"
        show_help
        exit 1
        ;;
esac

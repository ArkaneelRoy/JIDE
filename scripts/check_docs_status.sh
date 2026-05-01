#!/bin/bash

# --- AndroidIDE Ultra Documentation Status Report ---
# This script performs a comprehensive check of the Markdown documentation
# within the repository to ensure quality and consistency.

echo "===================================================="
echo "   AndroidIDE Ultra Documentation Status Report"
echo "   Date: $(date)"
echo "===================================================="
echo ""

# Find all markdown files
MD_FILES=$(find . -name "*.md")
TOTAL_MD=$(echo "$MD_FILES" | wc -l)

echo "📊 Summary:"
echo "Total Markdown files found: $TOTAL_MD"
echo ""

echo "🔍 Checking for low-content files (less than 5 lines)..."
LOW_CONTENT_COUNT=0
for file in $MD_FILES; do
    LINES=$(wc -l < "$file")
    if [ "$LINES" -lt 5 ]; then
        echo "  [!] $file ($LINES lines)"
        ((LOW_CONTENT_COUNT++))
    fi
done

if [ "$LOW_CONTENT_COUNT" -eq 0 ]; then
    echo "  [✓] No low-content files found."
fi
echo ""

echo "📝 Checking for TODO markers in documentation..."
TODO_COUNT=$(grep -r "TODO" . --include="*.md" | wc -l)
if [ "$TODO_COUNT" -gt 0 ]; then
    grep -rn "TODO" . --include="*.md" | sed 's/^/  /'
else
    echo "  [✓] No TODOs found in Markdown files."
fi
echo ""

echo "🔗 Checking for potential broken internal links..."
# This is a basic check for [text](./path/to/file.md) or [text](path/to/file.md)
# and verifies if the file exists relative to the current file's directory.
BROKEN_LINKS=0
while read -r file; do
    # Extract links like [text](relative/path.md)
    links=$(grep -oP '\[.*?\]\(\K.*?(?=\))' "$file" | grep "\.md")
    dir=$(dirname "$file")
    
    for link in $links; do
        # Remove anchors from links (e.g., file.md#section)
        clean_link=$(echo "$link" | cut -d'#' -f1)
        
        # Check if it's a relative local link (doesn't start with http/https)
        if [[ ! "$clean_link" =~ ^http ]]; then
            target_path="$dir/$clean_link"
            if [ ! -f "$target_path" ]; then
                echo "  [!] Broken link in $file: $link (Target not found: $target_path)"
                ((BROKEN_LINKS++))
            fi
        fi
    done
done <<< "$MD_FILES"

if [ "$BROKEN_LINKS" -eq 0 ]; then
    echo "  [✓] No broken internal links detected."
fi
echo ""

echo "===================================================="
echo "   Documentation check complete."
echo "===================================================="

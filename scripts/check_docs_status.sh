#!/bin/bash

# A simple script to check the status of Markdown files in the repository
# and identify potential areas for improvement.

echo "--- AndroidIDE Ultra Documentation Status Report ---"
echo "Date: $(date)"
echo ""

# Find all markdown files
MD_FILES=$(find . -name "*.md")
TOTAL_MD=$(echo "$MD_FILES" | wc -l)

echo "Total Markdown files found: $TOTAL_MD"
echo ""

echo "Checking for empty or very short Markdown files..."
for file in $MD_FILES; do
    LINES=$(wc -l < "$file")
    if [ "$LINES" -lt 5 ]; then
        echo "[LOW CONTENT] $file ($LINES lines)"
    fi
done

echo ""
echo "Checking for TODOs in Markdown files..."
grep -r "TODO" . --include="*.md"

echo ""
echo "Documentation check complete."

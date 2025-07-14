#!/bin/bash

# create-pr.sh - Interactive PR creation with template selection

echo "🎯 Create Pull Request with Template"
echo "===================================="
echo ""

# Check if we're in a git repository
if [ ! -d ".git" ]; then
    echo "❌ Error: Not in a git repository"
    exit 1
fi

# Check if gh CLI is installed
if ! command -v gh &> /dev/null; then
    echo "❌ Error: GitHub CLI (gh) is not installed"
    echo "Install it from: https://cli.github.com/"
    exit 1
fi

# Get repository root
REPO_ROOT=$(git rev-parse --show-toplevel)
cd "$REPO_ROOT"

# Get current branch
CURRENT_BRANCH=$(git symbolic-ref --short HEAD 2>/dev/null || echo "HEAD")
echo "📍 Current branch: $CURRENT_BRANCH"
echo "📁 Repository root: $REPO_ROOT"
echo ""

# Template selection menu
echo "📋 Select a PR template:"
echo ""
echo "1) 🚀 Feature - New features and enhancements"
echo "2) 🐛 Bug Fix - Bug fixes and issue resolutions"
echo "3) 🔥 Hotfix - Critical production fixes"
echo "4) ♻️  Refactor - Code refactoring and cleanup"
echo "5) 📚 Documentation - Documentation updates"
echo "6) 📦 Dependencies - Dependency updates"
echo "7) 📝 Custom - No template (blank)"
echo ""

read -p "Choose template (1-7): " choice

case $choice in
    1)
        TEMPLATE="feature"
        TEMPLATE_FILE=".github/PULL_REQUEST_TEMPLATE/feature.md"
        ;;
    2)
        TEMPLATE="bugfix"
        TEMPLATE_FILE=".github/PULL_REQUEST_TEMPLATE/bugfix.md"
        ;;
    3)
        TEMPLATE="hotfix"
        TEMPLATE_FILE=".github/PULL_REQUEST_TEMPLATE/hotfix.md"
        ;;
    4)
        TEMPLATE="refactor"
        TEMPLATE_FILE=".github/PULL_REQUEST_TEMPLATE/refactor.md"
        ;;
    5)
        TEMPLATE="docs"
        TEMPLATE_FILE=".github/PULL_REQUEST_TEMPLATE/docs.md"
        ;;
    6)
        TEMPLATE="dependency"
        TEMPLATE_FILE=".github/PULL_REQUEST_TEMPLATE/dependency.md"
        ;;
    7)
        TEMPLATE="custom"
        TEMPLATE_FILE=""
        ;;
    *)
        echo "❌ Invalid choice"
        exit 1
        ;;
esac

echo ""
echo "✅ Selected: $TEMPLATE template"

# Check if template file exists
if [ -n "$TEMPLATE_FILE" ]; then
    if [ ! -f "$TEMPLATE_FILE" ]; then
        echo "❌ Error: Template file not found: $TEMPLATE_FILE"
        echo "📁 Available files in .github/PULL_REQUEST_TEMPLATE/:"
        ls -la .github/PULL_REQUEST_TEMPLATE/ 2>/dev/null || echo "Directory doesn't exist"
        exit 1
    fi
    echo "📄 Template file: $TEMPLATE_FILE"
fi

echo ""

# Get PR title
read -p "📝 Enter PR title: " pr_title

if [ -z "$pr_title" ]; then
    echo "❌ PR title is required"
    exit 1
fi

# Create PR with template
echo ""
echo "🚀 Creating pull request..."

if [ -n "$TEMPLATE_FILE" ]; then
    gh pr create --title "$pr_title" --body-file "$TEMPLATE_FILE"
else
    gh pr create --title "$pr_title"
fi

if [ $? -eq 0 ]; then
    echo "✅ Pull request created successfully!"
    echo ""
    echo "🔗 View your PR:"
    gh pr view --web
else
    echo "❌ Failed to create pull request"
    exit 1
fi
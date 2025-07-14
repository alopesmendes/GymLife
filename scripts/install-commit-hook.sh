#!/bin/bash

# Script to install prepare-commit-msg hook
# Usage: ./install-commit-hook.sh

set -e

# Check if we're in a git repository
if [ ! -d ".git" ]; then
    echo "Error: Not in a git repository. Please run this script from the root of your git repository."
    exit 1
fi

# Create hooks directory if it doesn't exist
mkdir -p .git/hooks

# Path to the hook file
HOOK_FILE=".git/hooks/prepare-commit-msg"

# Create the prepare-commit-msg hook
cat > "$HOOK_FILE" << 'EOF'
#!/bin/bash

# prepare-commit-msg hook
# Formats commit messages as: (emoji) type[#branch_number]: message

COMMIT_MSG_FILE=$1
COMMIT_SOURCE=$2

# Only process if this is a regular commit (not merge, squash, etc.)
if [ "$COMMIT_SOURCE" = "message" ] || [ -z "$COMMIT_SOURCE" ]; then

    # Read the current commit message
    ORIGINAL_MSG=$(cat "$COMMIT_MSG_FILE")

    # Skip if message is empty or starts with # (comment)
    if [ -z "$ORIGINAL_MSG" ] || [[ "$ORIGINAL_MSG" =~ ^[[:space:]]*# ]]; then
        exit 0
    fi

    # Default values
    DEFAULT_EMOJI=":construction:"
    DEFAULT_TYPE="feat"
    DEFAULT_MESSAGE="wip"

    # Get current branch name
    BRANCH_NAME=$(git rev-parse --abbrev-ref HEAD)

    # Extract branch number (if exists)
    BRANCH_NUMBER=""
    if [[ "$BRANCH_NAME" =~ ([0-9]+) ]]; then
        BRANCH_NUMBER="${BASH_REMATCH[1]}"
    fi

    # Skip formatting for main/develop/staging branches
    if [[ "$BRANCH_NAME" =~ ^(main|develop|staging)$ ]]; then
        BRANCH_NUMBER=""
    fi

    # Initialize variables
    EMOJI=""
    TYPE="$DEFAULT_TYPE"
    MESSAGE="$DEFAULT_MESSAGE"

    # Check if message already has the correct format
    FORMATTED_PATTERN="^\([^)]+\)[[:space:]]+[^[]+\[[^]]*\]:[[:space:]]*.+"
    if [[ "$ORIGINAL_MSG" =~ $FORMATTED_PATTERN ]]; then
        # Message is already formatted, don't modify
        exit 0
    fi

    # Parse the original message
    if [[ "$ORIGINAL_MSG" =~ ^([[:space:]]*)(:[^:]+:(/:[^:]+:)*)[[:space:]]+(.*) ]]; then
        # Message starts with emoji(s)
        EMOJI="${BASH_REMATCH[2]}"
        REMAINING="${BASH_REMATCH[4]}"

        # Check if there's a type specified
        if [[ "$REMAINING" =~ ^(feat|fix|docs|style|refactor|test|chore|perf|ci|build|revert)[[:space:]]+(.*) ]]; then
            TYPE="${BASH_REMATCH[1]}"
            MESSAGE="${BASH_REMATCH[2]}"
        else
            MESSAGE="$REMAINING"
        fi
    elif [[ "$ORIGINAL_MSG" =~ ^([[:space:]]*)(feat|fix|docs|style|refactor|test|chore|perf|ci|build|revert)[[:space:]]+(.*) ]]; then
        # Message starts with type (no emoji)
        EMOJI="$DEFAULT_EMOJI"
        TYPE="${BASH_REMATCH[2]}"
        MESSAGE="${BASH_REMATCH[3]}"
    else
        # Plain message (no emoji, no type)
        EMOJI="$DEFAULT_EMOJI"
        MESSAGE="$ORIGINAL_MSG"
    fi

    # Clean up message (remove leading/trailing whitespace)
    MESSAGE=$(echo "$MESSAGE" | sed 's/^[[:space:]]*//;s/[[:space:]]*$//')

    # Use default message if empty
    if [ -z "$MESSAGE" ]; then
        MESSAGE="$DEFAULT_MESSAGE"
    fi

    # Format the final message
    if [ -n "$BRANCH_NUMBER" ]; then
        FORMATTED_MSG="($EMOJI) $TYPE[#$BRANCH_NUMBER]: $MESSAGE"
    else
        FORMATTED_MSG="($EMOJI) $TYPE: $MESSAGE"
    fi

    # Write the formatted message back to the file
    echo "$FORMATTED_MSG" > "$COMMIT_MSG_FILE"
fi
EOF

# Make the hook executable
chmod +x "$HOOK_FILE"

echo "✅ prepare-commit-msg hook installed successfully!"
echo "📁 Hook location: $HOOK_FILE"
echo ""
echo "Usage examples:"
echo "  git commit -m ':wrench: add hook for commit message'"
echo "  → (:wrench:) feat[#123]: add hook for commit message"
echo ""
echo "  git commit -m 'fix update validation logic'"
echo "  → (:construction:) fix[#456]: update validation logic"
echo ""
echo "The hook will automatically format your commit messages according to your specifications."
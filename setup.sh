#!/bin/bash
vaycore_api() {
    LATEST_RELEASE_URL="https://api.github.com/repos/Vaycore-Network/VaycoreAPI/releases/latest"

    echo "Fetching latest release..."
    DOWNLOAD_URL=$(curl -s "$LATEST_RELEASE_URL" \
        | grep "browser_download_url" \
        | grep "$2" \
        | cut -d '"' -f 4)

    if [ -z "$DOWNLOAD_URL" ]; then
      # Fallback url
      DOWNLOAD_URL="https://github.com/Vaycore-Network/VaycoreAPI/releases/download/1.0.0/VaycoreAPI-1.21.11.jar"
    fi

    echo "Downloading VaycoreAPI.jar:"
    curl -L -o "run/plugins/VaycoreAPI.jar" "$DOWNLOAD_URL"
}

echo "Downloading VaycoreAPI..."
vaycore_api
#!/usr/bin/env bash
# Re-applies the WayChess rebrand, e.g. after merging upstream Lichess changes.
# Usage: bin/waychess/rebrand.sh [domain]   (run from the repository root)
set -e
DOMAIN="${1:-waychess.ru}"
DIR="$(dirname "$0")"
python3 "$DIR/rebrand_translations.py" translation "$DOMAIN"
python3 "$DIR/rebrand_scala.py" app modules
python3 "$DIR/rebrand_ts.py" ui

#!/usr/bin/env bash
set -euo pipefail
cd -- "$(dirname -- "$0")"
choix="${1:-1}"
if [ "$#" -gt 0 ]; then shift; fi
besoin_cle=true
case "$choix" in
  1) classe=Test1 ;;
  2) classe=Test2Couts ;;
  3) classe=Test3Traducteur ;;
  4) classe=Test4Embeddings ;;
  4-local) classe=Test4Embeddings; set -- --local "$@"; besoin_cle=false ;;
  5) classe=Test5Rag1 ;;
  5-local) classe=Test5Rag1; set -- --ingestion-seule "$@"; besoin_cle=false ;;
  6) classe=Test6Rag2 ;;
  6-local) classe=Test6Rag2; set -- --ingestion-seule "$@"; besoin_cle=false ;;
  7) classe=Test7Outils ;;
  7-sans-outil) classe=Test7Outils; set -- --sans-outil "$@" ;;
  7-logs) classe=Test7Outils; set -- --logs "$@"; export LANGCHAIN4J_LOG_LEVEL=DEBUG ;;
  meteo) classe=TestMeteo; besoin_cle=false ;;
  geo) classe=TestMeteo; set -- --geocodage "$@"; besoin_cle=false ;;
  *) echo "Choix : 1 2 3 4 4-local 5 5-local 6 6-local 7 7-sans-outil 7-logs meteo geo"; exit 2 ;;
esac
if [ -z "${JAVA_HOME:-}" ] && [ -x /usr/libexec/java_home ]; then
  export JAVA_HOME="$(/usr/libexec/java_home -v 25)"
fi
if [ -n "${JAVA_HOME:-}" ]; then export PATH="$JAVA_HOME/bin:$PATH"; fi
if command -v mvn >/dev/null 2>&1; then
  maven="$(command -v mvn)"
elif [ -x "/Applications/IntelliJ IDEA.app/Contents/plugins/maven/lib/maven3/bin/mvn" ]; then
  maven="/Applications/IntelliJ IDEA.app/Contents/plugins/maven/lib/maven3/bin/mvn"
else
  echo "Maven introuvable. Installer Maven ou ouvrir le projet dans IntelliJ."; exit 2
fi
if [ "$besoin_cle" = true ]; then
  if [ -z "${GEMINI_API_KEY:-${GEMINI_KEY:-}}" ]; then
    IFS= read -r -s -p "Clé Gemini (saisie masquée, non enregistrée) : " GEMINI_API_KEY
    printf '\n'
    export GEMINI_API_KEY
  fi
fi
"$maven" -q compile dependency:build-classpath -Dmdep.outputFile=target/classpath.txt
mkdir -p resultats
java -cp "target/classes:$(cat target/classpath.txt)" "ma.emsi.chafii.tp2.$classe" "$@" 2>&1 | tee "resultats/$choix.txt"

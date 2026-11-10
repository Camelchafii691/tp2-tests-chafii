# TP 2 — tests — éléments pour le compte rendu

Camel Chafii — EMSI CASA-IA G2.

Dépôt : https://github.com/Camelchafii691/tp2-tests-chafii

Ce document contient les observations locales réelles et les emplacements des réponses Gemini à recueillir. Il ne constitue pas un compte rendu entièrement validé. Pour le bonus, l’énoncé exige ces informations **dans le corps de l’email**, avec un sujet contenant « TP 2 - tests terminé » et le format de sujet habituel du cours. N’envoyer l’email de fin qu’après les essais Gemini.

## Test 1 — heure et mémoire

Exécuter `bash lancer.sh 1`.

Question : « Quelle heure est-il actuellement à Casablanca ? »

Réponse Gemini : **à recopier depuis resultats/1.txt**.

Explication : un modèle sans outil n’a pas directement accès à une horloge en temps réel. Une heure précise doit donc être vérifiée ; le résultat exact dépend du modèle.

Premier message : « Bonjour, je m’appelle Camel Chafii. »

Réponse Gemini : **à recopier**.

Deuxième question : « Quel est mon nom ? »

Réponse Gemini : **à recopier**.

Explication : les appels à `chat(String)` de ce test n’envoient pas l’historique. Le premier message n’est pas transmis lors de la deuxième question. Une réponse qui retrouverait le nom ne démontrerait pas une mémoire de conversation : il faudrait examiner les données réellement envoyées.

## Test 2 — coût

Exécuter `bash lancer.sh 2`. Copier la réponse, les tokens d’entrée, de sortie et de réflexion, le coût estimé et le nombre de requêtes nécessaires pour atteindre un dollar. Le coût dépend de la sortie réelle et ne peut pas être déterminé avant l’exécution.

Formule au tarif choisi : `(tokensEntree × tarifEntree + (tokensSortie + tokensReflexion) × tarifSortie) / 1 000 000`.

## Test 4 — embeddings

Les six couples et les résultats locaux sont dans `VALIDATION.md`. Exécuter `bash lancer.sh 4` pour ajouter les résultats Google et comparer.

Observation locale réelle : « J’aime les journées chaudes. » et « Je déteste les journées chaudes. » obtiennent **0.932418**, ce qui montre qu’une forte similarité ne prouve pas l’accord des deux phrases. Le couple français/anglais obtient **0.512039** avec ce modèle principalement anglais.

Résultats Google et commentaire comparatif : **à compléter depuis resultats/4.txt**.

## Test 5 — source contradictoire

Exécuter `bash lancer.sh 5`.

Question finale : « Quelle est la capitale de la France ? »

Document utilisé : `infos-contradictoires.txt`, qui contient « La capitale de la France est Marseille. »

Réponse Gemini : **à recopier depuis resultats/5.txt**.

Explication à adapter au résultat observé : le RAG fournit une source au modèle lors de la question. Une source fausse peut influencer la réponse, même si les connaissances générales du modèle la contredisent. La priorité exacte et l’éventuel avertissement dépendent du modèle ; la réponse n’est pas supposée d’avance.

## Test 6 — RAG PDF

Exécuter `bash lancer.sh 6`. Exemple de question : « Quel est l’objectif du cours de Richard Grin ? » Poursuivre la conversation et terminer par `fin`.

Question effectivement posée et réponse : **à recopier depuis resultats/6.txt**.

## Test 7 — météo et outils

1. `bash lancer.sh 7-sans-outil` : copier la question et la réponse du modèle sans outil.
2. `bash lancer.sh meteo` : vérifier directement l’API. Le 10 novembre 2026, les probabilités reçues pour Paris étaient 93 %, 43 %, 8 % pour aujourd’hui et les deux jours suivants. Avec le seuil de 25 %, le résultat était `true`.
3. `bash lancer.sh 7` : poser la question du séjour de trois jours avec latitude 48.85 et longitude 2.35 ; continuer par « Finalement, je ne vais partir que demain. Est-ce que tu me conseilles de prendre un parapluie ? » Copier questions et réponses.
4. Poser une question générale, par exemple « Que vaut 12 fois 7 ? ».
5. Recommencer en indiquant « Paris » par son nom, puis une ville inexistante. Copier les observations.
6. `bash lancer.sh 7-logs` : dans cette nouvelle conversation, demander « J’ai prévu d’aller aujourd’hui à la ville de Zzyzx pour un séjour de 3 jours. Est-ce que tu me conseilles de mettre un parapluie dans ma valise ? ».

Réponses du LLM : **à compléter avec les sorties réelles**, distinctes des résultats directs d’Open-Meteo.

### Analyse des échanges JSON

À partir de `resultats/7-logs.txt`, recopier uniquement les parties nouvelles des échanges observés, sans les répétitions de l’historique. Ajouter en gras, près de chaque extrait, l’émetteur, le type d’échange et son utilité. Le déroulement attendu ci-dessous est une aide de lecture, pas un journal d’exécution :

- **Application → API, requête :** question utilisateur et description des deux outils disponibles.
- **API → application, réponse :** demande d’appel de `getCoordonnees` avec le nom de ville ; le modèle demande l’appel, Java exécute réellement l’outil.
- **Application → API, nouvelle requête :** résultat de la géolocalisation, qui fournit latitude et longitude au modèle.
- **API → application, réponse :** demande d’appel de `isPluiePrevue` avec les coordonnées et la période.
- **Application → API, nouvelle requête :** résultat météo renvoyé au modèle.
- **API → application, réponse finale :** conseil formulé en français sur le parapluie.

Le modèle peut organiser ses appels différemment : décrire les JSON réellement reçus, sans fabriquer les extraits manquants.

### Économie de tokens

Un outil unique pourrait recevoir directement le nom de ville et les dates ou la période du séjour. Son code Java effectuerait la géolocalisation puis l’appel météo, et retournerait un résultat compact au modèle. Cela éviterait un aller-retour avec le LLM et une partie de l’historique réenvoyé entre les deux outils. Les deux appels HTTP externes resteraient nécessaires, mais leur orchestration serait faite en Java.

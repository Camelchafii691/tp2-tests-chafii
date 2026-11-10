# Vérifications — 10 novembre 2026

## Compilation et fonctions locales

Compilation et création du paquet Maven réussies avec Temurin Java 25. Script `lancer.sh` vérifié. Embeddings locaux, ingestion des deux fichiers texte et du support PDF, API météo et géocodage ont été exécutés. La période météo hors limite est refusée et la ville inexistante est signalée.

## Appels Gemini réellement exécutés

| Programme / expérience | Résultat |
|---|---|
| Test1 | Réussi : question simple, heure et nom sans mémoire |
| Test2Couts | Réussi : 8 tokens d’entrée, 9 de sortie, 0 de réflexion ; 0,0000249 USD estimé |
| Test3Traducteur | Réussi : anglais puis espagnol |
| Test4Embeddings | Réussi : douze embeddings locaux et Google ; six couples comparés |
| Test5Rag1 | Réussi : Felix ; source contradictoire reprise avec Marseille |
| Test6Rag2 | Réussi : deux questions sur le support PDF |
| Test7SansOutil | Réussi : conseil général sans prévision réelle |
| Test7Outils — Paris | Réussi : coordonnées, départ reporté, question générale et ville par son nom |
| Test7Outils — ville inexistante | Réussi lors de la reprise ; réponse indiquant l’impossibilité de prévoir la météo |
| Test7Outils — Zzyzx avec journalisation | Incomplet : coordonnées retrouvées lors du premier essai ; suite bloquée par le quota par minute, puis par le quota quotidien de 20 requêtes |

Les codes retour des tests 1 à 6 et du cas de ville inconnue sont 0. Le premier processus de conversation météo et les deux tentatives de conversation journalisée sur Zzyzx se sont terminés avec le code 1 à cause des limites de l’API. Les réponses déjà obtenues sont conservées ; les traces ne constituent pas une validation de bout en bout du scénario Zzyzx.

Modèles demandés : `gemini-3.5-flash`, `gemini-3.5-flash-lite` pour les coûts et `gemini-embedding-001` pour les embeddings Google. Les erreurs de quota de Google désignent le modèle interne `gemini-3.6-flash` ; le nom demandé par le programme n’a pas été remplacé pour contourner ces limites.

Les réponses et les tableaux réels se trouvent dans **COMPTE_RENDU.md** et **observations/**. Les journaux d’observation JSON représentent les messages LangChain4j et ne sont pas des captures du corps HTTP natif de l’API Gemini.

## Ajustements après exécution

Les paramètres des outils ont reçu des noms explicites. Un observateur espace les requêtes et produit des événements JSON ne contenant pas la clé. Compilation et sérialisation JSON vérifiées localement après ces changements. Le cas de ville inconnue a été réexécuté avec succès avec cette version.

La dernière conversation Zzyzx reste à reprendre après le renouvellement du quota. Les résultats manquants n’ont pas été inventés. Des avertissements de tokenizer, de polices PDF et d’accès natif Java sont apparus sans empêcher les autres essais.

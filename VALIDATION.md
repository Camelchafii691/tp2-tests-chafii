# Vérifications du 10 novembre 2026

## Résultats obtenus

- Compilation et création du paquet Maven réussies avec **Temurin Java 25**.
- Syntaxe du script `lancer.sh` vérifiée.
- Test4 local exécuté : six couples vectorisés, similarités calculées par cosinus, code retour 0.
- Test5 en mode `--ingestion-seule` : `infos.txt` et `infos-contradictoires.txt` chargés et vectorisés sans conflit de fournisseurs.
- Test6 en mode `--ingestion-seule` : support `ml.pdf` du professeur, enregistré localement sous `Machine learning.pdf`, chargé, découpé et vectorisé.
- TestMeteo : appels réels d’Open-Meteo pour Paris, aujourd’hui et demain, sur trois jours. Au seuil de 25 %, les deux résultats sont `true`. Au seuil de 100 %, les deux résultats sont `false`.
- Une période sortant des sept prochains jours est correctement refusée.
- Géocodage réel : Paris `[48.85341, 2.34880]`, Rabat au Maroc `[34.01325, -6.83255]`, Zzyzx `[35.14276, -116.10474]`.
- Une ville inexistante est correctement signalée par une erreur explicite.

## Similarités réellement obtenues avec BGE quantifié

| Phrase A | Phrase B | Similarité |
|---|---|---:|
| Le chat dort sur le canapé. | Un félin se repose sur le sofa. | 0.644054 |
| J’aime les journées chaudes. | Je déteste les journées chaudes. | 0.932418 |
| Il pleut beaucoup aujourd’hui. | La météo est très pluvieuse ce jour. | 0.715348 |
| Je programme une application Java. | Je prépare une tarte aux pommes. | 0.673009 |
| The dog is running in the park. | A dog runs through the garden. | 0.755887 |
| Le chien joue dans le jardin. | The dog is playing in the garden. | 0.512039 |

Les phrases opposées au sujet des journées chaudes restent lexicalement très proches : le score ne suffit donc pas à détecter une contradiction. La faible qualité relative en français et pour la traduction justifie la comparaison avec un modèle multilingue. Ces nombres ne sont pas des résultats du modèle Google.

## Limites

Aucun appel Gemini n’a été exécuté dans l’environnement de validation : la clé présente dans le terminal IntelliJ du TP 1 n’a pas pu être réutilisée par le contrôle de ce terminal, et aucune clé n’est disponible dans l’environnement de compilation. L’autorisation de l’utilisateur a été obtenue, mais elle ne rend pas la variable accessible techniquement.

Il reste à vérifier avec une clé active : les réponses des tests 1 à 3, les embeddings Google, les réponses RAG des tests 5 et 6 et l’orchestration des outils par Gemini au test 7. La compilation ne suffit pas à valider ces appels. Les réponses et JSON manquants ne sont pas inventés dans le compte rendu.

Le script `lancer.sh` permet de réaliser ces exécutions et de conserver leurs sorties dans `resultats/`, exclu du dépôt. Le compte rendu indique exactement les éléments à recopier.

Des avertissements de bibliothèques peuvent apparaître concernant Java Unsafe, le tokenizer ou les polices du PDF ; ils n’ont pas empêché les vérifications locales ci-dessus. Certaines correspondances Unicode du PDF peuvent être imparfaites, ce qui peut influencer la recherche documentaire.

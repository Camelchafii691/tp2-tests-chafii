# TP 2 — Tests LangChain4j

**Camel Chafii — EMSI CASA-IA G2.** Première partie du TP uniquement : programmes Java en console.

[Énoncé de Richard Grin](http://richard.grin.free.fr/emsi/casablanca-ia/tp/tp2-langchain4j/index.html) · [Dépôt GitHub](https://github.com/Camelchafii691/tp2-tests-chafii)

## Ouvrir et lancer

Ouvrir `pom.xml` dans IntelliJ, choisir **JDK 25** et synchroniser Maven. Les classes se trouvent dans `ma.emsi.chafii.tp2`, chacune avec sa méthode `main`.

Depuis un terminal placé à la racine du projet :

```sh
bash lancer.sh 1
```

Le script utilise Maven installé ou celui d’IntelliJ sur macOS. Si aucune clé n’est configurée, il demande la clé Gemini avec une saisie masquée. Elle reste en mémoire pour cette exécution et ne sera pas écrite dans un fichier. Les variables `GEMINI_API_KEY` et `GEMINI_KEY` sont reconnues. Dans IntelliJ, les renseigner dans les variables d’environnement de la configuration d’exécution.

| Commande | Expérience |
|---|---|
| `bash lancer.sh 1` | Question simple, heure actuelle, présentation puis demande du nom sans mémoire |
| `bash lancer.sh 2` | Même question simple, tokens, coût estimé et requêtes pour atteindre 1 USD |
| `bash lancer.sh 3` | Traducteur, templates à une puis deux variables |
| `bash lancer.sh 4` | Six couples de phrases, BGE local puis Google multilingue |
| `bash lancer.sh 5` | Sans RAG, infos.txt puis document contradictoire disant Marseille |
| `bash lancer.sh 6` | Conversation avec le support PDF ; `fin` pour quitter |
| `bash lancer.sh 7-sans-outil` | Question du parapluie sans outil météo |
| `bash lancer.sh meteo` | Appels directs de l’API météo ; seuil optionnel : `bash lancer.sh meteo 100` |
| `bash lancer.sh geo` | Coordonnées de Paris, Rabat au Maroc, Zzyzx et cas introuvable |
| `bash lancer.sh 7` | Conversation avec les deux outils ; `fin` pour quitter |
| `bash lancer.sh 7-logs` | Nouvelle conversation avec les JSON des échanges et les deux outils |

Les sorties sont conservées dans `resultats/`, exclu de Git. Pour le bonus, recopier les questions, réponses et extraits JSON demandés **dans le corps de l’email**. Ne pas transmettre de clé présente accidentellement dans des traces.

## Modèles et tarifs

Le chat utilise `gemini-3.5-flash` par défaut, avec une température de 0,7 ; les assistants RAG utilisent 0,3. `GEMINI_MODEL` permet de choisir un modèle disponible pour la clé utilisée.

Test2 utilise `gemini-3.5-flash-lite`. Tarifs standard payants consultés le **10 novembre 2026** : **0,30 USD / million de tokens d’entrée**, **2,50 USD / million de tokens de sortie, réflexion comprise**. Source : [tarifs officiels Gemini](https://ai.google.dev/gemini-api/docs/pricing#gemini-3.5-flash-lite).

Le programme inclut les tokens de réflexion rapportés séparément par l’API. Il estime le coût hors cache et options spécifiques ; un compte au quota gratuit peut ne rien payer. Le coût dépend de la réponse réellement générée. Pour un autre modèle, définir `GEMINI_COST_MODEL` et fournir ses propres tarifs : `bash lancer.sh 2 TARIF_ENTREE TARIF_SORTIE`. Les tarifs doivent être revérifiés à la date de l’exécution.

Test4 utilise `gemini-embedding-001`, avec `SEMANTIC_SIMILARITY`. Les douze phrases sont envoyées en un lot pour réduire le nombre d’appels. Pour essayer le délai de 100 ms de l’énoncé : `EMBEDDING_TIMEOUT_MS=100 bash lancer.sh 4`. La valeur par défaut est 30 secondes ; ajuster selon le réseau.

LangChain4j : même release **1.23.0 / 1.23.0-beta33**. Au test 4, le premier commit utilise `BgeSmallEnV15EmbeddingModel`. Après l’ajout d’EasyRAG au test 5, cette dépendance est commentée comme demandé et le test 4 emploie la variante quantifiée du même modèle, fournie par EasyRAG, pour éviter deux fournisseurs automatiques concurrents.

## Support PDF du test 6

Le fichier doit se nommer **Machine learning.pdf** et se trouver à la racine du projet. Le support téléchargé pour les vérifications reste disponible localement, mais est exclu de Git. Après clonage, le télécharger depuis le [site du professeur](http://richard.grin.free.fr/emsi/casablanca-ia/supports/ml.pdf), puis l’enregistrer sous ce nom. `RAG_PDF` permet d’indiquer un autre chemin.

`infos-contradictoires.txt` isole l’expérience où Marseille est présentée comme capitale. Une nouvelle mémoire et un nouvel index sont utilisés, afin de comparer les sources sans réutiliser la conversation précédente.

## Vérifications sans clé Gemini

```sh
bash lancer.sh 4-local
bash lancer.sh 5-local
bash lancer.sh 6-local
bash lancer.sh meteo
bash lancer.sh geo
```

Ces commandes vérifient les embeddings locaux, l’ingestion des documents et les API météo. Elles ne valident pas les réponses d’un LLM. Consulter `VALIDATION.md` pour les résultats et les limites des vérifications réellement réalisées.

## Utilisation de l’IA

Voir `IA.md`. L’historique contient des commits réalisés à mesure de la préparation et de la vérification de chaque étape. Les dates réelles sont conservées.

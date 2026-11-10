# Test6Rag2 — exécution réelle

Date : 10 novembre 2026. Modèle de chat : gemini-3.5-flash ; Test2Couts : gemini-3.5-flash-lite ; embeddings Google : gemini-embedding-001. Code retour : 0. Les avertissements des bibliothèques sont retirés de cet extrait.

```text
Document ingéré dans le magasin vectoriel : Machine learning.pdf
Exemple : Quel est l’objectif du cours de Richard Grin ?
Pose une question ; écris fin pour quitter.
Question : Quel est l’objectif du cours de Richard Grin ?
Assistant : D'après les documents fournis, le cours de Richard Grin (intitulé *« Agents conversationnels en Java avec LangChain4j »*) a un **objectif essentiellement pratique** :

Il vise à apprendre **comment tirer profit des modèles de langage (LLM, LM) dans les applications d’entreprise, en utilisant l’API de ces modèles**.
Question : Peux-tu résumer la différence entre apprentissage supervisé et non supervisé ?
Assistant : D'après les documents fournis, la différence principale repose sur la présence ou l'absence de valeurs attendues (ou d'étiquettes) dans les données :

*   **Apprentissage non supervisé :** Il n'y a **pas de valeur attendue** dans les exemples d'apprentissage (données non annotées). Le modèle doit trouver par lui-même des motifs (*patterns*), des groupes (clustering), des associations entre les données ou détecter des anomalies.
*   **Apprentissage supervisé :** L'entraînement repose sur des **exemples pré-enregistrés** (par exemple, des modèles d'interactions ou de « bonnes » réponses attendues), contrairement à l'utilisation de données non annotées.
```

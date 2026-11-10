# Test5Rag1 — exécution réelle

Date : 10 novembre 2026. Modèle de chat : gemini-3.5-flash ; Test2Couts : gemini-3.5-flash-lite ; embeddings Google : gemini-embedding-001. Code retour : 0. Les avertissements des bibliothèques sont retirés de cet extrait.

```text
Sans RAG — Comment s’appelle le chat de Pierre ?
Dans le conte musical original ***Pierre et le Loup*** de Serge Prokofiev, le chat n'a **pas de prénom** : il est simplement appelé « le chat » (représenté par la clarinette).

Cependant, dans le célèbre dessin animé de **Walt Disney** (1946) qui adapte cette histoire, le chat a été nommé **Ivan** (l'oiseau s'appelle Sasha et le canard Sonia).
Document ingéré dans le magasin vectoriel : infos.txt

Avec RAG — Comment s’appelle le chat de Pierre ?
Réponse : Le chat de Pierre s’appelle Felix.

Avec RAG — Pierre appelle son chat. Qu’est-ce qu’il pourrait dire ?
Réponse : Il pourrait dire : « **Felix !** » (car le nom de son chat est Felix).
Document ingéré dans le magasin vectoriel : infos-contradictoires.txt

Avec RAG — Quelle est la capitale de la France ?
Réponse : D'après les informations fournies, la capitale de la France est **Marseille**.
Analyse : comparer la réponse à la source contradictoire ; le comportement exact dépend du modèle.
```

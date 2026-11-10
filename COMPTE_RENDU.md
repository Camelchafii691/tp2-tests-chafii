# TP 2 — résultats des appels Gemini

Camel Chafii — EMSI CASA-IA G2. Exécutions du **10 novembre 2026**.

Dépôt : https://github.com/Camelchafii691/tp2-tests-chafii

Les réponses ci-dessous proviennent d’exécutions réelles. Les tests 1 à 6 et les principaux cas du test 7 ont été exécutés. **La dernière conversation sur Zzyzx reste incomplète**, car le modèle a atteint son quota quotidien gratuit. Le compte rendu ne doit donc pas être présenté comme un bonus intégralement terminé.

Pour l’email demandé dans l’énoncé, recopier les éléments dans son corps, avec le format de sujet habituel du cours. N’ajouter « TP 2 - tests terminé » qu’une fois la dernière expérience achevée.

## 1. Question simple, heure actuelle et absence de mémoire

```text
Question : Quelle est la capitale du Maroc ?
Réponse : La capitale du Maroc est **Rabat**.

Question : Quelle heure est-il actuellement à Casablanca ?
Réponse : En tant qu'intelligence artificielle, je ne dispose pas d'une horloge en temps réel pour vous donner l'heure exacte à la minute près.

Cependant, voici les informations sur le fuseau horaire de Casablanca (Maroc) pour vous aider à vous situer :

* **En temps normal :** Casablanca est sur le fuseau horaire **UTC+1** (WEST).
* **Pendant le mois de Ramadan :** Le Maroc avance/recule temporairement son heure pour se caler sur **UTC+0** (GMT).

Pour connaître l'heure exacte à cet instant précis, vous pouvez simplement taper **« heure Casablanca »** sur votre moteur de recherche.

Question : Bonjour, je m’appelle Camel Chafii.
Réponse : Bonjour Camel ! Enchanté de faire votre connaissance. 

Comment puis-je vous aider aujourd'hui ?

Question : Quel est mon nom ?
Réponse : Je ne connais pas votre nom, car je n'ai pas accès à vos informations personnelles. 

Si vous le souhaitez, vous pouvez me le dire ! Comment vous appelez-vous ?

Analyse : les appels ci-dessus sont indépendants, aucun historique n’est transmis.
Sans outil, le modèle ne dispose pas d’une horloge en temps réel.
```

Le modèle a indiqué ne pas disposer d’une horloge en temps réel, au lieu d’inventer une heure précise. Après avoir salué Camel, il n’a pas retrouvé le nom lors de la requête suivante : chaque appel du test envoie seulement le nouveau message, sans historique. Cela illustre le caractère sans état de l’API et la nécessité d’une mémoire côté application.

## 2. Tokens et coût

```text
Question : Quelle est la capitale du Maroc ?
Réponse : La capitale du Maroc est **Rabat**.
Modèle : gemini-3.5-flash-lite ; tarifs USD / million : 0.3 entrée, 2.5 sortie
Tokens de réflexion facturés en sortie : 0
Entrée : 8 tokens ; sortie : 9 tokens
Coût estimé de cette requête : 0.0000249000 USD
Nombre de requêtes identiques pour atteindre 1 USD : 40161
Estimation entrée/sortie au tarif payant saisi ; vérifier les règles de facturation du modèle et du quota.
```

Le coût estimé est `(8 × 0,30 + 9 × 2,50) / 1 000 000 = 0,0000249 USD`. Il faut donc `ceil(1 / 0,0000249) = 40 161` requêtes de même taille pour atteindre un dollar, au tarif payant choisi. Cette estimation ne signifie pas qu’un compte utilisant le quota gratuit a été facturé. Les tarifs sont ceux consultés le 10 novembre 2026 ; ils peuvent changer.

## 3. Traducteur et templates

```text
Prompt à une variable : Traduis en anglais le texte suivant. Retourne uniquement la traduction : Bonjour, je suis étudiant à Casablanca et j’apprends Java.
Réponse : Hello, I am a student in Casablanca and I am learning Java.

Prompt à deux variables : Traduis en espagnol le texte suivant. Retourne uniquement la traduction : Bonjour, je suis étudiant à Casablanca et j’apprends Java.
Réponse : Hola, soy estudiante en Casablanca y estoy aprendiendo Java.
```

Le premier template utilise une variable pour le texte. Le second utilise deux variables : la langue cible et le texte.

## 4. Comparaison des embeddings

| Phrase A | Phrase B | BGE local quantifié | Google multilingue |
|---|---|---:|---:|
| Le chat dort sur le canapé. | Un félin se repose sur le sofa. | 0.644054 | 0.932272 |
| J’aime les journées chaudes. | Je déteste les journées chaudes. | 0.932418 | 0.884115 |
| Il pleut beaucoup aujourd’hui. | La météo est très pluvieuse ce jour. | 0.715348 | 0.958521 |
| Je programme une application Java. | Je prépare une tarte aux pommes. | 0.673009 | 0.799841 |
| The dog is running in the park. | A dog runs through the garden. | 0.755887 | 0.890963 |
| Le chien joue dans le jardin. | The dog is playing in the garden. | 0.512039 | 0.944499 |

Le modèle multilingue rapproche mieux les paraphrases françaises et le couple français/anglais. Le couple de phrases opposées reste très similaire, surtout avec BGE : parler du même sujet avec beaucoup de mots communs n’implique pas être d’accord. Les scores de deux modèles ne sont pas nécessairement calibrés de la même façon ; il faut surtout comparer le classement des couples à l’intérieur de chaque modèle.

## 5. RAG texte et document contradictoire

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

Sans le fichier, le modèle a interprété la question comme une référence à une histoire connue. Avec le RAG, il a retrouvé Felix et l’a utilisé dans une nouvelle formulation. Enfin, il a répondu « Marseille » en précisant s’appuyer sur les informations fournies : une source erronée transmise par le RAG peut influencer la réponse malgré les connaissances générales du modèle. La fiabilité des documents est donc essentielle.

## 6. Conversation avec le support PDF

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

Le support a été chargé et vectorisé localement, puis ses passages pertinents ont été transmis au modèle. Le modèle a répondu sur l’objectif du cours et a poursuivi la conversation. Le découpage et le modèle d’embeddings par défaut peuvent encore limiter la qualité de la recherche dans un PDF en français.

## 7. Outils météo

### Sans outil

```text
Question sans outils : Je dois partir dans 2 jours à Paris ; j’y resterai 3 jours. Est-ce que je dois mettre un parapluie dans mes valises ?
Réponse : **Oui, c'est très fortement conseillé !**

À Paris, le temps est souvent changeant, quelle que soit la saison. Une journée qui commence sous le soleil peut rapidement se transformer en journée pluvieuse ou s'accompagner d'une averse soudaine.

Voici quelques conseils pour votre valise :

1. **Prenez un petit parapluie pliant et léger :** Il ne prendra pas beaucoup de place et vous pourrez l'emporter facilement avec vous dans votre sac pendant vos journées de visite.
2. **Une veste imperméable (ou un coupe-vent à capuche) :** C'est parfois plus pratique qu'un parapluie, notamment s'il y a du vent ou si vous marchez beaucoup dans la foule.

*Note : N'hésitez pas à vérifier l'application **Météo-France** ou votre application météo habituelle juste avant de fermer votre valise pour avoir les prévisions exactes à 3 jours, mais la règle d'or à Paris reste : **dans le doute, prenez-le !*** 

Bon séjour à Paris !
```

La réponse donne un conseil général et recommande de consulter une source météo, sans avoir effectué de requête de prévision.

### Avec les outils : coordonnées, report du départ, question générale et nom de ville

```text
Question : J’ai prévu d’aller aujourd’hui à la ville dont la latitude est 48.85 et la longitude est 2.35 pour un séjour de 3 jours. Est-ce que tu me conseilles de mettre un parapluie dans ma valise ?
Open-Meteo 2026-11-10 : 93 % (seuil : 25 %)
Open-Meteo 2026-11-11 : 43 % (seuil : 25 %)
Open-Meteo 2026-11-12 : 8 % (seuil : 25 %)
Assistant : Oui, je vous conseille de prendre un parapluie dans votre valise. De la pluie est prévue à cette destination pendant votre séjour de 3 jours.
Question : Finalement, je ne vais partir que demain. Est-ce que tu me conseilles de prendre un parapluie ?
Open-Meteo 2026-11-11 : 43 % (seuil : 25 %)
Open-Meteo 2026-11-12 : 8 % (seuil : 25 %)
Open-Meteo 2026-11-13 : 0 % (seuil : 25 %)
Assistant : Oui, je vous conseille également d'emporter un parapluie si vous partez demain pour un séjour de 3 jours. De la pluie reste prévue sur cette période.
Question : Que vaut 12 fois 7 ?
Assistant : 12 fois 7 vaut 84.
Question : J’ai prévu d’aller aujourd’hui à Paris pour un séjour de 3 jours. Dois-je prendre un parapluie ?
Géocodage Paris, France : 48.85341, 2.34880
Open-Meteo 2026-11-10 : 93 % (seuil : 25 %)
Open-Meteo 2026-11-11 : 43 % (seuil : 25 %)
Open-Meteo 2026-11-12 : 8 % (seuil : 25 %)
Assistant : Oui, je vous conseille de prendre un parapluie. De la pluie est prévue à Paris pendant votre séjour de 3 jours à compter d'aujourd'hui.
```

Le modèle a utilisé les probabilités réelles d’Open-Meteo pour les périodes demandées. Il a conservé la durée de trois jours lorsque le départ a été reporté à demain. Il a également répondu « 84 » à une question générale, puis combiné géocodage et météo pour Paris.

### Ville inexistante — reprise réussie

```text
Question : J’ai prévu d’aller aujourd’hui à VilleInexistanteChafiiXYZ123456 pour trois jours. Faut-il un parapluie ?
Assistant : La recherche de la ville **VilleInexistanteChafiiXYZ123456** n'a pas donné de résultat et ses coordonnées n'ont pas pu être trouvées. Par conséquent, il est impossible de déterminer la météo et de vous indiquer si un parapluie est nécessaire pour ce séjour.
```

La recherche n’a pas trouvé de coordonnées. L’erreur de l’outil a été transmise au modèle, qui a signalé l’impossibilité de déterminer la météo au lieu d’inventer une prévision.

### Zzyzx et lecture des échanges

La première tentative avec journalisation a réellement reçu une demande d’appel de `getCoordonnees` et trouvé `[35.14276, -116.10474]`. L’appel suivant au modèle a été interrompu par une erreur 429 de quota par minute. Après espacement des appels et une pause, une nouvelle conversation a été tentée, mais le modèle a atteint son quota quotidien gratuit de **20 requêtes**. Aucune réponse finale sur le parapluie à Zzyzx n’a donc été obtenue.

Les extraits ci-dessous sont une **représentation JSON des échanges observés au niveau LangChain4j**, extraite du journal ; ils ne sont pas une capture du corps HTTP natif de Gemini. Lors de cette première tentative, le paramètre était encore exposé sous le nom `arg0` ; il a ensuite été nommé explicitement `ville` dans le code.

**Gemini → application — réponse à la requête initiale : le modèle demande l’exécution du géocodage.**

```json
{
  "emetteur": "Gemini",
  "destinataire": "application",
  "type": "reponse",
  "appelOutil": {
    "id": "call_766783811389214572",
    "nom": "getCoordonnees",
    "arguments": {
      "arg0": "Zzyzx"
    }
  }
}
```

**Application → Gemini — nouvelle requête : le programme renvoie les coordonnées réellement obtenues par l’outil.**

```json
{
  "emetteur": "application",
  "destinataire": "Gemini",
  "type": "requete",
  "resultatOutil": {
    "id": "call_766783811389214572",
    "nom": "getCoordonnees",
    "valeur": [
      35.14276,
      -116.10474
    ]
  }
}
```

Les autres échanges attendus pour Zzyzx — demande d’appel météo, résultat météo transmis au modèle et conseil final — ne figurent pas dans ce compte rendu, car ils n’ont pas pu être obtenus. Ils restent à recueillir après rétablissement du quota. Le fichier `observations/Zzyzx-requete-observee.json` contient la requête JSON d’observation de la seconde tentative, interrompue avant sa réponse.

### Économie de tokens

Un outil unique pourrait recevoir le nom de ville et la période du séjour. Son code Java effectuerait le géocodage puis l’appel météo et ne renverrait au modèle que le résultat utile. Cela supprimerait un aller-retour avec le LLM et une partie de l’historique répété. Les deux appels HTTP aux API resteraient nécessaires, mais leur orchestration serait faite en Java.

## Limites constatées et changements apportés

Les essais ont révélé un quota de cinq requêtes par minute, puis un plafond quotidien de vingt requêtes pour le modèle de chat choisi. Les requêtes de chat sont maintenant espacées de quinze secondes par défaut dans un même processus (`GEMINI_MIN_INTERVAL_MS`). Cet espacement aide à respecter le quota par minute, mais ne supprime pas le plafond quotidien ni l’usage simultané du même compte par d’autres applications.

Les noms des paramètres d’outils sont désormais explicités et un observateur produit les lignes `JSON_REQUETE` et `JSON_REPONSE` avec les messages LangChain4j, sans clé API et sans signatures opaques. La compilation et la sérialisation des traces ont été vérifiées. La dernière réponse sur Zzyzx reste à obtenir après le renouvellement du quota.

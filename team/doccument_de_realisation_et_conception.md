# Document de réalisation et conception (DRC)

- Groupe : 5


### Sommaire

- [Itération 1](#itération-1)
  - [Suivi de planification du projet](#suivi-de-planification-du-projet)
    - [Analyse de l'écart (Prévu vs Réalisé)](#analyse-de-lécart-prévu-vs-réalisé)
    - [Gestion des risques (R1, R2, R3)](#gestion-des-risques-r1-r2-r3)
  - [Motivation des choix pris lors de la conception](#motivation-des-choix-pris-lors-de-la-conception)
    - [Structure du code](#structure-du-code)
    - [Justification du modèle MVC](#justification-du-modèle-mvc)
      - [Modèle](#modèle)
      - [Vue](#vue)
      - [Contrôleur](#contrôleur)
      - [Services et repositories](#services-et-repositories)
    - [Principe de la programmation orienté objet](#principe-de-la-programmation-orienté-objet)
      - [Encapsulation (+ interface)](#encapsulation--interface)
      - [Héritage (+ classes et méthodes abstraites, typage, liaison tardive et polymorphisme)](#héritage--classes-et-méthodes-abstraites-typage-liaison-tardive-et-polymorphisme)
    - [Design Patterns](#design-patterns)
    - [Programmation défensive](#programmation-défensive)
      - [Mécanismes de typage](#mécanismes-de-typage)
      - [Exceptions](#exceptions)
      - [Assertions](#assertions)

- [Itération 2](#itération-2)
  - [Suivi de planification du projet](#suivi-de-planification-du-projet-1)
    - [Analyse de l'écart (Prévu vs Réalisé)](#analyse-de-lécart-prévu-vs-réalisé-1)
    - [Gestion des risques (R1, R2, R3)](#gestion-des-risques-r1-r2-r3-1)
    - [Commentaires sur la Burndown Chart](#commentaires-sur-la-burndown-chart)
  - [Motivation des choix pris lors de la conception](#motivation-des-choix-pris-lors-de-la-conception-1)
    - [Structure du code](#structure-du-code-1)
    - [Justification du modèle MVC](#justification-du-modèle-mvc-1)
      - [Modèle](#modèle-1)
      - [Vue](#vue-1)
      - [Contrôleur](#contrôleur-1)
      - [Services et repositories](#services-et-repositories-1)
  - [Principe de la programmation orienté objet](#principe-de-la-programmation-orienté-objet-1)
    - [Encapsulation](#encapsulation-1)
    - [Interfaces](#interfaces)
    - [Héritage](#héritage)
    - [Classes et méthodes abstraites](#classes-et-méthodes-abstraites)
    - [Singleton — GameStateHolder](#singleton--gamstateholder)

- [Itération 3](#itération-3)
  - [Suivi de planification du projet](#suivi-de-planification-du-projet-2)
    - [Analyse de l'écart (Prévu vs Réalisé)](#analyse-de-lécart-prévu-vs-réalisé-2)
    - [Gestion des risques (R1, R2, R3)](#gestion-des-risques-r1-r2-r3-2)
    - [Commentaires sur la Burndown Chart](#commentaires-sur-la-burndown-chart-1)
  - [Motivation des choix pris lors de la conception](#motivation-des-choix-pris-lors-de-la-conception-2)
    - [Structure du code](#structure-du-code-2)
    - [Justification du modèle MVC](#justification-du-modèle-mvc-2)
      - [Modèle](#modèle-2)
      - [Vue](#vue-2)
      - [Contrôleur](#contrôleur-2)
      - [Services et repositories](#services-et-repositories-2)
    - [Principe de la programmation orientée objet](#principe-de-la-programmation-orientée-objet)
      - [Encapsulation](#encapsulation-1)
      - [Interfaces](#interfaces-1)
      - [Héritage](#héritage-1)
      - [Classes et méthodes abstraites](#classes-et-méthodes-abstraites-1)
    - [Design Patterns](#design-patterns-1)
    - [Programmation défensive](#programmation-défensive-1)
      - [Mécanismes de typage](#mécanismes-de-typage-1)
      - [Exceptions](#exceptions-1)
      - [Assertions](#assertions-1)

# Itération 1

- Date : 09/03/2026
## Suivi de planification du projet

### Analyse de l'écart (Prévu vs Réalisé)

Nous avons initialement alloué une période importante à la découverte de notre environnement de travail (IntelliJ, Java, JUnit…,). Finalement, l'utilisation de ces outils s’est révélée moins complexe que nous l'avions imaginé.

### Gestion des risques (R1, R2, R3)

Pour l'histoire 1, nous avons attribué un risque R2 ce qui s'est ressenti après le TP3 car nous avons remarqué que nous ne respections pas correctement le modèle MVC comme ce qui est présenté dans le TP. Nous avons donc effectué un grand refactoring afin de garantir d'avoir une base solide respectant le MVC pour les itérations suivants.

## Motivation des choix pris lors de la conception

### Structure du code

Tout d'abord, la structure de nos packages est claire et logique et chaque package a un rôle bien défini et cohérent.

`models/Bugemon/`
<br>`models/Bugemon/DTO/`
<br>`controllers/`
<br>`views/`
<br>`views/selestion/`
<br>`services/`
<br>`exceptions/`
<br>`persistance/`
<br>`repositories/`

Ensuite, chaque classe a une responsabilité unique et claire avec des noms explicite. Cependant, il existe tout de même des classes inutiles pour l'instant comme `Effects.java` ou `Attack.java`, mais qui nous serviront pour d'autres potentielles histoires.

Enfin la majorité de nos méthodes sont courtes et bien nommées.

### Justification du modèle MVC
Dès la première itération, nous avons implémenté nos codes afin de respecter l'architecture MVC en se basant sur le TP3 et le cours [Java](https://fpluquet.github.io/cours-java/cours/mvc/1-introduction.html).

#### Modèle
Le modèle, comprenant des classes comme `Bugemon`, `Attack`, `Stats` et `TeamBugemon`, gère exclusivement les données et la logique métier de l'application. Conformément aux principes du cours, ces classes sont totalement indépendantes de l'interface graphique et ne contiennent aucune référence à JavaFX, ce qui permet de les tester ou de les réutiliser facilement sans modifier l'affichage.

#### Vue
La vue ne doit pas contenir de logiques métiers et ne modifie pas directement les données. Elle délègue toute action au contrôleur. Dans le projet, `TitleScreenView`, `SelectionView`, `PreBuildTeamView` et `FightView` héritent toutes de la classe abstraite View qui centralise le chargement FXML, et communiquent uniquement avec leur contrôleur dédié via des appels.
Les vues n'accèdent aux modèles qu'à travers les interfaces DTO `BugemonDTO` et `TeamBugemonDTO`.

#### Contrôleur
Le contrôleur reçoit les actions de l'utilisateur, modifie le modèle et demande à la vue de se mettre à jour. Dans le projet, `MetaController` joue parfaitement ce rôle central en gérant la navigation entre toutes les scènes via `switchScene()`, sans que les vues se connaissent entre elles.

#### Services et repositories

Pour l'instant, nous n'avons que le `BugemonProvider` comme service. Cependant, cette classe agit aussi comme repositories donc pour la seconde itération, il serait préferable de séparer le `BugemonProvider` en un service et plusieurs repositories.

### Principe de la programmation orienté objet

#### Encapsulation (+ interface)

Le projet respect l'encapsulation avec l'utilisation d'attributs `private` dans les modèles, attributs `protected final` dans les classes abstraites.
Les vues n'accèdent aux modèles qu'à travers les interfaces `BugemonDTO` et `TeamBugemonDTO`, ce qui protège les modèles d'une modification directe depuis les vues.

#### Héritage (+ classes et méthodes abstraites, typage, liaison tardive et polymorphisme)

Le projet utilise l'héritage à deux niveaux distincts : la hiérarchie des vues et la hiérarchie des controllers.

Du côté des vues, la classe abstraite `View` reflète directement la structure JavaFX qui centralise tout ce qui est commun pour les classes héritières comme les attributs `root` et `scene`, le chargement du fichier FXML, la méthode `show()` et la méthode abstraite `getTitle()`.
Les sous-classes `TitleScreenView`, `SelectionView` et `PreBuildTeamView` héritent donc de ces éléments en appelant `super(cheminFxml)` dans leur constructeur, et se contentent d'implémenter `getTitle()` avec leur propre titre et de câbler leurs propres boutons.

Du côté des controllers, la classe abstraite générique `Controller<T extends View>` centralise les attributs `metaController` et `view` ainsi que la méthode `show()`.
Le paramètre générique `T` borné par `extends View` garantit que `this.view` est automatiquement du bon type dans chaque sous-classe : dans `SelectionController` par exemple, `this.view` est directement un `SelectionView` sans aucun cast nécessaire.
Les sous-classes `TitleScreenController`, `SelectionController` et `PreBuildTeamController` héritent tous de ces éléments via `super(metaController, new VueCorrespondante())` et définissent ensuite leur propre logique métier.

L'héritage nous évite donc la duplication de code, nous assure un typage précis grâce aux génériques et enfin elle nous permet la liaison tardive et polymorphisme dans `View.show()`par l'appel `this.getTitle()`.
### Design Patterns

Le Template Method est un pattern qui définit le squelette d'un algorithme dans une méthode abstraite, en laissant certaines étapes à des sous-classes.
Le projet l'applique à travers la classe abstraite `View`, qui impose à toutes ses sous-classes (voir [Héritage](#héritage--classes-et-méthodes-abstraites-typage-liaison-tardive-et-polymorphisme))

L'Iterator est décrit comme permettant de parcourir une collection sans exposer sa structure interne.
Le projet l'utilise à travers les boucles `for-each` sur les `List<Bugemon>` et `List<Attack>` dans `TeamBugemon` et `BugemonProvider`.

La Facade fournit une interface unifiée à un ensemble d'interfaces dans un sous-système, simplifiant l'accès à un système complexe.
Dans le projet, `MetaController` joue exactement ce rôle : il centralise toute la navigation entre les scènes via `switchScene()`, et masque aux contrôleurs spécifiques la complexité de la gestion des `Stage`, `Scene et du chargement FXML.
### Programmation défensive

#### Mécanismes de typage

(Voir [Héritage](#héritage--classes-et-méthodes-abstraites-typage-liaison-tardive-et-polymorphisme))

#### Exceptions

Dans `BugemonProvider` et `TeamBugemon` les exceptions `BugemonNotFoundException`, `BugemonNotInTeamException` et `AttackNotFoundException` sont utilisées.

#### Assertions

On n'a pas encore eu l'occasion de place des assertions.

# Itération 2

- Date : 30/03/2026

## Suivi de planification du projet
### Analyse de l'écart (Prévu vs Réalisé)
Pour cette itération, la vélocité était fixée à 28 points. Finalement, nous avons utilisé 34 points (ce qu'on c'est permis car on avait une semaine supplémentaire). Nous avons sous-estimé la complexité des tâches, notamment à cause de la confiance excessive générée par la première itération qui s'était bien déroulée.
Pour remédier à cela, nous avons réévalué ensemble le nombre d'heures pour chaque histoire afin d'essayer de coller au mieux entre nos estimations et la réalité pour l'itération 3.
Dans notre nouvelle méthode de travail de la seconde itération, en limitant les sous-taches (qui ne sont pas définies explicitement) à 2h max, certain membre se rendent compte qu'ils finissent la tâche en entier sans pouvoir laisser de travail pour la prochaine paire pilote copilote. Ce qui explique pourquoi il y a 0 point pour certaines sous-tâches.

Suite à notre réunion avec le coach, il a également été relevé une grande inégalité dans le nombre de commits entre les membres : certains avaient 25 commits ou plus tandis que d'autres en avaient 0 ou 1. Ce déséquilibre était en partie dû à une mauvaise pratique de l'itération 1, où au lieu de merger les branches vers le main, un membre du groupe se chargeait de copier-coller le code des branches directement sur le main, ce qui faisait que les commits sur les branches n'étaient pas comptabilisés. Ce problème a été corrigé durant cette seconde itération.
Malgré cela, une grande inégalité persiste dans les commits.
Cependant, une grande inégalité persiste dans les commits. Cela est dû au fait que certains membres du groupe sont trop investis et prennent des tâches qui ne leur sont pas attribuées. Nous en avons discuté en réunion et nous verrons si la situation s'améliore pour l'itération 3.
### Gestion des risques (R1, R2, R3)
Pour l'histoire 8, nous avions attribué un risque R3. L'implémentation de l'EffectManager s'est avérée plus complexe que prévu notamment pour la gestion des effets temporaires et leur expiration, le risque aurait dû être évalué à R2.
### Commentaires sur la Burndown Chart
Lors de la [répartition des tâches](repartition_des_taches.md#itération-2) sur la deuxième itération, on a fait en sorte d'enchainer 1 à 2h de travail par paire de pilote copilote avec certains jours où plusieurs paires peuvent travailler sur la même journée.
Cette méthodologie fait qu'en théorie, la courbe réelle de la burndown chart sera forcément en moyenne en dessous de la droite idéale.
## Motivation des choix pris lors de la conception
### Structure du code
Par rapport à l'itération 1, la structure des packages a été profondément réorganisée pour mieux respecter les principes de cohésion et de séparation des responsabilités du cours.
```
├── configuration
├── controllers
│   ├── fight
│   └── teamchoice
├── exceptions
├── listener
├── models
│   ├── bugemon
│   │   └── dto
│   └── game
│       └── dto
├── persistence
│   ├── addebugemon
│   └── savedteam
├── repositories
├── services
│   ├── color
│   ├── dataprovider
│   ├── fight
│   └── game
└── views
    ├── fight
    ├── teamchoice
    │   └── selection
    └── utils
```

Les changements principaux par rapport à l'itération 1 sont les suivants. Le MetaController a été renommé en ControllerManager pour mieux refléter son rôle. Les controllers et les vues ont été regroupés en sous-packages thématiques (fight/, teamchoice/) afin que chaque sous-package ait une responsabilité claire. Les services ont également été séparés en sous-packages distincts selon leur domaine. Enfin, la classe AppConfig centralise désormais tous les chemins et constantes du projet, ce qui évite les chaînes de caractères dispersées dans le code.
### Justification du modèle MVC
#### Modèle
Le modèle s'est enrichi cette itération avec l'ajout de nouvelles classes. Fight gère toute la logique d'un combat : l'ordre des tours, l'exécution des attaques et la détection de fin de combat. Item représente les objets utilisables en combat. Effect et ActiveEffect modélisent les effets secondaires des attaques et leur durée. Conformément aux principes du cours, ces classes restent totalement indépendantes de JavaFX et ne contiennent aucune logique d'affichage.
#### Vue
Les vues ont été réorganisées en sous-packages thématiques. FightView gère l'affichage du combat (sprites, barres HP, panel d'actions) et ne contient aucune logique métier. Elle délègue toutes les interactions utilisateur à un FightListener plutôt qu'au controller directement, ce qui renforce encore le découplage entre la vue et le controller. Les sous-vues FightChoicePanelSubView, ItemsView et SelectBugemonFromTeamView sont chargées via fx:include et ont chacune une responsabilité claire et unique.
#### Contrôleur
Le MetaController de l'itération 1 a été renommé en ControllerManager pour mieux refléter son rôle de gestionnaire central. Il gère désormais non seulement la navigation entre les scènes principales, mais aussi l'ouverture des sous-fenêtres modales via openItemChoiceWindow() et openSwitchWindow(). Le FightAgainstCPUController orchestre le déroulement du combat en coordonnant le modèle Fight et la vue FightView, sans jamais contenir de logique d'affichage.
#### Services et repositories
Comme prévu à l'itération 1, le BugemonProvider a été séparé en service et repositories distincts. BugemonRepository et AttackRepository gèrent l'accès aux données JSON, TeamRepository gère la persistance des équipes, et BugemonProvider, AttackProvider et TeamProvider constituent la couche service qui contient la logique métier. Le GameStateHolder est un singleton qui centralise l'état partagé entre controllers sans les coupler directement entre eux.
### Principe de la programmation orienté objet
### Encapsulation
Le projet continue de respecter l'encapsulation avec des attributs private dans tous les modèles et protected final dans les classes abstraites, conformément au principe de responsabilité unique (SRP) du cours.
### Interfaces
Une nouveauté importante de cette itération est l'introduction de l'interface FightListener qui découple complètement FightView de FightAgainstCPUController : la vue ne connaît que l'interface, jamais le controller concret. Cela suit exactement le principe de découplage par interface décrit dans le cours, où l'on préfère dépendre d'abstractions plutôt que de classes concrètes (DIP — Dependency Inversion Principle). Ainsi, modifier ou remplacer le FightAgainstCPUController n'impacte pas la vue.
De même, les vues n'accèdent aux modèles qu'à travers les interfaces DTO BugemonDTO, TeamBugemonDTO et ItemDTO, ce qui protège les modèles de toute modification directe depuis les vues et respecte le principe de ségrégation des interfaces (ISP). Il n'existe pas d'AttackDTO car les attaques ne sont pas des entités indépendantes dans les vues — leurs informations sont accessibles directement via les méthodes de BugemonDTO.
### Héritage
Le projet utilise toujours l'héritage à deux niveaux distincts : la hiérarchie des vues et la hiérarchie des controllers, comme à l'itération 1.
Cette itération enrichit ces hiérarchies avec de nouvelles sous-classes : FightView, FightAgainstCPUController, ItemChoiceController et SwitchBugemonController héritent tous des classes abstraites View et Controller<T extends View> sans dupliquer de code, ce qui respecte le principe Open/Closed (OCP) : on étend le système sans modifier les classes existantes.
### Classes et méthodes abstraites
La classe abstraite View impose à toutes ses sous-classes d'implémenter la méthode abstraite getTitle(). De même, Controller<T extends View> centralise les attributs et méthodes communs à tous les controllers. Ces classes abstraites empêchent l'instanciation directe et garantissent que chaque sous-classe respecte le contrat défini.
### Singleton — GameStateHolder
Le GameStateHolder est un singleton dans services/game/ qui centralise l'état partagé de la session de jeu. Sans lui, des objets modèles comme TeamBugemon transiteraient en arguments entre les controllers ce qui n'est pas idéal.

Il stocke deux types de données :

currentTeamPlayer et savedTeams : état inter-scènes, qui persiste entre les navigations
currentPlayerTeamDTO, currentOnFieldId, switchOnPurpose, availableItems : état interne au combat, utilisé par SwitchBugemonController et ItemChoiceController

Avant de naviguer vers le combat, un controller écrit dans le holder :
javaGameStateHolder.getInstance().setCurrentTeamPlayer(playerTeam);
metaController.goToFight();
Les controllers SwitchBugemonController et ItemChoiceController lisent directement depuis le holder sans recevoir aucun argument. À la fin du combat on peut aussi écrire l'état actuel de la team (Bugémons en vie, leurs PV restants, etc.) pour les combats d'après (feature pas encore implémentée), de même pour l'état de l'inventaire.

Un singleton garantit une seule instance durant toute l'exécution, ce qui correspond exactement au pattern Singleton décrit dans le cours avec constructeur private et méthode getInstance().

# Itération 3

- Date : 12/04/2026
## Suivi de planification du projet

### Analyse de l'écart (Prévu vs Réalisé)
Pour cette itération, la vélocité était fixée à 28 points. Nous avons réalisé 27,5 points. Cette amélioration s'explique par le fait qu'en analysant notre manière de travailler sur les 2 itérations précédentes, nous comprenons mieux ce qu'il faut faire et comment le faire, ce qui nous permet désormais d'estimer bien plus précisément la charge de travail réelle de chaque histoire.
Lors de la répartition des tâches en sous-tâches, nous avons constaté que l'histoire 7 prendrait un peu plus de temps que prévu et l'histoire 9 un peu moins. Nous avons donc simplement échangé les heures supplémentaires entre ces deux histoires tout en restant aux alentour de 28 points au total.
Par rapport aux itérations précédentes, nous avons amélioré la répartition du nombre de commits entre les membres. La discussion que nous avions eue lors de l'itération 2 sur le fait que certaines personnes en faisaient trop a porté ses fruits, même si un déséquilibre persiste encore, nous sommes sur la bonne voie et nous allons continuer dans cette direction pour l'itération 4.
### Gestion des risques (R1, R2, R3)
Pour l'histoire 7 (expérience et montée de niveau), nous avions attribué un risque R3. Ce risque s'est avéré sous-estimé car nous pensions que plusieurs fonctionnalités nécessaires seraient déjà implémentées dans les histoires précédentes, ce qui n'était pas le cas. Le risque aurait dû être évalué à R2.
Pour l'histoire 9 (structure de la Tour NO), nous avions attribué un risque R1. Cette histoire s'est très bien déroulée sans aucune difficulté particulière, le risque élevé était donc surestimé.
Pour l'histoire 10 (utiliser un objet), nous avions attribué un risque R3. Cette histoire s'est déroulée sans difficulté, la majeure partie du travail étant déjà réalisée dans les histoires précédentes.
### Commentaires sur la Burndown Chart
La burndown chart de cette itération montre une progression en deux temps. Durant la première moitié de l'itération (jours 1 à 7), la courbe réelle descend nettement plus vite que la courbe idéale, témoignant d'un rythme de travail soutenu en début d'itération. La seconde moitié est plus calme, la courbe réelle et idéale se rejoignant progressivement vers le jour 13. Cette allure s'explique par le fait que nous préférons terminer les histoires rapidement afin de disposer de temps en fin d'itération pour corriger les éventuels commentaires du coach avant la remise finale.
## Motivation des choix pris lors de la conception
### Structure du code
Par rapport à l'itération 2, la structure des packages a évolué avec l'ajout de nouveaux sous-packages liés aux nouvelles histoires implémentées.
La nouvelle structure est la suivante :
```
├── configuration
├── controllers
│   ├── afterfight
│   ├── fight
│   ├── teamchoice
│   └── tower
├── exceptions
├── models
│   ├── bugemon
│   │   └── dto
│   └── game
│       └── dto
├── persistence
├── repositories
├── services
│   ├── color
│   ├── dataprovider
│   ├── fight
│   └── game
└── views
├── afterfight
├── fight
├── listener
├── teamchoice
│   └── selection
├── tower
└── utils
```
Les changements principaux par rapport à l'itération 2 sont les suivants. Un nouveau sous-package controllers/afterfight/ regroupe les controllers VictoryController, DefeatController et LevelUpController qui gèrent tous les écrans après un combat. 
De même, un sous-package views/afterfight/ et views/tower/ ont été créés pour leurs vues associées. Le package listener/ a été déplacé dans views/listener/ pour mieux refléter le fait que les interfaces listener sont définies du côté des vues. Enfin, JsonRepository a été introduite comme classe abstraite de base pour presque tous les repositories.
Seul TeamRepository n'hérite pas de JsonRepository car il ne lit pas ses données depuis le package resources, contrairement aux autres repositories, mais depuis le package persistence, dédié aux données propres à l'utilisateur qui ne doivent pas être versionnées sur Git. 
Si l'histoire 13 "Ajouter des Bugémons" est implémentée, il sera pertinent d'introduire une méthode générique readJSONFromPersistence dans JsonRepository et de créer une classe CustomBugemonRepository qui en hérite, afin d'éviter la duplication de logique similaire entre les deux types de lecture. Nous garderons cette piste en tête pour une itération 4.
### Justification du modèle MVC
#### Modèle
Le modèle s'est enrichi cette itération avec l'ajout de nouvelles classes. LevelUpBonus représente un bonus de montée de niveau avec ses quatre composantes (HP, attaque, défense, initiative) et sa méthode applyTo(Bugemon) qui applique directement le bonus sur le Bugémon concerné. LevelTower gère la progression dans la Tour NO avec son pattern fixe de 6 étapes par étage. TowerNodeType est un enum qui représente les trois types de nœuds possibles (COMBAT, REWARD, BOSS). Conformément aux principes du cours, ces classes restent totalement indépendantes de JavaFX.
#### Vue
Les vues ont été enrichies avec plusieurs nouveaux écrans. LevelUpView affiche le Bugémon qui monte de niveau, son sprite, le message de niveau atteint et les trois boutons d'options de bonus proposées au joueur. VictoryView affiche l'XP totale gagnée ainsi que la liste de tous les Bugémons de l'équipe avec leur XP individuelle. RewardView affiche la récompense obtenue après une case bonus et un bouton "Continuer". ItemsView est une sous-fenêtre modale affichant la liste des objets disponibles via une ListView<ItemDTO> et permet au joueur de sélectionner et utiliser un objet durant le combat.
#### Contrôleur
Le LevelUpController orchestre l'enchaînement des montées de niveau après un combat : il gère la file de Bugémons à faire monter de niveau, affiche les options de bonus et applique le choix du joueur avant de passer au Bugémon suivant ou de continuer dans la tour. La logique de progression dans la tour est désormais entièrement dans ControllerManager via goToNextTowerStep(), ce qui évite la création d'un controller dédié inutile.
#### Services et repositories
Cette itération ajoute plusieurs nouveaux services. ExperienceService calcule et distribue l'XP après un combat selon la formule de la spécification. LevelUpBonusGenerator génère les 3 options de bonus proposées au joueur lors d'une montée de niveau. ItemEffectApplier applique les effets des objets sur les Bugémons. ItemProvider et ItemRepository gèrent l'accès aux données des objets. Enfin, JsonRepository est introduite comme classe abstraite générique dont héritent tous les repositories, centralisant la logique de désérialisation JSON.
Suite à notre deuxième réunion avec le coach, nous avons revu l'architecture de notre couche de données. Initialement, les Provider étaient implémentés en tant que singletons tandis que les Repository utilisaient des méthodes statiques. Après discussion, nous avons réalisé que cette approche inversait les responsabilités : le pattern singleton est particulièrement pertinent lorsqu'on a besoin d'un point d'accès unique à une ressource partagée, ce qui correspond exactement au rôle des Repository qui encapsulent l'accès aux fichiers JSON. Les Provider, quant à eux, n'ont pas cette contrainte et peuvent être instanciés directement là où on en a besoin. Nous avons donc inversé la logique : les Repository (BugemonRepository, AttackRepository, ItemRepository, TeamRepository) sont désormais des singletons, garantissant qu'un seul chargement du fichier JSON est effectué, tandis que les Provider sont de simples classes instanciables.
Afin de s'assurer que l'initialisation des Bugémons du BugemonRepository — assignation des attaques, des couleurs et des PV — ne soit effectuée qu'une seule fois quel que soit le nombre d'instanciations du BugemonProvider, nous avons introduit un booléen statique initialized qui garantit que ce traitement n'est exécuté qu'au premier appel du constructeur.
Par ailleurs, nous avons corrigé une violation MVC dans le modèle TeamBugemon, dont certains constructeurs faisaient appel directement à BugemonProvider pour résoudre des IDs en objets Bugémon. Cette dépendance d'un modèle envers une couche de service étant une mauvaise pratique, ces constructeurs ont été refactorisés pour recevoir directement une liste de Bugémons déjà résolue, la responsabilité de l'appel au Provider étant désormais déléguée aux controllers.
### Principe de la programmation orientée objet
#### Encapsulation
Le projet continue de respecter l'encapsulation avec des attributs private final dans les nouvelles classes modèles comme LevelUpBonus, dont tous les attributs sont immuables. Cela garantit l'intégrité des données et évite toute modification accidentelle.
#### Interfaces
Les interfaces listener FightListener et SelectBugemonFromTeamListener sont désormais regroupées dans views/listener/ ce qui renforce leur rôle d'interface de communication entre la vue et le controller. Aucune nouvelle interface n'a été ajoutée cette itération mais leur déplacement clarifie l'architecture.
#### Héritage
Cette itération enrichit les hiérarchies existantes avec LevelUpController, LevelUpView, VictoryController (refactorisé), DefeatController et RewardController qui héritent tous de Controller<T extends View> et View sans dupliquer de code.
#### Classes et méthodes abstraites
JsonRepository est introduite comme classe abstraite générique dont héritent tous les repositories (BugemonRepository, AttackRepository, ItemRepository, TeamRepository). Elle centralise la logique de désérialisation JSON via la méthode statique générique readJSONFromAssets(String path, Class<W> wrapperClass) et l'interface interne JsonWrapper<T> que chaque repository implémente. C'est un exemple typique du pattern Template Method du syllabus appliqué aux repositories.
### Design Patterns
Le Template Method est toujours appliqué via la classe abstraite View et ses sous-classes. Cette itération l'étend également à JsonRepository : la méthode readJSONFromAssets() définit le squelette de la désérialisation JSON et chaque repository fournit sa propre classe wrapper via l'interface JsonWrapper<T>.
Le Singleton est désormais appliqué à deux niveaux : le GameStateHolder comme à l'itération 2, et les repositories (BugemonRepository, AttackRepository, ItemRepository, TeamRepository) qui garantissent chacun qu'un seul chargement du fichier JSON est effectué durant toute l'exécution.
Le Prototype est toujours utilisé via les copies profondes Bugemon(Bugemon other), TeamBugemon(TeamBugemon other) et Stats(Stats other) pour isoler les données entre les équipes en combat.
L'Iterator est toujours utilisé à travers les boucles for-each sur les listes de Bugémons, d'attaques et d'effets actifs.
La Facade est toujours assurée par le ControllerManager qui centralise toute la navigation et masque la complexité de la gestion des scènes et sous-fenêtres modales.
L'Observer est toujours appliqué via les interfaces FightListener et SelectBugemonFromTeamListener, désormais regroupées dans views/listener/.
### Programmation défensive
#### Mécanismes de typage
Le typage générique Controller<T extends View> et JsonRepository avec son interface interne JsonWrapper<T> garantissent toujours le bon typage sans cast. Les interfaces DTO protègent toujours les modèles d'une modification directe depuis les vues.
#### Exceptions
Cette itération apporte une refonte complète de la gestion des erreurs suite au feedback du coach. Initialement, les exceptions étaient soit ignorées silencieusement avec un simple System.err.println, soit converties en RuntimeException sans retour visuel à l'utilisateur. Désormais, dans les couches inférieures comme les repositories et les services, les exceptions sont déclarées dans les signatures de méthodes via throws et remontent jusqu'aux controllers sans être catchées inutilement en chemin. C'est au niveau des controllers que l'exception est finalement traitée, ce qui permet de donner un retour visuel clair à l'utilisateur.
Lorsque c'est possible, on préfère faire remonter l'exception jusqu'au ControllerManager plutôt que de la catcher dans chaque controller individuellement, car cela centralise la logique de gestion d'erreur et rend le code plus lisible. C'est le ControllerManager qui affiche le message d'erreur via sa méthode fatalError(), laquelle délègue l'affichage à FatalErrorView. Cette classe n'hérite pas de View car elle n'est associée à aucun controller — elle est statique et indépendante du cycle de vie des scènes JavaFX.
De nouvelles exceptions ont également été ajoutées : JsonResourceNotFoundException, LoadingTeamFailedException, SavingTeamFailedException et TowerNotInitializedException, chacune représentant un cas d'erreur précis et explicite.
#### Assertions
Les tests JUnit constituent toujours notre principale forme de programmation défensive. Cette itération ajoute ExperienceServiceTest, LevelUpBonusGeneratorTest, LevelUpBonusTest, StatsTest, ItemRepositoryTest et enrichit les tests existants, couvrant notamment le calcul d'XP, la génération des options de bonus, la montée de niveau et l'application des bonus sur les stats.

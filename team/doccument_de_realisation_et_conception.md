# Document de réalisation et conception (DRC)

- Groupe : 5
- Date : 09/03/2026

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
    - [Principe de la programmation orientée objet](#principe-de-la-programmation-orientée-objet)
      - [Encapsulation (+ interfaces)](#encapsulation--interfaces)
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
    - [Commentaires sur la Burndown Chart](#commentaires-sur-la-burndown-chart-1)
  - [Motivation des choix pris lors de la conception](#motivation-des-choix-pris-lors-de-la-conception-1)
    - [Structure du code](#structure-du-code-1)
    - [Justification du modèle MVC](#justification-du-modèle-mvc-1)
      - [Modèle](#modèle-1)
      - [Vue](#vue-1)
      - [Contrôleur](#contrôleur-1)
      - [Services et repositories](#services-et-repositories-1)
  - [Principe de la programmation orientée objet](#principe-de-la-programmation-orientée-objet-1)
    - [Encapsulation](#encapsulation-1)
    - [Interfaces](#interfaces)
    - [Héritage](#héritage-1)
    - [Classes et méthodes abstraites](#classes-et-méthodes-abstraites)
    - [Typage et génériques](#typage-et-génériques)
    - [Singleton — GameStateHolder](#singleton--gamstateholder)
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
Malgré cela, une grande inégalité persiste dans les commits. Cela est dû au fait que certains membres du groupe sont trop investis et prennent des tâches qui ne leur sont pas attribuées. Nous en avons discuté en réunion et nous verrons si la situation s'améliore pour l'itération 3.
### Gestion des risques (R1, R2, R3)
Pour l'histoire 8, nous avions attribué un risque R3. L'implémentation de l'EffectManager s'est avérée plus complexe que prévu notamment pour la gestion des effets temporaires et leur expiration, le risque aurait dû être évalué à R2.
### Commentaires sur la Burndown Chart
Lors de la [répartition des tâches](repartition_des_taches.md#itération-2) sur la deuxième itération, on a fait en sorte d'enchainer 1 à 2h de travail par paire de pilote copilote avec certains jours où plusieurs paires peuvent travailler sur la même journée.
Cette méthodologie fait qu'en théorie, la courbe réelle de la burndown chart sera forcément en moyenne en dessous de la droite idéale.
## Motivation des choix pris lors de la conception
### Structure du code
Par rapport à l'itération 1, la structure des packages a été profondément réorganisée pour mieux respecter les principes de cohésion et de séparation des responsabilités du cours.

models/bugemon/
<br>models/bugemon/dto/
<br>models/game/
<br>models/game/dto/
<br>controllers/
<br>controllers/fight/
<br>controllers/teamchoice/
<br>views/
<br>views/fight/
<br>views/teamchoice/
<br>views/teamchoice/selection/
<br>views/utils/
<br>services/dataprovider/
<br>services/fight/
<br>services/game/
<br>services/color/
<br>repositories/
<br>listener/
<br>configuration/
<br>exceptions/
<br>persistence/

Les changements principaux par rapport à l'itération 1 sont les suivants. Le MetaController a été renommé en ControllerManager pour mieux refléter son rôle. Les controllers et les vues ont été regroupés en sous-packages thématiques (fight/, teamchoice/) afin que chaque sous-package ait une responsabilité claire. Les services ont également été séparés en sous-packages distincts selon leur domaine. Enfin, la classe AppConfig centralise désormais tous les chemins et constantes du projet, ce qui évite les chaînes de caractères dispersées dans le code.
### Justification du modèle MVC
#### Modèle
Le modèle s'est enrichi cette itération avec l'ajout de nouvelles classes. Fight gère toute la logique d'un combat : l'ordre des tours, l'exécution des attaques et la détection de fin de combat. Item représente les objets utilisables en combat. Effect et ActiveEffect modélisent les effets secondaires des attaques et leur durée. Conformément aux principes du cours, ces classes restent totalement indépendantes de JavaFX et ne contiennent aucune logique d'affichage.
#### Vue
Les vues ont été réorganisées en sous-packages thématiques. FightView gère l'affichage du combat (sprites, barres HP, panel d'actions) et ne contient aucune logique métier. Elle délègue toutes les interactions utilisateur à un FightListener plutôt qu'au controller directement, ce qui renforce encore le découplage entre la vue et le controller. Les sous-vues FightChoicePanelSubView, ItemsView et SelectBugemonFromTeamView sont chargées via fx:include et ont chacune une responsabilité claire et unique.
#### Contrôleur
Le MetaController de l'itération 1 a été renommé en ControllerManager pour mieux refléter son rôle de gestionnaire central. Il gère désormais non seulement la navigation entre les scènes principales, mais aussi l'ouverture des sous-fenêtres modales via openItemChoiceWindow() et openSwitchWindow(). Le FightController orchestre le déroulement du combat en coordonnant le modèle Fight et la vue FightView, sans jamais contenir de logique d'affichage.
#### Services et repositories
Comme prévu à l'itération 1, le BugemonProvider a été séparé en service et repositories distincts. BugemonRepository et AttackRepository gèrent l'accès aux données JSON, TeamRepository gère la persistance des équipes, et BugemonProvider, AttackProvider et TeamService constituent la couche service qui contient la logique métier. Le GameStateHolder est un singleton qui centralise l'état partagé entre controllers sans les coupler directement entre eux.
### Principe de la programmation orienté objet
### Encapsulation
Le projet continue de respecter l'encapsulation avec des attributs private dans tous les modèles et protected final dans les classes abstraites, conformément au principe de responsabilité unique (SRP) du cours.
### Interfaces
Une nouveauté importante de cette itération est l'introduction de l'interface FightListener qui découple complètement FightView de FightController : la vue ne connaît que l'interface, jamais le controller concret. Cela suit exactement le principe de découplage par interface décrit dans le cours, où l'on préfère dépendre d'abstractions plutôt que de classes concrètes (DIP — Dependency Inversion Principle). Ainsi, modifier ou remplacer le FightController n'impacte pas la vue.
De même, les vues n'accèdent aux modèles qu'à travers les interfaces DTO BugemonDTO, TeamBugemonDTO, AttackDTO et ItemDTO, ce qui protège les modèles de toute modification directe depuis les vues et respecte le principe de ségrégation des interfaces (ISP).
### Héritage
Le projet utilise toujours l'héritage à deux niveaux distincts : la hiérarchie des vues et la hiérarchie des controllers, comme à l'itération 1.
Cette itération enrichit ces hiérarchies avec de nouvelles sous-classes : FightView, FightController, ItemChoiceController et SwitchBugemonController héritent tous des classes abstraites View et Controller<T extends View> sans dupliquer de code, ce qui respecte le principe Open/Closed (OCP) : on étend le système sans modifier les classes existantes.
### Classes et méthodes abstraites
La classe abstraite View impose à toutes ses sous-classes d'implémenter la méthode abstraite getTitle(). De même, Controller<T extends View> centralise les attributs et méthodes communs à tous les controllers. Ces classes abstraites empêchent l'instanciation directe et garantissent que chaque sous-classe respecte le contrat défini.
### Typage et génériques
Le paramètre générique T borné par extends View dans Controller<T extends View> garantit le bon typage dans chaque sous-classe sans cast nécessaire. Dans SelectionController par exemple, this.view est directement un SelectionView sans aucun cast.
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

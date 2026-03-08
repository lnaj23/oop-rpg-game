# Document de réalisation et conception (DRC)

- Groupe : 5
- Date : 08/03/2026

### Sommaire

- [Suivi de planification du projet](#suivi-de-planification-du-projet)
  - [Analyse de l'écart (Prévu vs Réalisé)](#analyse-de-lécart-prévu-vs-réalisé)
  - [Gestion des risques (R1, R2, R3)](#gestion-des-risques-r1-r2-r3)
- [Motivation des choix pris lors de la conception](#motivation-des-choix-pris-lors-de-la-conception)
  - [Structure du code](#structure-du-code)
  - [Justification du modèle MVC](#justification-du-modèle-mvc)
    - [Modèle](#modèle-)
    - [Vue](#vue)
    - [Contrôleur](#contrôleur)
    - [Services et repositories](#services-et-repositories)
  - [Principe de la programmation orienté objet](#principe-de-la-programmation-orienté-objet)
    - [Encapsulation (+ interfaces)](#encapsulation--interface)
    - [Héritage (+ classes et méthodes abstraites, typage, liaison tardive et polymorphisme)](#héritage--classes-et-méthodes-abstraites-typage-liaison-tardive-et-polymorphisme)
  - [Programmation défensive](#programmation-défensive)
    - [Mécanisme de typage](#mécanismes-de-typage)
    - [Exceptions](#exceptions-)

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
<br>`services/`
<br>`exceptions/`

Ensuite, chaque classe a une responsabilité unique et claire avec des noms explicite. Cependant, il existe tout de même des classes inutiles pour l'instant comme `Effects.java` ou `Attack.java`, mais qui nous serviront pour d'autres potentielles histoires.

Enfin la majorité de nos méthodes sont courtes et bien nommées.

### Justification du modèle MVC
Durant la première itération, nous avons directement implémenté nos codes afin de respecter l'architecture MVC en se basant sur le TP3 et le cours [Java](https://fpluquet.github.io/cours-java/cours/mvc/1-introduction.html).

#### Modèle 
Le modèle, comprenant des classes comme `Bugemon`, `Attack`, `Stats` et `TeamBugemon`, gère exclusivement les données et la logique métier de l'application. Conformément aux principes du cours, ces classes sont totalement indépendantes de l'interface graphique et ne contiennent aucune référence à JavaFX, ce qui permet de les tester ou de les réutiliser facilement sans modifier l'affichage. 
#### Vue
La vue ne doit pas contenir de logiques métiers et ne modifie pas directement les données. Elle délègue toute action au contrôleur. Dans le projet, `TitleScreenView`, `SelectionView` et `PreBuildTeamView` héritent toutes de la classe abstraite View qui centralise le chargement FXML, et communiquent uniquement avec leur contrôleur dédié via des appels.
Les vues n'accèdent aux modèles qu'à travers les interfaces DTO `BugemonDTO` et `TeamBugemonDTO`.
#### Contrôleur
Le contrôleur reçoit les actions de l'utilisateur, modifie le modèle et demande à la vue de se mettre à jour. Dans le projet, `MetaController` joue parfaitement ce rôle central en gérant la navigation entre toutes les scènes via `switchScene(), sans que les vues se connaissent entre elles.

#### Services et repositories

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

### Programmation défensive

#### Mécanismes de typage

(Voir [Héritage](#héritage--classes-et-méthodes-abstraites-typage-liaison-tardive-et-polymorphisme))

#### Exceptions 

Dans BugemonProvider et TeamBugemon les exceptions `BugemonNotFoundException`, `BugemonNotInTeamException` et `AttackNotFoundException` sont utilisées.

#### Assertions


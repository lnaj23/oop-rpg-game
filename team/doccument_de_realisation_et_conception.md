# Document de réalisation et conception (DRC)

- Groupe : 5
- Date : 09/03/2026

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
  - [Design Patterns](#design-patterns)
  - [Programmation défensive](#programmation-défensive)
    - [Mécanisme de typage](#mécanismes-de-typage)
    - [Exceptions](#exceptions-)
    - [Assertions](#assertions) 

## Suivi de planification du projet

## Motivation des choix pris lors de la conception

### Justification du modèle MVC
Durant la première itération, nous avons directement implémenté nos codes afin de respecter l'architecture MVC.

La couche Modèle, comprenant des classes comme Bugemon, Attack, Stats et TeamBugemon, gère exclusivement les données et la logique métier de l'application. Conformément aux principes du cours, ces classes sont totalement indépendantes de l'interface graphique et ne contiennent aucune référence à JavaFX, ce qui permet de les tester ou de les réutiliser facilement sans modifier l'affichage. Le BugemonProvider s'occupe du chargement des fichiers JSON ainsi que de la lecture de celle-ci.

La Vue est scindée en deux parties : le déclaratif avec les fichiers (menuview.fxml, selectionview.fxml) pour la structure, et les classes Java comme MenuView pour l'initialisation. Cette séparation permet de découpler l'apparence visuelle de la logique de contrôle. La vue reste passive et ne traite aucune logique « business » ; elle se contente d'afficher les informations que le contrôleur lui transmet, comme les noms et sprites des Bugemons.

Le Contrôleur, représenté par MenuController et SelectionController, sert de lien entre le modèle et la vue. Il intercepte les actions de l'utilisateur et orchestre la mise à jour de l'application. Dans SelectionController.java, le contrôleur récupère les données via le BugemonProvider (Modèle) pour ensuite mettre à jour dynamiquement les éléments de la vue. Comme recommandé dans les exercices du TP, nos contrôleurs ne gèrent plus directement la structure graphique, mais manipulent les composants injectés via @FXML.
# Document de réalisation et conception (DRC)

- Groupe : 5
- Date : 26/02/2026

## Suivi de planification du projet

### Analyse de l'écart (Prévu vs Réalisé)

Nous avons initialement alloué une période importante à la découverte de notre environnement de travail (IntelliJ, Java, JUnit,...). Finalement, l'utilisation de ces outils s’est révélée moins complexe que nous l'avions imaginé.

### Gestion des risques (R1, R2, R3)

Pour l'histoire 1, nous avons attribué un risque R2 ce qui s'est ressenti après le TP3 car nous avons remarqué que nous ne respections pas correctement le modèle MVC comme ce qui est présenté dans le TP. Nous avons donc effectué un grand refactoring afin de garantir d'avoir une base solide respectant le MVC pour les itérations suivants.

## Motivation des choix pris lors de la conception

### Justification du modèle MVC
Durant la première itération, nous avons directement implémenté nos codes afin de respecter l'architecture MVC.

La couche Modèle, comprenant des classes comme Bugemon, Attack, Stats et TeamBugemon, gère exclusivement les données et la logique métier de l'application. Conformément aux principes du cours, ces classes sont totalement indépendantes de l'interface graphique et ne contiennent aucune référence à JavaFX, ce qui permet de les tester ou de les réutiliser facilement sans modifier l'affichage. Le BugemonProvider s'occupe du chargement des fichiers JSON ainsi que de la lecture de celle-ci.

La Vue est scindée en deux parties : le déclaratif avec les fichiers (menuview.fxml, selectionview.fxml) pour la structure, et les classes Java comme MenuView pour l'initialisation. Cette séparation permet de découpler l'apparence visuelle de la logique de contrôle. La vue reste passive et ne traite aucune logique « business » ; elle se contente d'afficher les informations que le contrôleur lui transmet, comme les noms et sprites des Bugemons.

Le Contrôleur, représenté par MenuController et SelectionController, sert de lien entre le modèle et la vue. Il intercepte les actions de l'utilisateur et orchestre la mise à jour de l'application. Dans SelectionController.java, le contrôleur récupère les données via le BugemonProvider (Modèle) pour ensuite mettre à jour dynamiquement les éléments de la vue. Comme recommandé dans les exercices du TP, nos contrôleurs ne gèrent plus directement la structure graphique, mais manipulent les composants injectés via @FXML.
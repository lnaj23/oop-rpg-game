# Histoires mises à jour

- Groupe : 5
- Date : 22/03/2026
- Itération 2

## Présentation et contenu des histoires
| Titre       | Description                                                                                                                                  | Risque | Points | Itération | À faire | En cours | Complétée |
|-------------|----------------------------------------------------------------------------------------------------------------------------------------------|--------|--------|-----------|---------|----------|-----------|
| Histoire 1  | Le joueur compose une équipe de 1 à 6 Bugémons uniques via une interface affichant leurs noms, sprites et statistiques                       | 2      | 21     | 1         |         |          | X         |
| Histoire 2  | Le joueur peut saauvegarder une équipe de façon permanete ou lancer le jeu avec une équipe éphémère non sauvegardé.                          | 3      | 3      | 2         |         | X        |           | 
| Histoire 3  | Un menu dédié permet de renommer, modifier, supprimer ou utiliser une équipe sauvegardée pour lancer une partie.                             | 2      | 13     | 1         |         |          | X         |
| Histoire 4  | Un combat se lance contre une équipe aléatoire, avec un affichage permanent des statistiques et une boîte de dialogue décrivant les actions. | 1      | 13     | 2         |         | X        |           |
| Histoire 5  | Le joueur prend le contrôle des actions de son premier Bugémon tandis que l'ordinateur continue de jouer automatiquement.                    | 2      | 3      | 2         |         | X        |           |
| Histoire 6  | Le calcul de dégâts utilise la formule complète intégrant les statistiques, l'initiative, les types et les coups critiques.                  | 2      | 3      | 2         | X       |          |           |
| Histoire 7  |                                                                                                                                              | 3      | 5      |           |         |          |           | 
| Histoire 8  | Le système gère l'application des soins et des modificateurs de statistiques temporaires ou permanents lors des attaques.                    | 3      | 4      | 2         | X       |          |           |
| Histoire 9  |                                                                                                                                              | 1      | 8      |           |         |          |           |
| Histoire 10 |                                                                                                                                              | 3      | 5      |           |         |          |           |
| Histoire 11 |                                                                                                                                              | 2      | 5      |           |         |          |           |
| Histoire 12 |                                                                                                                                              | 3      | 5      |           |         |          |           |
| Histoire 13 |                                                                                                                                              | 1      | 5      |           |         |          |           |
| Histoire 14 |                                                                                                                                              | 2      | 8      |           |         |          |           |
| Histoire 15 |                                                                                                                                              | 2      | 5      |           |         |          |           |
| Histoire 16 |                                                                                                                                              | 3      | 3      |           |         |          |           |
| Histoire 17 |                                                                                                                                              | 2      | 21     |           |         |          |           |
| Histoire 18 |                                                                                                                                              | 3      | 8      |           |         |          |           |
| Histoire 19 |                                                                                                                                              | 1      | 21     |           |         |          |           |
| Histoire 20 |                                                                                                                                              | 2      | 5      |           |         |          |           |
| Histoire 21 |                                                                                                                                              | 3      | 3      |           |         |          |           |
| Histoire 22 |                                                                                                                                              | 1      | 13     |           |         |          |           |
| Histoire 23 |                                                                                                                                              | 2      | 5      |           |         |          |           |
| Histoire 24 |                                                                                                                                              | 3      | 3      |           |         |          |           |
| Histoire 25 |                                                                                                                                              | 2      | 13     |           |         |          |           |
| Histoire 26 |                                                                                                                                              | 2      | 5      |           |         |          |           |
| Histoire 27 |                                                                                                                                              | 3      | 2      |           |         |          |           |
| Histoire 28 |                                                                                                                                              | 2      | 8      |           |         |          |           |


## Séparation histoires en sous taches 1/3

Tache général : Se mettre à jour sur les TP3 c-a-d Meta Controller, MVC, DTO, JSON, ect. Surtout le MVC, comprendre vraiment le code actuel. Faut vraiment que le code soit correct dés le debut

---
**HISTOIRE 2**
**Comportement de la SelectionView :**

- Renommer le bouton "Valider" en "Sauvegarder" (voir maquette)
- Après sauvegarde, rester sur la `SelectionView`
- Ajouter un bouton "Mes équipes" pour naviguer vers la `PreBuildTeamView`
- Ajouter un bouton "Lancer la partie" (ne fait rien pr l'instant)

**Persistance :**

- Supprimer la `List<TeamBugemon>` partagée entre `SelectionController` et `PreBuildTeamController`
- Lors de la sauvegarde d'une équipe, sérialiser celle-ci en JSON dans `persistence/saveteam/` (un fichier par équipe, nommé d'après le nom de l'équipe)
- Lors de l'ouverture de la `PreBuildTeamView`, charger tous les fichiers JSON présents dans `persistence/saveteam/` et les afficher dans la `ListView`
- Les opérations rename et delete agissent directement sur les fichiers JSON correspondants
- Créer une classe `TeamRepository` dans le package `repositories` qui s'occupe de toutes les opérations sur les fichiers JSON : save, load, loadAll, rename, delete

\+ faire des Test pout le `TeamRepository` !!

---

**HISTOIRE 4/5 (une seule grosse histoire)** : PAS DE COMBAT AUTOMATIQUE

- **Initialiser `FightView` et `FightController`** : de préférence qql qui n'a pas encore fait du MVC,
  à faire en parallèle des models, car c'est relativement court donc à faire rapidement aussi

    - Créer `fight.fxml` avec tous les éléments de la maquette (+label pr le text) et le lier à une classe `FightView`
    - Créer un `FightController` vide et l'enregistrer comme nouvelle scène dans le `MetaController`
    - Ajouter un bouton "Lancer la partie" dans `SelectionView` et `PreBuildTeamView` qui navigue vers la `FightView`. La vue doit seule s'afficher (avec 2 image de bugemon hard codé) rien d'autre

- **models :**

Updates des models actuels :

- `Stats` : Faire bien la distinction entre les pv max et les pv actuels qui diminuent
  (pour les barres de vie, il faudra le pourcentage de pv restant)
    + getter pour la classe `Bugemon`
- `Bugemon` :
    - `takeDamage(float damage)`
    - `String attack(Attack attack, Bugemon target)` (return un String décrivant l'action)
    - `boolean isKO()`
    - `chooseRandomAttack()` (pour le CPU)
    - ect
      Test pas nécessaire je pense, ça sera surtout pr l'histoire 6

- `TeamBugemon` :
    - `Bugemon onFieldBugemon`
    - `AttackTeamEnnemi(TeamBugemon target)`
    - `boolean isStillStanding()`
    - `List<Bugemon> getAvailableBugemons()`
    - `changeOnFieldBugemon(Bugemon bug)`
    - ect

Models à ajouter
- `Fight` :
    - `TeamBugemon equipPlayer`
    - `TeamBugemon equipCPU` (hard coder pr l'instant)
    - `Bugemon playingBugemon`
    - `Bugemon waitingBugemon`
    - `playingAttackWaiting(Attack attack)`
    - `switchRole()`
    - `isFinished()`
    - `playerWon()`
    - ect

- **base du lien models — controller — vue :** Tache longue donc surement plusieurs duo
  La vue et le controller ont déjà était commencé plus tôt
    - Combat style Histoire 4, mais pas automatique :
      Quand la partie on le `FightController` crée un Fight avec l'équipe choisit/crée et initialise l'affichage, pour les `onFieldBugemon` on prend juste le 1er. Sur le bouton d'action, on met seulement le nom de l'attaque notre bugemon allié sur le terrain et on peut uniquement attaquer puis on se fait attaquer puis encore jusqu'un Bugemon meurt et se faire remplacer automatiquement et bref, on continue le combat jusqu'un gagnant avec un message de victoire/defense suivi d'un retour à l'écran titre. Sans oublier les barre de vie, les noms et les sprites qui s'actualisent et la boit de dialogue qui décrit l'action qui s'est déroulée comme les vrais jeux Pokémon.

- **Amelioration lien models — controller — vue :** Tache moins longue que l'autre
    - Combat style Histoire 5 : en fois la tâche précédente finie
      Mtn qd on lance le combat, on les options Attaquer, Changer, Objet et Abandonner comme sur la maquette, on n'a pas encore l'Histoire de l'Objet donc on affiche une liste hard coder pr l'instant, je pense. Mtn qd un bugemon meurt il y a un popup avec nos Bugemon encore en vie et on choisit le nouveau à envoyer sur le terrain. Et bref, on peut aussi abandonner ce qui affiche le message de défaite et retour à l'écran titre


---

## Séparation histoires en sous taches 2/3
Commencez d'abord par les lire les diagramme de classe, la Javadoc et comprendre le code des models et de `Fightview` et `FightController` avant de lire la suite

- windows `start javadoc/index.html`
- Mac `open javadoc/index.html`

---
Histoire 5

- Quand on gagne ou perd faut afficher un popup d'avec le bouton ok qui nous ramène à l'écran titre, un pop-up similaire à `SelectionView` qd on ne sélectionne aucune bugemon. Pareil si on abandonne
- Quand notre bugemon sur le terrain est KO, une fenêtre s'ouvre pour choisir quel bugemon envoyer. Utiliser un nouveau stage avec la même `teamGrid` que dans `FightView` en gardant les bugemon KO grisés. Un click sur un bugemon vivant le met sur le terrain et ferme la fenêtre, un click sur un bugemon KO affiche un message d'erreur "Ce bugemon est KO". Voir `switchPlayerBugemon()` dans `FightController` pour comprendre comment changer de bugemon côté model et vue. Même mécanique qd on change manuellement
- Qd on clique sur objet, une fenêtre similaire s'ouvre avec une `ListView` d'objets (hard codée pour l'instant vu qu'on n'en a pas) et un bouton pour fermer. Utiliser un objet fait perdre le tour et affiche comme message "" — voir `onSwitchDisplaeyPlayerClicked()` pour comprendre comment forcer l'ennemi à attaquer/passer son tour. Créez déjà une classe Objet avec l'attribut du JSON, mais hard codée l'objet affichés

---

Histoire 6
J'ai déjà implémenté la mécanique Initiative pour déterminer qui attaque en premier donc il ne faut pas la faire
Les différents services a codé et où les utiliser + Faire les Tests :

- `Service` : une classe différente par service, abstraite

    - `DamageCalculator`  dans `services/fight/`
        - avec la méthode : `public static int computeDamage(Bugemon attacker, Bugemon target, Attack attack)` qui prend en compte la formule de l'énoncé pour calculer les vrais dégâts d'un attack puis utiliser cette methode ds la methode `attack` de Bugemon

    - `TypeEffectivenessService` dans `services/fight/` :
        - `public static double getTypeMultiplier(String attackType, String targetType)`
        - `public static String getTypeColor(Bugemon bug)` qui retourne la couleur hex du type qui sera utilisé ds avec `SelectionView` pour colorier les case de chaque bugemon (un bugemon sélectionner de devoir plus apparait en rouge prc c'est pour type feu mtn)
        - `public static String getEffectivenessMessage(String attackType, String targetType)` → retourne `"Super efficace !"`, `"Peu efficace..."` ou `""` selon le multiplicateur. En suite l'utiliser ds la methode `attack` de Bugemon pour calculer le String renvoyer.

    - `AttackMessageService` dans `services/fight/` avec :
      Pendant le choix d'attaque en plein combat qd on passe la souris sur une attaque un pop up doit apparaitre pour nous prévenir si l'attaque est rentable ou pas donc il faut créer la méthode :
        - `public static String predictEfficiency( Bugemon target, Attack attack)`  qui renvoie un String prédisant l'efficacité de l'attaque sur l'attaque, on met souris contre le bugemon enemy sur le terrain en fct du multiplicateur (voir énoncé) :"Super efficace !", "Peu efficace", "Rien de spécial"

      Ensuite le Message renvoyé par la Methode `attack` de Bugemon doit être modifié en fonction des dégâts donc il faut aussi une methode `String tellEfficiency()`

---

Histoire 8 : À faire APRES l'histoire 6
- Commencer par Appliquer les effets d'altération de manière définitive
  Créer un Service ``EffectApplier`
`public static void applyEffects (Bugemon attacker, Bugemon target, Attack, attack)`
  qui gère la modification de stats en fct des effets (voir énoncé) à chaque attaque
  (les effets peuvent s'applique au lanceur aussi)

- Ensuite gérer les notions de durée :
  Pour les effets temporaires (`duree = "1_tour"`), une approche simple :

Créer une classe `ActiveEffect` avec deux attributs : `Effects effect` et `Integer turnsLeft`.

Dans `Bugemon`, ajouter `List<ActiveEffect> ongoingEffects`.

À chaque tour, appeler `tickEffects()` qui itère sur `ongoingEffects` : si `turnsLeft == null` on ignore (effet permanent), sinon on applique l'effet et on décrémente — si `turnsLeft` atteint `0` on annule l'effet puis on retire l'`ActiveEffect` de la liste.
Aprés ça on applique simplement l'effet

\+ implémenter un service qui remet tous les modificateurs de statistiques sont remis à zéro

---
\+ Refactor `FightView` en plusieurs fichiers comme `Selectionview` prc +400 ligne c trop

\+ Refactor l'ensemble des vues du code, car il y aura forcément de la duplication de code

## Séparation histoires en sous taches 3/3 (refactor histoire 5)

#### 1. Les sous-vues items/switch n'héritent pas de `View`

**Problème** `ItemsSubView` et `SwitchBugemonSubView` sont des classes standalone qui gèrent elles-mêmes leur `Stage`, leur FXML, leur `Scene`. Ce sont de vraies vues, elles devraient hériter de `View`.

**Fix**

- Faire hériter `ItemsSubView` et `SwitchBugemonSubView` de `View`.
- Leur associer un controller (`ItemChoiceController` / `SwitchBugemonController`).
- La gestion du stage passe dans le `MetaController` via `openSubScene()` ou similaire — **pas de `new Stage()` dans une vue**.

---
#### 2. `FightController` ouvre lui-même les sous-vues items/switch

**Problème** `FightController` instancie directement `ItemsSubView` et `SwitchBugemonSubView` dans `onItemClicked()` et `showSwitchWindow()`. Le controller ne doit pas s'occuper d'ouvrir des vues, encore moins des sous-vues.

**Fix**

- Créer `ItemChoiceController` et `SwitchBugemonController` .
- `FightController` demande au `MetaController` d'ouvrir ces sous-scènes via une méthode dédiée, ex : `metaController.openSwitchWindow(...)`.
- Le `MetaController` instancie le controller concerné, lui passe un callback de retour, et l'affiche.
- Pas de communication directe entre les 3 controllers !
- Toute communication entre controllers passe par le `MetaController`, il faut donc une methode que le `MetaController` va utiliser pour récupérer le bugemon choisi depuis `SwitchBugemonController` et l'envoyer à `FightController`

---

#### 3. `MetaController` utilise `openSubScene` plutôt que le `Stage` principal

**Problème** Les sous-vues items/switch doivent s'afficher en sous-fenêtre modale (pas en grand écran, FightView doit rester visible en arrière).

**Fix** Ajouter dans `MetaController` une méthode `openSubScene(Stage subStage)` ou similaire qui :

- Crée un nouveau `Stage` secondaire (pas le `stage` principal).
- Il affiche la vue demandée.
- Gère la modalité (`Modality.APPLICATION_MODAL`).

Les controllers ne créent jamais de `Stage` eux-mêmes, ni ouvrir des vues.

---

#### 4. `ItemChoiceView` ne dois pas importer la classe `Item` directement — créer `ItemDTO`

**Problème** `Item` est un objet modèle. La vue ne doit jamais importer ni manipuler un objet modèle directement — elle ne doit voir que des DTOs. Le controller, lui, a le droit de manipuler `Item`.

**Fix**

- Créer `ItemDTO` (interface ou record read-only) avec `getId()`, `getName()`, `getDescription()`.
- `Item` implémente `ItemDTO`.
- `ItemChoiceView` ne manipule que `ItemDTO` — `ItemChoiceController` garde ses références à `Item`.

---

#### 6. Créer l'interface `ListenerSelectBugemonFromTeam`

**Problème** `SwitchBugemonController` et `ChooseBugemonToApplyItemController` (futur) ont tous les deux besoin d'afficher une équipe et de retourner un bugémon sélectionné. Plutôt que de dupliquer la logique ou de recréer deux vues identiques, il faut une interface commune.

**Fix** Créer l'interface `ListenerSelectBugemonFromTeam` :

java

```java
public interface ListenerSelectBugemonFromTeam {
    void onBugemonSelected(Object bugemonId);
    void onCancelled(); // optionnel selon le contexte
}
```

`SwitchBugemonController` et `ChooseBugemonToApplyItemController` l'implémentent. La vue `SwitchBugemonView` (héritant de `View`) notifie via cette interface.

---

#### 7. Diviser FightView en plusieurs SubView

C'est le truc le moins urgent

---
**Tâches refactor liées à l'Histoire 5**

**1. Créer `SwitchBugemonController` et `ItemChoiceController`** `FightController` ne doit plus instancier ni ouvrir lui-même `SwitchBugemonSubView` / `ItemsSubView`. Il délègue au `MetaController`.

**2. `ItemsSubView` et `SwitchBugemonSubView` héritent de `View`** Elles gèrent actuellement leur propre `Stage`/`Scene`/FXML — c'est le rôle du `MetaController` via `openSubScene()`.

**3. Ajouter `openSubScene()` dans `MetaController`** Crée un `Stage` secondaire modal, y affiche la sous-vue demandée. Aucun controller ne crée jamais de `Stage` directement.

**4. Communication `FightController` ↔ sous controllers via `MetaController` uniquement** `FightController` → `metaController.openSwitchWindow(callback)` → `SwitchBugemonController` traite → `MetaController` renvoie le résultat à `FightController`. Jamais de référence directe entre les 3 controllers.

**5. Créer `ItemDTO`** `ItemChoiceView` ne manipule que `ItemDTO`. `ItemChoiceController` garde ses `Item` modèle.

**6. Créer `ListenerSelectBugemonFromTeam`** Interface commune implémentée par `SwitchBugemonController` et le futur `ChooseBugemonToApplyItemController` pour réutiliser la même `SwitchBugemonView` sans la dupliquer.

**7. Diviser `FightView`** en plusieurs `SubView`
# Sokoban - Projet L3 Informatique

Ce projet est une implémentation du jeu classique **Sokoban** développée en **Java** avec le framework **LibGDX**. L'application respecte une architecture logicielle rigoureuse basée sur le patron de conception **MVC** (Modèle-Vue-Contrôleur).

## Prérequis

* **Java JDK 17** (ou supérieur) installé.
* Une connexion internet (lors du premier lancement uniquement) pour le téléchargement automatique des dépendances via Gradle.

## Installation et Exécution

Le projet utilise le **Gradle Wrapper** fourni. Il n'est pas nécessaire d'installer Gradle sur votre machine.

1. **Clonage du projet (dans le répertoire courant) :**
    ```bash
   git clone https://github.com/Nurulome/MySokoban.git
   cd MySokoban
2. **Lancement du jeu pour Linux/MacOS :**
   ```bash
   ./gradlew lwjgl3:run
3. **Pour Window :**
    ```bash
   ./gradlew.bat lwjgl3:run

## Commandes du jeu :
* Déplacement du joueur : flèches directionnelles (↑, ↓, ←, →)
* Afficher l'aide : touche H
* Quitter le jeu : touche Échap


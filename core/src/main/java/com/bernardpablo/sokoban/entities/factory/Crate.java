package com.bernardpablo.sokoban.entities.factory;

import com.bernardpablo.sokoban.logic.ILevelState;

/**
 * Représente une caisse (objet déplaçable) dans le jeu Sokoban.
 * @author Bernard PABLO
 */
public class Crate extends Entity {
    private final ILevelState level;

    /**
     * Construit une caisse avec ses dépendances.
     * @param x      Position X initiale.
     * @param y      Position Y initiale.
     * @param tileId Identifiant de la tuile graphique.
     * @param level  Interface d'accès à l'état du niveau pour les collisions.
     */
    public Crate(float x, float y, int tileId, ILevelState level) {
        super(x, y, tileId);
        this.level = level;
    }

    /**
     * Tente de déplacer la caisse suite à une poussée.
     * Cette méthode utilise une logique récursive pour gérer les collisions (pas dans les règles du Sokoban "de base")
     * @param dx Déplacement horizontal souhaité.
     * @param dy Déplacement vertical souhaité.
     * @return {@code true} si la caisse a pu être déplacée, {@code false} sinon.
     */
    public boolean push(int dx, int dy) {
        float nextX = x + dx;
        float nextY = y + dy;

        // Collision avec les murs du niveau
        if (level.isWall(nextX, nextY)) {
            return false;
        }

        // Détection d'une autre caisse sur la trajectoire
        Crate nextCrate = level.getCrateAt(nextX, nextY);
        if (nextCrate != null) {
            // Tentative de pousser la caisse suivante (propagation du mouvement)
            if (!nextCrate.push(dx, dy)) {
                return false;
            }
        }

        // Finalisation du mouvement
        x = nextX;
        y = nextY;
        return true;
    }
}

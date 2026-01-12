package com.bernardpablo.sokoban.entities.factory;

import com.bernardpablo.sokoban.logic.ILevelState;

/**
 * Représente le personnage contrôlé par l'utilisateur.
 * Utilise l'interface {@link ILevelState} pour percevoir le monde sans
 * dépendre directement du modèle global.
 * @author Bernard PABLO
 */
public class Player extends Entity {
    private final ILevelState level;

    /**
     * Construit un nouveau joueur avec ses dépendances.
     * @param x       Position X initiale.
     * @param y       Position Y initiale.
     * @param tileId  Identifiant visuel du personnage.
     * @param level   Accès à l'état du niveau (murs, caisses).
     */
    public Player(float x, float y, int tileId, ILevelState level) {
        super(x, y, tileId);
        this.level = level;
    }

    /**
     * Tente de déplacer le joueur selon un vecteur de direction.
     * @param dx Déplacement sur l'axe X (pixels).
     * @param dy Déplacement sur l'axe Y (pixels).
     * @return true si le déplacement (et l'éventuelle poussée) a réussi.
     */
    public boolean tryMove(int dx, int dy) {
        float nextX = x + dx;
        float nextY = y + dy;

        // Collision avec les murs
        if (level.isWall(nextX, nextY)) {
            return false;
        }

        // Interaction avec les caisses
        Crate crate = level.getCrateAt(nextX, nextY);
        if (crate != null) {
            if (!crate.push(dx, dy)) {
                return false;
            }
        }

        // Mise à jour de la position
        this.x = nextX;
        this.y = nextY;
        return true;
    }
}

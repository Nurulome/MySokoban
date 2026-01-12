package com.bernardpablo.sokoban.entities.factory;

/**
 * Représente une cible (emplacement d'objectif) dans le jeu Sokoban.
 * @author Bernard PABLO
 */
public class Target extends Entity {

    /**
     * Construit une cible à une position spécifique.
     * @param x      Position X initiale
     * @param y      Position Y initiale
     * @param tileId Identifiant de la tuile graphique (Tile ID)
     */
    public Target(float x, float y, int tileId) {
        super(x, y, tileId);
    }
}

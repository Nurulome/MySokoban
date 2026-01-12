package com.bernardpablo.sokoban.entities.factory;

/**
 * Classe de base abstraite représentant une entité physique dans l'univers du jeu.
 * @author Bernard PABLO
 */
public abstract class Entity {
    protected float x;
    protected float y;
    protected float width = 64;
    protected float height = 64;
    protected int tileId;

    /**
     * Initialise une nouvelle entité avec ses coordonnées et son apparence.
     * @param x      Position X initiale.
     * @param y      Position Y initiale.
     * @param tileId Identifiant de la tuile graphique.
     */
    public Entity(float x, float y, int tileId) {
        this.x = x;
        this.y = y;
        this.tileId = tileId;
    }

    /**
     * @return La position actuelle sur l'axe X.
     */
    public float getX() {
        return x;
    }

    /**
     * @return La position actuelle sur l'axe Y.
     */
    public float getY() {
        return y;
    }

    /**
     * @return L'ID de la tuile associée à cette entité pour le rendu.
     */
    public int getTileId() {
        return tileId;
    }
}

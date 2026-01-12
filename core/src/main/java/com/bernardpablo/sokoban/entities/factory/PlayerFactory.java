package com.bernardpablo.sokoban.entities.factory;

/**
 * Fabrique concrète responsable de la création de l'entité {@link Player}.
 * @see Player
 * @author Bernard PABLO
 */
public class PlayerFactory extends EntityFactory<Player> {

    /**
     * Instancie le joueur et lui injecte ses dépendances logiques.
     * @param x       Coordonnée X initiale.
     * @param y       Coordonnée Y initiale.
     * @param tileId  Identifiant visuel du joueur dans le tileset.
     * @param context Contexte de création.
     * @return Une instance de {@link Player} prête à l'emploi.
     */
    @Override
    protected Player howToBuild(float x, float y, int tileId, EntityCreationContext context) {
        // Extraction et injection de la dépendance ILevelState
        return new Player(x, y, tileId, context.getLevelState());
    }
}

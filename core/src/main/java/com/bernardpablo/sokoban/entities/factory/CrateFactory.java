package com.bernardpablo.sokoban.entities.factory;

/**
 * Fabrique concrète responsable de la création des entités de type {@link Crate}.
 * @author Bernard PABLO
 */
public class CrateFactory extends EntityFactory<Crate> {

    /**
     * Instancie une nouvelle caisse en lui injectant l'accès à l'état du niveau.
     * @param x       Position X initiale
     * @param y       Position Y initiale
     * @param tileId  Identifiant de la tuile correspondant à la couleur/type de la caisse.
     * @param context Contexte de création
     * @return Une instance de {@link Crate} configurée.
     */
    @Override
    protected Crate howToBuild(float x, float y, int tileId, EntityCreationContext context) {
        return new Crate(x, y, tileId, context.getLevelState());
    }
}

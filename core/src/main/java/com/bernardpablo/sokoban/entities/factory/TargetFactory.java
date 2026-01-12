package com.bernardpablo.sokoban.entities.factory;

/**
 * Fabrique concrète responsable de la création des entités de type {@link Target}.
 * @see Target
 * @author Bernard PABLO
 */
public class TargetFactory extends EntityFactory<Target> {

    /**
     * Instancie une nouvelle cible aux coordonnées spécifiées.
     * @param x       Position X sur la carte
     * @param y       Position Y sur la carte
     * @param tileId  L'identifiant de la tuile graphique représentant la cible.
     * @param context Contexte de création
     * @return Une nouvelle instance de {@link Target}.
     */
    @Override
    protected Target howToBuild(float x, float y, int tileId, EntityCreationContext context) {
        return new Target(x, y, tileId);
    }
}

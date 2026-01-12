package com.bernardpablo.sokoban.entities.factory;

/**
 * Classe de base abstraite pour la fabrication d'entités de jeu.
 * @see Entity
 * @author Bernard PABLO
 */
public abstract class EntityFactory<T extends Entity> {

    /**
     * Point d'entrée public pour la création d'une entité.
     * @param x       Position initiale sur l'axe X
     * @param y       Position initiale sur l'axe Y
     * @param tileId  L'identifiant de la texture (Tile ID) défini dans le fichier TMX.
     * @param context Le contexte de création ({@link EntityCreationContext}).
     * @return Une nouvelle instance de type T.
     */
    public T build(float x, float y, int tileId, EntityCreationContext context) {
        return howToBuild(x, y, tileId, context);
    }

    /**
     * Méthode de fabrication spécifique ("Hook Method").
     * <p>
     * Doit être implémentée par chaque fabrique concrète pour instancier
     * l'objet spécifique (Player, Crate, etc.) avec ses paramètres propres.
     * </p>
     *
     * @param x       Position X.
     * @param y       Position Y.
     * @param tileId  Identifiant de la tuile graphique.
     * @param context Contexte de création (peut être null pour certaines entités).
     * @return L'instance concrète de l'entité.
     */
    protected abstract T howToBuild(float x, float y, int tileId, EntityCreationContext context);
}

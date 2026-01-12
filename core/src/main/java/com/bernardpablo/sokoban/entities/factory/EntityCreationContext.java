package com.bernardpablo.sokoban.entities.factory;

import com.bernardpablo.sokoban.logic.ILevelState;

/**
 * Contexte de création regroupant les dépendances nécessaires à l'instanciation des entités.
 * @author Bernard PABLO
 */
public final class EntityCreationContext {
    /** L'état du niveau permettant aux entités de gérer leurs interactions. */
    private final ILevelState levelState;

    /**
     * Constructeur privé pour forcer l'utilisation du Builder.
     * @param levelState L'état du niveau à injecter.
     */
    private EntityCreationContext(ILevelState levelState) {
        this.levelState = levelState;
    }

    /**
     * @return L'état du niveau ({@link ILevelState}) contenu dans ce contexte.
     */
    public ILevelState getLevelState() { return levelState; }

    /**
     * Initialise un nouveau constructeur de contexte.
     * @return Une nouvelle instance de {@link Builder}.
     */
    public static EntityCreationContext.Builder builder() {
        return new EntityCreationContext.Builder();
    }

    /**
     * Builder interne
     */
    public static final class Builder {
        private ILevelState levelState;

        /**
         * Définit l'état du niveau pour le contexte en cours de création.
         * @param levelState L'interface d'accès au niveau.
         * @return L'instance du builder pour chaînage.
         */
        public EntityCreationContext.Builder levelState(ILevelState levelState) {
            this.levelState = levelState;
            return this;
        }

        /**
         * Finalise la création de l'objet
         * @return Le contexte configuré.
         */
        public EntityCreationContext build() {
            return new EntityCreationContext(levelState);
        }
    }
}

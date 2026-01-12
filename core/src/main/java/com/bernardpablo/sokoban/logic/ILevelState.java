package com.bernardpablo.sokoban.logic;

import com.bernardpablo.sokoban.entities.factory.Crate;
import com.bernardpablo.sokoban.entities.factory.Target;

/**
 * Cette interface agit comme une "vue logique" ou un contrat d'accès aux données.
 * Elle est principalement utilisée par les entités mobiles (Joueur, Caisses) pour
 * percevoir leur environnement (murs, obstacles, cibles) sans dépendre directement
 * de l'implémentation complète du modèle.
 * @author Bernard PABLO
 */
public interface ILevelState {

    /**
     * Vérifie si une collision avec un mur existe aux coordonnées spécifiées.
     * @param x Coordonnée X
     * @param y Coordonnée Y
     * @return true si la position est occupée par un élément infranchissable (Mur ou Vide).
     */
    boolean isWall(float x, float y);

    /**
     * Récupère la caisse située à une position précise.
     * @param x Coordonnée X
     * @param y Coordonnée Y
     * @return L'instance de {@link Crate} présente à ces coordonnées, ou null si la case est vide.
     */
    Crate getCrateAt(float x, float y);

    /**
     * Identifie si une cible se trouve à une position donnée.
     * @param x Coordonnée X
     * @param y Coordonnée Y
     * @return L'instance de {@link Target} trouvée, ou null sinon.
     */
    Target getTargetAt(float x, float y);
}

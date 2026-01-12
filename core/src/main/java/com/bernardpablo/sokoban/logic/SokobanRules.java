package com.bernardpablo.sokoban.logic;

import com.bernardpablo.sokoban.entities.factory.Crate;
import com.bernardpablo.sokoban.entities.factory.Target;

/**
 * Classe utilitaire définissant les règles métier du Sokoban.
 * <p>
 * Cette classe centralise la logique de validation des objectifs. Elle permet de
 * déterminer si une caisse spécifique correspond à une cible donnée en se basant
 * sur leurs identifiants de tuiles (Tile IDs) définis dans les fichiers Tiled (.tmx).
 * </p>
 * @author Bernard PABLO
 */
public class SokobanRules {

    /**
     * Vérifie si une caisse donnée est placée sur une cible qui lui correspond.
     * <p>
     * La correspondance est établie en récupérant l'ID de cible attendu pour l'ID
     * de la caisse fournie.
     * </p>
     * @param crate  La caisse à vérifier (peut être nulle).
     * @param target La cible à vérifier (peut être nulle).
     * @return true si la caisse correspond à la cible selon le barème des IDs.
     */
    public static boolean crateMatchesTarget(Crate crate, Target target) {
        if (crate == null || target == null) return false;

        int expected = getExpectedTargetTileId(crate.getTileId());
        return expected != -1 && target.getTileId() == expected;
    }

    /**
     * Table de correspondance entre les identifiants de tuiles des caisses
     * et ceux des cibles associées.
     * @param crateTileId L'ID de la tuile de la caisse.
     * @return L'ID de la cible correspondante, ou -1 si aucune règle n'est définie.
     */
    private static int getExpectedTargetTileId(int crateTileId) {
        switch (crateTileId) {
            case 2: return 26;
            case 3: return 39;
            case 4: return 52;
            case 5: return 65;
            case 6: return 78;
            default: return -1;
        }
    }
}

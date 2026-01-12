package com.bernardpablo.sokoban.logic;

import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.bernardpablo.sokoban.entities.factory.*;

/**
 * Chargeur de niveaux pour le moteur Sokoban.
 * <p>
 * Cette classe utilitaire est responsable de la lecture des fichiers TMX et de l'instanciation
 * de l'univers de jeu. Elle analyse les différentes couches (layers) de la carte pour extraire
 * les murs, les cibles, les caisses et le joueur.
 * </p>
 * @author Bernard PABLO
 */
public final class LevelLoader {
    private LevelLoader() {}

    /**
     * Charge un niveau complet à partir d'un chemin de fichier.
     * @param path  Le nom du fichier de niveau (ex: "level1.tmx") situé dans le dossier assets/maps/.
     * @param model L'état actuel du niveau ({@link ILevelState}) injecté dans les entités pour la gestion des collisions.
     * @return Un objet {@link LevelContent} contenant toutes les données du niveau chargé, ou null en cas d'erreur critique.
     * @throws IllegalArgumentException Si la carte est mal formée ou si des couches essentielles sont manquantes.
     */
    public static LevelContent load(String path, ILevelState model) {
        LevelContent content = new LevelContent();

        EntityCreationContext ec = EntityCreationContext.builder().levelState(model).build();

        try {
            content.map = new TmxMapLoader().load("maps/" + path);

            if (content.map.getLayers().get("Walls") == null) {
                throw new IllegalArgumentException("Couche 'Walls' manquante dans le fichier TMX.");
            }
            if (content.map.getProperties().get("infinite", false, Boolean.class)) {
                throw new IllegalArgumentException("Les cartes de taille infinie ne sont pas supportées.");
            }
        } catch (IllegalArgumentException i) {
            System.err.println("Erreur de configuration de carte : " + i);
        } catch (Exception e) {
            System.err.println("Erreur fatale : Impossible de charger le fichier " + path);
            System.exit(-1); // Arrêt propre en cas de ressource manquante critique
            return null;
        }

        try {
            content.nextLevel = content.map.getProperties().get("nextLevel", String.class);
        } catch (Exception e) {
            System.out.println("Information : Aucun niveau suivant défini (Dernier niveau).");
        }

        // Chargement des Cibles
        MapObjects targetObjects = content.map.getLayers().get("targets").getObjects();
        if (targetObjects != null) {
            for (TiledMapTileMapObject obj : targetObjects.getByType(TiledMapTileMapObject.class)) {
                content.targets.add(new TargetFactory().build(obj.getX(), obj.getY(), obj.getTile().getId(), null));
            }
        } else {
            System.err.println("Attention : Aucune cible détectée. Le niveau sera impossible à terminer.");
        }

        // Chargement des Caisses
        MapObjects crateObjects = content.map.getLayers().get("crates").getObjects();
        if (crateObjects != null) {
            for (TiledMapTileMapObject obj : crateObjects.getByType(TiledMapTileMapObject.class)) {
                content.crates.add(new CrateFactory().build(obj.getX(), obj.getY(), obj.getTile().getId(), ec));
            }
        } else {
            System.err.println("Attention : Aucune caisse détectée.");
        }

        // Chargement du Joueur
        MapObjects playerObjects = content.map.getLayers().get("player").getObjects();
        if (playerObjects != null) {
            for (TiledMapTileMapObject obj : playerObjects.getByType(TiledMapTileMapObject.class)) {
                // On ne prend que le premier objet "Joueur" trouvé
                content.player = new PlayerFactory().build(obj.getX(), obj.getY(), obj.getTile().getId(), ec);
                break;
            }
        } else {
            System.err.println("Erreur : Aucun point de départ pour le joueur n'a été trouvé.");
        }

        return content;
    }
}

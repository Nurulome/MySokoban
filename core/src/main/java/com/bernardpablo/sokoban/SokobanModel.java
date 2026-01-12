package com.bernardpablo.sokoban;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bernardpablo.sokoban.entities.factory.Crate;
import com.bernardpablo.sokoban.logic.ILevelState;
import com.bernardpablo.sokoban.entities.factory.Player;
import com.bernardpablo.sokoban.entities.factory.Target;
import com.bernardpablo.sokoban.logic.LevelContent;
import com.bernardpablo.sokoban.logic.LevelLoader;
import com.bernardpablo.sokoban.logic.SokobanRules;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * Modèle principal du jeu Sokoban.
 * <p>
 * Cette classe gère l'état logique du jeu, les entités (joueur, caisses, cibles)
 * et les règles de collision. Elle implémente {@link ILevelState} pour permettre
 * aux entités de consulter l'état du monde sans couplage fort.
 * </p>
 * <p>
 * En tant qu'{@link Observable}, elle notifie les vues de chaque changement
 * significatif (mouvement, victoire, chargement).
 * </p>
 * * @author Bernard PABLO
 */
public class SokobanModel extends Observable implements ILevelState {
    private TiledMap map;
    private String currentLevel;
    private String nextLevel;
    private Player player;
    private Array<Crate> crates;
    private Array<Target> targets;

    /**
     * Initialise un nouveau modèle vide avec des listes d'entités prêtes à être chargées.
     */
    public SokobanModel() {
        this.crates = new Array<>();
        this.targets = new Array<>();
    }

    /**
     * Charge un niveau à partir d'un fichier TMX et initialise l'état du jeu.
     * Notifie les observateurs avec l'événement "LEVEL_LOADED".
     * @param levelPath Chemin interne vers le fichier .tmx du niveau.
     */
    public void loadLevel(String levelPath) {
        LevelContent content = LevelLoader.load(levelPath, this);
        if (content == null) return;

        this.map = content.map;
        this.player = content.player;
        this.crates = content.crates;
        this.targets = content.targets;
        this.nextLevel = content.nextLevel;
        this.currentLevel = levelPath;

        setChanged();
        notifyObservers("LEVEL_LOADED");
    }

    /**
     * Vérifie si toutes les cibles du niveau sont occupées par une caisse correspondante.
     * @return true si le niveau est terminé avec succès.
     */
    public boolean isLevelCompleted() {
        if (targets.isEmpty()) return false;
        for (Target target : targets) {
            Crate crate = getCrateAt(target.getX(), target.getY());
            if (!SokobanRules.crateMatchesTarget(crate, target)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Tente de déplacer le joueur selon un vecteur de direction.
     * <p>
     * Utilise un système de snapshot (capture d'état) pour détecter si une caisse
     * a été poussée sur une cible et déclencher l'événement sonore approprié.
     * </p>
     * @param dx Déplacement horizontal en pixels.
     * @param dy Déplacement vertical en pixels.
     */
    public void movePlayer(int dx, int dy) {
        Map<Crate, Vector2> positionsBefore = new HashMap<>();
        for (Crate crate : crates) {
            positionsBefore.put(crate, new Vector2(crate.getX(), crate.getY()));
        }

        if (player.tryMove(dx, dy)) {
            for (Crate crate : crates) {
                Vector2 oldPos = positionsBefore.get(crate);
                if (crate.getX() != oldPos.x || crate.getY() != oldPos.y) {
                    Target target = getTargetAt(crate.getX(), crate.getY());
                    if (target != null && SokobanRules.crateMatchesTarget(crate, target)) {
                        setChanged();
                        notifyObservers("BOX_ON_TARGET");
                    }
                }
            }

            if (isLevelCompleted()) {
                setChanged();
                notifyObservers("LEVEL_COMPLETED");
            }
        }
    }

    /**
     * Détermine si une position donnée est occupée par un mur dans la TiledMap.
     * @param x Coordonnée X en pixels.
     * @param y Coordonnée Y en pixels.
     * @return true si la case appartient à la couche "Walls" (ou si hors carte).
     */
    @Override
    public boolean isWall(float x, float y) {
        int tileX = (int) (x / 64);
        int tileY = (int) (y / 64);
        TiledMapTileLayer wallsLayer = (TiledMapTileLayer) map.getLayers().get("Walls");

        if (tileX < 0 || tileY < 0 || tileX >= wallsLayer.getWidth() || tileY >= wallsLayer.getHeight()) {
            return true;
        }

        TiledMapTileLayer.Cell cell = wallsLayer.getCell(tileX, tileY);
        return cell != null && cell.getTile() != null;
    }

    /**
     * Recherche une caisse à des coordonnées précises.
     * @param x Coordonnée X en pixels.
     * @param y Coordonnée Y en pixels.
     * @return La {@link Crate} trouvée, ou null sinon
     */
    @Override
    public Crate getCrateAt(float x, float y) {
        for (Crate crate : crates) {
            if (crate.getX() == x && crate.getY() == y) return crate;
        }
        return null;
    }

    /**
     * Recherche une cible à des coordonnées précises.
     * @param x Coordonnée X en pixels.
     * @param y Coordonnée Y en pixels.
     * @return La {@link Target} trouvée, ou null sinon.
     */
    @Override
    public Target getTargetAt(float x, float y) {
        for (Target target : targets) {
            if (target.getX() == x && target.getY() == y) return target;
        }
        return null;
    }

    // Getters
    public TiledMap getMap() { return map; }
    public String getNextLevel() { return nextLevel; }
    public Array<Crate> getCrates() { return crates; }
    public Array<Target> getTargets() { return targets; }
    public Player getPlayer() { return player; }
    public String getCurrentLevelPath() { return currentLevel; }
}

package com.bernardpablo.sokoban.logic;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import com.bernardpablo.sokoban.entities.factory.Crate;
import com.bernardpablo.sokoban.entities.factory.Player;
import com.bernardpablo.sokoban.entities.factory.Target;

/**
 * Conteneur de données regroupant l'intégralité des éléments d'un niveau chargé.
 * <p>
 * Cette classe sert de structure de transfert entre le {@link LevelLoader}
 * et le {@link com.bernardpablo.sokoban.SokobanModel}.
 * </p>
 * @author Bernard PABLO
 */
public class LevelContent {
    public TiledMap map;
    public String nextLevel;
    public Player player;
    public Array<Crate> crates = new Array<>();
    public Array<Target> targets = new Array<>();
}

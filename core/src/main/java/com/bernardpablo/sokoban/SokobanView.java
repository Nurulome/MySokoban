package com.bernardpablo.sokoban;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bernardpablo.sokoban.entities.factory.Crate;
import com.bernardpablo.sokoban.entities.factory.Entity;
import com.bernardpablo.sokoban.entities.factory.Target;

import java.util.Observable;
import java.util.Observer;

/**
 * Représente la Vue principale du jeu Sokoban.
 * <p>
 * Cette classe est responsable du rendu graphique en utilisant LibGDX. Elle implémente
 * l'interface {@link Observer} pour réagir aux changements d'état du {@link SokobanModel}.
 * </p>
 * <p>
 * Elle gère le rendu de la carte Tiled (.tmx), des entités dynamiques (joueur, caisses, cibles)
 * ainsi que de l'interface utilisateur ({@link TextOverlay}).
 * </p>
 * @author Bernard PABLO
 */
public class SokobanView implements Observer {
    private final SokobanModel model;
    private final OrthogonalTiledMapRenderer tiledMapRenderer;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final SpriteBatch batch;
    private final TextOverlay textOverlay;

    /**
     * Constructeur : Initialise le moteur de rendu, la caméra et le système de vue.
     * Enregistre également cette vue comme observateur du modèle.
     * @param model Le modèle logique auquel la vue doit s'abonner.
     */
    public SokobanView(SokobanModel model) {
        this.model = model;
        model.addObserver(this);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(null);
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera);
        batch = new SpriteBatch();
        textOverlay = new TextOverlay();
    }

    /**
     * Méthode de mise à jour appelée automatiquement par le modèle via le pattern Observer.
     * @param o L'objet observé (SokobanModel).
     * @param arg L'argument de notification, attendu ici comme une {@link String} identifiant l'événement.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (!(arg instanceof String)) return;
        String event = (String) arg;

        switch (event) {
            case("LEVEL_LOADED") :
                // Met à jour la carte et ajuste la caméra à la nouvelle taille du niveau
                tiledMapRenderer.setMap(model.getMap());
                updateViewport();
                break;
            default:
                break;
        }
    }

    /**
     * Ajuste la taille du monde virtuel et centre la caméra sur la carte.
     * Calcule les dimensions totales en pixels à partir des propriétés de la TiledMap.
     */
    private void updateViewport() {
        int mapWidth = model.getMap().getProperties().get("width", Integer.class);
        int tileWidth = model.getMap().getProperties().get("tilewidth", Integer.class);

        int mapHeight = model.getMap().getProperties().get("height", Integer.class);
        int tileHeight = model.getMap().getProperties().get("tileheight", Integer.class);

        float mapPixelWidth = mapWidth * tileWidth;
        float mapPixelHeight = mapHeight * tileHeight;

        viewport.setWorldSize(mapPixelWidth, mapPixelHeight);
        viewport.update(com.badlogic.gdx.Gdx.graphics.getWidth(), com.badlogic.gdx.Gdx.graphics.getHeight(), true);
        camera.position.set(mapPixelWidth / 2, mapPixelHeight / 2, 0);
        camera.update();
    }

    /**
     * Boucle de rendu principale.
     * <p>
     * Efface l'écran, dessine la carte statique, puis appelle le rendu des objets
     * dynamiques et de l'interface textuelle (Overlay).
     * </p>
     */
    public void render() {
        // Nettoyage de l'écran (fond noir)
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        renderDynamicObjects();
        batch.end();

        batch.begin();
        textOverlay.render(batch);
        batch.end();
    }

    /**
     * Parcourt et dessine les entités mobiles ou interactives du jeu.
     * L'ordre de rendu est : Cibles -> Caisses -> Joueur.
     */
    private void renderDynamicObjects() {
        TiledMap map = model.getMap();
        for (Target target : model.getTargets()) {
            drawEntity(target, map);
        }
        for (Crate crate : model.getCrates()) {
            drawEntity(crate, map);
        }
        drawEntity(model.getPlayer(), map);
    }

    /**
     * Dessine une entité spécifique en récupérant la texture correspondante
     * dans le tileset de la carte.
     * @param entity L'entité à dessiner (Player, Crate ou Target).
     * @param map    La carte contenant les définitions de textures (tilesets).
     */
    private void drawEntity(Entity entity, TiledMap map) {
        TiledMapTile tile = map.getTileSets().getTile(entity.getTileId());
        if (tile != null) {
            TextureRegion region = tile.getTextureRegion();
            batch.draw(region, entity.getX(), entity.getY());
        }
    }

    /**
     * Gère le redimensionnement de la fenêtre de jeu. (non autorisée)
     * @param width  Nouvelle largeur en pixels.
     * @param height Nouvelle hauteur en pixels.
     */
    public void resize(int width, int height) {
        if (viewport != null) {
            viewport.update(width, height, true);
        }
    }

    /**
     * Libère les ressources natives de LibGDX (Batch, Renderer et Overlay).
     */
    public void dispose() {
        batch.dispose();
        tiledMapRenderer.dispose();
        textOverlay.dispose();
    }

    /**
     * @return L'objet {@link TextOverlay} utilisé par cette vue.
     */
    public TextOverlay getTextOverlay() {
        return textOverlay;
    }
}

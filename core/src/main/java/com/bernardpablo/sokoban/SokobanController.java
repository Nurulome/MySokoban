package com.bernardpablo.sokoban;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

import java.util.Observable;
import java.util.Observer;

/**
 * Contrôleur principal du jeu Sokoban.
 * <p>
 * Cette classe fait le lien entre l'utilisateur et le système. Elle capture les entrées
 * clavier via {@link InputAdapter} et pilote le {@link SokobanModel}.
 * </p>
 * @author Bernard PABLO
 */
public class SokobanController extends InputAdapter implements Observer {
    /** Taille d'une tuile en pixels, utilisée pour définir l'amplitude des déplacements. */
    private static final int TILE_SIZE = 64;

    private final SokobanModel model;
    private final SokobanView view;

    private boolean gameCompleted;

    /**
     * Constructeur : Initialise le contrôleur, lie la vue et le modèle, et lance le premier niveau.
     * @param model Le modèle logique à piloter.
     * @param view  La vue permettant d'accéder aux overlays d'interface.
     */
    public SokobanController(SokobanModel model, SokobanView view) {
        this.model = model;
        this.view = view;
        this.gameCompleted = false;

        model.addObserver(this);

        model.loadLevel("sokoban_level1.tmx");
        view.getTextOverlay().showHelp();
    }

    /**
     * Gère la saisie de caractères
     * <p>
     * Cette méthode traite les commandes globales :
     * 'R' pour recommencer, 'H' pour l'aide et 'A' pour les informations.
     * Si un overlay est visible, n'importe quelle touche le fermera.
     * </p>
     * @param character Le caractère saisi.
     * @return true si l'entrée a été consommée.
     */
    @Override
    public boolean keyTyped(char character) {
        if (view.getTextOverlay().isVisible()) {
            view.getTextOverlay().hide();

            if (gameCompleted) {
                Gdx.app.exit();
            }
            return true;
        }

        switch (Character.toLowerCase(character)) {
            case 'r' :
                model.loadLevel(model.getCurrentLevelPath());
                return true;
            case 'h' :
                view.getTextOverlay().showHelp();
                return true;
            case 'a' :
                view.getTextOverlay().showAbout();
                return true;
        }
        return false;
    }

    /**
     * Gère l'appui sur les touches sans caractère.
     * <p>
     * Traduit les flèches directionnelles en ordres de mouvement pour le joueur
     * au sein du modèle.
     * </p>
     * @param keycode Le code de la touche appuyée (constantes Input.Keys).
     * @return true si l'entrée a été traitée.
     */
    @Override
    public boolean keyDown(int keycode) {
        if (view.getTextOverlay().isVisible()) {
            view.getTextOverlay().hide();
            if (gameCompleted) Gdx.app.exit();
            return true;
        }

        switch (keycode) {
            case Input.Keys.LEFT:
                model.movePlayer(-TILE_SIZE, 0);
                return true;
            case Input.Keys.RIGHT:
                model.movePlayer(TILE_SIZE, 0);
                return true;
            case Input.Keys.UP:
                model.movePlayer(0, TILE_SIZE);
                return true;
            case Input.Keys.DOWN:
                model.movePlayer(0, -TILE_SIZE);
                return true;
            case Input.Keys.ESCAPE:
                Gdx.app.exit();
                return true;
        }
        return false;
    }

    /**
     * Réagit aux notifications du modèle.
     * <p>
     * En cas de victoire sur un niveau ("LEVEL_COMPLETED"), le contrôleur vérifie
     * si un niveau suivant est disponible. Sinon, il déclenche l'écran de fin de jeu.
     * </p>
     * @param o   Le modèle observé.
     * @param arg L'événement envoyé par le modèle.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (!(arg instanceof String)) return;
        String event = (String) arg;

        switch (event) {
            case("LEVEL_COMPLETED"):
                String nextLevel = model.getNextLevel();
                if (nextLevel != null && !nextLevel.isEmpty()) {
                    System.out.println("Niveau completé! Chargement du prochain niveau : " + nextLevel);
                    model.loadLevel(nextLevel);
                } else {
                    System.out.println("Niveau completé! Plus du niveaux");
                    gameCompleted = true;
                    view.getTextOverlay().showMessage(
                        "BRAVO !\n\n" +
                            "Vous avez fini tous les niveaux \n\n" +
                            "Appuyer sur une touche pour quitter"
                    );
                }
                break;
            default:
                break;
        }
    }
}

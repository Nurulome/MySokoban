package com.bernardpablo.sokoban;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

/**
 * Classe principale et point d'entrée du jeu Sokoban.
 * Cette classe étend {@link ApplicationAdapter} pour s'intégrer au cycle de vie de LibGDX.
 * @author Bernard PABLO
 */
public class MySokoban extends ApplicationAdapter {
    private SokobanModel model;
    private SokobanView view;
    private SokobanController controller;
    private SokobanSound sound;

    /**
     * Initialise les composants du jeu au lancement de l'application.
     */
    @Override
    public void create() {
        model = new SokobanModel();
        view = new SokobanView(model);
        sound = new SokobanSound(model);
        controller = new SokobanController(model, view);

        // Définit le contrôleur comme processeur d'entrées par défaut
        Gdx.input.setInputProcessor(controller);
    }

    /**
     * Transmet les événements de redimensionnement de la fenêtre à la vue (non utilisée dans l'implémntation actuelle).
     * @param width  Nouvelle largeur de la fenêtre.
     * @param height Nouvelle hauteur de la fenêtre.
     */
    @Override
    public void resize(int width, int height) {
        view.resize(width, height);
    }

    /**
     * Boucle de rendu principale.
     * Délègue l'intégralité du dessin à l'objet {@link SokobanView}.
     */
    @Override
    public void render() {
        view.render();
    }

    /**
     * Nettoie les ressources lors de la fermeture de l'application.
     */
    @Override
    public void dispose() {
        view.dispose();
        sound.dispose();
        if (model.getMap() != null) {
            model.getMap().dispose();
        }
    }
}



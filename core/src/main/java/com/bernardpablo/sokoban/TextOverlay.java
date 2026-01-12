package com.bernardpablo.sokoban;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;

/**
 * Gestionnaire d'interfaces de superposition (overlays) pour le jeu.
 * <p>
 * Cette classe permet d'afficher des écrans d'information (Aide, À propos, Messages)
 * en semi-transparence par-dessus le rendu du jeu. Elle utilise une caméra dédiée
 * pour garantir que l'overlay s'adapte toujours à la taille de la fenêtre,
 * indépendamment du niveau chargé.
 * </p>
 * @author Bernard PABLO
 */
public class TextOverlay {

    /**
     * Définit les différents types d'écrans affichables.
     */
    private enum OverlayType {
        /** Aucun overlay affiché */
        NONE,
        /** Écran des contrôles et touches de raccourci */
        HELP,
        /** Informations sur le projet et la version */
        ABOUT,
        /** Message contextuel personnalisé */
        MESSAGE
    }

    private OverlayType currentType;
    private String message;
    private final BitmapFont font;
    private final GlyphLayout layout;
    private final ShapeRenderer shapeRenderer;
    private final OrthographicCamera camera;
    private boolean visible;

    /** Texte statique affiché dans le menu d'aide. */
    private static final String HELP_TEXT =
        "CONTROLES\n\n" +
            "Fleches directionnelles - Se deplacer\n" +
            "R - Restart le niveau\n" +
            "H - Help\n" +
            "A - About (Règles) \n" +
            "ESC - Exit\n\n" +
            "Appuyer sur une touche pour quitter";

    /** Texte statique affiché dans le menu 'À propos'. */
    private static final String ABOUT_TEXT =
        "SOKOBAN\n\n" +
            "Un jeu de puzzle classique\n" +
            "Pousser toutes les caisses sur les cibles\n\n" +
            "Version 1.0\n\n" +
            "Appuyer sur une touche pour quitter";

    /**
     * Constructeur : Initialise les ressources graphiques (Font, ShapeRenderer)
     * et configure la caméra d'interface.
     */
    public TextOverlay() {
        this.font = new BitmapFont();
        this.font.getData().setScale(5.f);
        this.font.setColor(Color.WHITE);
        this.layout = new GlyphLayout();
        this.shapeRenderer = new ShapeRenderer();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.currentType = OverlayType.NONE;
        this.visible = false;
        this.message = "";
    }

    /**
     * Affiche l'écran d'aide avec les contrôles pré-définis.
     */
    public void showHelp() {
        this.currentType = OverlayType.HELP;
        this.message = HELP_TEXT;
        this.visible = true;
    }

    /**
     * Affiche l'écran d'information sur le jeu.
     */
    public void showAbout() {
        this.currentType = OverlayType.ABOUT;
        this.message = ABOUT_TEXT;
        this.visible = true;
    }

    /**
     * Affiche un message personnalisé (ex: changement de niveau).
     * @param text Le message à afficher au centre de l'écran.
     */
    public void showMessage(String text) {
        this.currentType = OverlayType.MESSAGE;
        this.message = text;
        this.visible = true;
    }

    /**
     * Masque l'overlay et réinitialise le message courant.
     */
    public void hide() {
        this.visible = false;
        this.currentType = OverlayType.NONE;
        this.message = "";
    }

    /** @return true si l'overlay est actuellement visible à l'écran. */
    public boolean isVisible() {
        return visible;
    }

    /** @return Le type d'overlay actuellement actif. */
    public OverlayType getCurrentType() {
        return currentType;
    }

    /**
     * Effectue le rendu de l'overlay.
     * <p>
     * <strong>Note :</strong> Cette méthode interrompt temporairement le SpriteBatch passé
     * en paramètre pour dessiner le fond opaque avec un ShapeRenderer, puis relance le
     * batch pour le rendu du texte.
     * </p>
     * @param batch Le SpriteBatch principal du jeu.
     */
    public void render(SpriteBatch batch) {
        if (!visible || message.isEmpty()) {
            return;
        }

        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        camera.setToOrtho(false, screenWidth, screenHeight);
        camera.update();

        // On arrête le batch pour utiliser le ShapeRenderer (rendu de formes)
        batch.end();

        // Activation du mélange (blending) pour la semi-transparence du fond
        Gdx.gl.glEnable(com.badlogic.gdx.graphics.GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(
            com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA,
            com.badlogic.gdx.graphics.GL20.GL_ONE_MINUS_SRC_ALPHA
        );

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.8f); // Noir avec 80% d'opacité
        shapeRenderer.rect(0, 0, screenWidth, screenHeight);
        shapeRenderer.end();

        Gdx.gl.glDisable(com.badlogic.gdx.graphics.GL20.GL_BLEND);

        // On redémarre le batch pour le texte
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        float targetWidth = screenWidth * 0.8f;
        layout.setText(font, message, Color.WHITE, targetWidth, Align.center, true);

        // Calcul du centrage vertical et horizontal
        float x = (screenWidth - targetWidth) / 2;
        float y = (float) screenHeight / 2 + layout.height / 2;

        font.draw(batch, layout, x, y);
    }

    /**
     * Libère les ressources natives (Font et ShapeRenderer).
     */
    public void dispose() {
        font.dispose();
        shapeRenderer.dispose();
    }
}

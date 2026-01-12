package com.bernardpablo.sokoban;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.Observable;
import java.util.Observer;

/**
 * Gère les effets sonores du jeu Sokoban.
 * <p>
 * Cette classe agit en tant qu'observateur du {@link SokobanModel}. Elle réagit
 * aux événements métier spécifiques pour déclencher des retours auditifs (audio feedback)
 * à l'utilisateur, sans interférer avec la logique de calcul ou l'affichage.
 * </p>
 * @author Bernard PABLO
 */
public class SokobanSound implements Observer {
    private final Sound crateOnTargetSound;

    /**
     * Constructeur : Charge les ressources audio et enregistre cette instance
     * comme observateur du modèle.
     * @param model Le modèle logique à écouter pour déclencher les sons.
     */
    public SokobanSound(SokobanModel model) {
        model.addObserver(this);
        // Chargement du fichier audio depuis le dossier assets
        crateOnTargetSound = Gdx.audio.newSound(Gdx.files.internal("sounds/crate_on_target.wav"));
    }

    /**
     * Méthode de mise à jour appelée par le modèle lors d'un changement d'état.
     * <p>
     * Filtre les événements pour ne jouer le son que lors de l'action "BOX_ON_TARGET".
     * </p>
     * @param o   Le modèle observé.
     * @param arg L'identifiant de l'événement (attendu sous forme de {@link String}).
     */
    @Override
    public void update(Observable o, Object arg) {
        if (!(arg instanceof String)) return;

        String event = (String) arg;

        switch (event) {
            case "BOX_ON_TARGET":
                // Déclenche la lecture de l'effet sonore
                crateOnTargetSound.play();
                break;
            default :
                break;
        }
    }

    /**
     * Libère les ressources audio natives de LibGDX.
     * <p>
     * Cette méthode doit être appelée lors de la fermeture de l'application
     * ou du changement d'écran pour éviter les fuites de mémoire (memory leaks).
     * </p>
     */
    public void dispose() {
        crateOnTargetSound.dispose();
    }
}

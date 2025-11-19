package world.planet;

import javafx.scene.media.AudioClip;

/**
 * Gère la lecture de l’ambiance sonore associée à une planète.
 *
 * <p>
 * Cette classe encapsule le chargement et le contrôle d’un fichier audio
 * utilisé comme musique ou ambiance de fond. Elle utilise un {@link AudioClip},
 * ce qui permet une lecture légère, simple et en boucle continue.
 * </p>
 *
 * <p><b>Note :</b> Cette classe ne gère pas le volume, les transitions,
 * ni les effets audio. Elle se concentre uniquement sur le chargement et la lecture.</p>
 */
public class PlanetAudio {
    private AudioClip clip;

    /**
     * Charge un fichier audio à partir de son chemin dans les ressources.
     *
     * @param path Le chemin du fichier audio au sein du classpath.
     *             Si la valeur est {@code null}, aucun chargement n'est effectué.
     *
     * <p>
     * Le fichier est automatiquement configuré pour être joué en boucle
     * (cycle infini).
     * </p>
     *
     * @throws RuntimeException si la ressource audio n'est pas trouvée
     *                          ou si son chargement échoue.
     */
    public void load(String path) {
        if (path == null) return;
        clip = new AudioClip(getClass().getResource(path).toExternalForm());
        clip.setCycleCount(AudioClip.INDEFINITE);
    }

    /**
     * Lance la lecture du clip audio chargé.
     * Si aucun clip n’est chargé, l’appel est ignoré.
     */
    public void play() {
        if (clip != null) clip.play();
    }

    /**
     * Arrête la lecture du clip audio.
     * Si aucun clip n’est chargé, l’appel est ignoré.
     */
    public void stop() {
        if (clip != null) clip.stop();
    }
}

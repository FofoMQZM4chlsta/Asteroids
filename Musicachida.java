package logica;

import javax.sound.sampled.*;
import java.io.File;

public class Musicachida {

    public static Clip musica;

    public static void Musiquita() {

        try {

            AudioInputStream audio = AudioSystem.getAudioInputStream(
            Musicachida.class.getResource("KCC.wav"));

            musica = AudioSystem.getClip();
            musica.open(audio);
            musica.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public static void MusiquitaDedede() {

        try {

            AudioInputStream audio = AudioSystem.getAudioInputStream(
            Musicachida.class.getResource("KDDD.wav"));

            musica = AudioSystem.getClip();
            musica.open(audio);
            musica.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void nomusiquita() {

        if (musica != null) {
            musica.stop();
            musica.close();
        }

    }

}
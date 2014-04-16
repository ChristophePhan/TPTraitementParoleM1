package TP2;

import java.io.IOException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Christophe & Tristan
 */
public class TP2Main {

    public static void main(String[] args) throws IOException, UnsupportedAudioFileException {
        AnalyseOLA aola = new AnalyseOLA("test_seg.wav");
        // 8 ms -> 8 * (22050/1000) = 176 echantillons
        // 32 ms -> 32 * (22050/1000) = 705 echantillons
        int start = 176;
        int size = 705;

        aola.reconstructionHamming(start, size);
        aola.reconstructionFourier(start, size, 1024);
    }
}

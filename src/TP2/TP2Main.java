package TP2;

import java.io.IOException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Christophe & Tristan
 */
public class TP2Main {

    public static void main(String[] args) throws IOException, UnsupportedAudioFileException {
        int fftOrder = 1024;
        int start = 176;
        int size = 705;
        AnalyseOLA aola = new AnalyseOLA("test_seg_bruit_0dB.wav", "test_seg_bruit_0dB_debruite.wav", fftOrder, start, size, 2,30,0);
        // 8 ms -> 8 * (22050/1000) = 176 echantillons
        // 32 ms -> 32 * (22050/1000) = 705 echantillons

        //aola.reconstructionHamming();
    }
}

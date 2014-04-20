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
        // 8 ms -> 8 * (22050/1000) = 176 echantillons
        // 32 ms -> 32 * (22050/1000) = 705 echantillons
        int start = 176;
        int size = 705;
        AnalyseOLA aola = new AnalyseOLA("test_seg_bruit_0dB.wav", "test_seg_bruit_0dB_debruite2-1-0.wav", fftOrder, start, size, 2, 1, 0);
        aola = new AnalyseOLA("test_seg_bruit_0dB.wav", "test_seg_bruit_0dB_debruite2-10-0.wav", fftOrder, start, size, 2, 10, 0);
        aola = new AnalyseOLA("test_seg_bruit_0dB.wav", "test_seg_bruit_0dB_debruite2-30-0.wav", fftOrder, start, size, 2, 30, 0);
        aola = new AnalyseOLA("test_seg_bruit_0dB.wav", "test_seg_bruit_0dB_debruite2-10-1.wav", fftOrder, start, size, 2, 10, 1);
        aola = new AnalyseOLA("test_seg_bruit_0dB.wav", "test_seg_bruit_0dB_debruite2-30-1.wav", fftOrder, start, size, 2, 30, 1);
        aola = new AnalyseOLA("test_seg_bruit_0dB.wav", "test_seg_bruit_0dB_debruite2-10-3.wav", fftOrder, start, size, 2, 10, 3);
        aola = new AnalyseOLA("test_seg_bruit_0dB.wav", "test_seg_bruit_0dB_debruite2-30-3.wav", fftOrder, start, size, 2, 30, 3);
        aola = new AnalyseOLA("test_seg_bruit_0dB.wav", "test_seg_bruit_0dB_debruite4-10-0.wav", fftOrder, start, size, 4, 10, 0);
        aola = new AnalyseOLA("test_seg_bruit_0dB.wav", "test_seg_bruit_0dB_debruite4-30-0.wav", fftOrder, start, size, 4, 30, 0);
        aola = new AnalyseOLA("test_seg_bruit_0dB.wav", "test_seg_bruit_0dB_debruite4-10-1.wav", fftOrder, start, size, 4, 10, 1);
        aola = new AnalyseOLA("test_seg_bruit_0dB.wav", "test_seg_bruit_0dB_debruite4-30-1.wav", fftOrder, start, size, 4, 30, 1);
        aola = new AnalyseOLA("test_seg_bruit_0dB.wav", "test_seg_bruit_0dB_debruite4-10-3.wav", fftOrder, start, size, 4, 10, 3);
        aola = new AnalyseOLA("test_seg_bruit_0dB.wav", "test_seg_bruit_0dB_debruite4-30-3.wav", fftOrder, start, size, 4, 30, 3);
        

        //aola.reconstructionHamming();
    }
}

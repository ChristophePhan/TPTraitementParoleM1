package TP2;

import TP1.Analyse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.UnsupportedAudioFileException;
import traitementparole.SoundSignal;
import fft.FFT; //si .jar

/**
 *
 * @author Christophe & Tristan
 */
public class AnalyseOLA {

    private SoundSignal ss;
    private double[] moyenne_bruit;
    private int fftOrder, start, size, alpha, beta, gamma;
    private String out;

    public AnalyseOLA(String path, String out, int fftOrder, int start, int size, int alpha, int beta, int gamma) throws IOException {
        ss = new SoundSignal();
        try {
            ss.setSignal(path);
        } catch (UnsupportedAudioFileException | IOException ex) {
            Logger.getLogger(Analyse.class.getName()).log(Level.SEVERE, null, ex);
        }
        moyenne_bruit = new double[fftOrder];
        this.fftOrder = fftOrder;
        this.out = out;
        this.start = start;
        this.size = size;
        this.alpha = alpha;
        this.beta = beta;
        this.gamma = gamma; // l'indice gamme est ce qui permet d'annuler le bruit ou d'amplifier le bruit donc pour annuler on le met a 0
        this.debruitage();
    }

    public double[] fenetrageHamming(int start, int size) {
        double[] signal_fen = new double[size];
        double[] hamming = this.hamming(size);
        for (int i = 0; i < size; i++) {
            if (start + i < ss.getSignalLength()) {
                signal_fen[i] = ss.getSignalSample(start + i) * hamming[i];
            }
        }
        return signal_fen;
    }

    public double[] hamming(int size) {
        double c1 = (double) 0.54;
        double c2 = (double) 0.46;
        double[] hamming = new double[size];
        for (int i = 0; i < size; i++) {
            hamming[i] = c1 - (double) (c2 * Math.cos((double) 2 * Math.PI * i / (size - 1)));
        }
        return hamming;
    }

    public double[] fft(double[] sign, int fftOrder, int indiceFenetre) {
        //int fftOrder = 1024;
        FFT fft = new FFT(fftOrder);//fftOrder : ordre de fft
        // une puissance de 2
        double[] x = new double[fftOrder * 2];
        //Initialisation du tableau x
        for (int i = 0; i < fftOrder; i++) {
            if (i < sign.length) {
                x[i * 2] = sign[i]; //signal fenétré
            }
            //(attention à la taille de signal_fen)
            x[i * 2 + 1] = 0;
        }
        fft.transform(x, false);
        //la partie réelle : x[i*2]
        //la partie imaginaire : x[i*2+1]
        //calculs sur le spectre

        //question 8 estimation du spectre d’amplitude du bruit
        int nb_echantillonsBruit = 5;
        double[] spectre_amplitude = this.spectreamplitude(x, fftOrder);
        if (indiceFenetre < nb_echantillonsBruit) {
            for (int i = 0; i < moyenne_bruit.length; i++) {
                moyenne_bruit[i] = moyenne_bruit[i] + spectre_amplitude[i];
            }
        } else if (indiceFenetre == nb_echantillonsBruit) {
            for (int i = 0; i < moyenne_bruit.length; i++) {
                moyenne_bruit[i] = moyenne_bruit[i] / nb_echantillonsBruit;
            }
        } // fin question 8
        
        // question 9
        spectre_amplitude = this.soustractionspetrale(spectre_amplitude, moyenne_bruit, alpha, beta, gamma);
        double[] spectre_phase = this.spectrephase(x, fftOrder);
        
        x = this.spectrereconstruction(spectre_amplitude, spectre_phase, fftOrder);

        fft.transform(x, true);
        //le signal fenêtré final se trouve en x[i*2]
        return x;
    }

    public double[] spectreamplitude(double[] x_fourier, int fftorder) {
        double[] spectre_amplitude = new double[fftorder];
        for (int i = 0; i < spectre_amplitude.length; i++) {
            //if (i * 2 + 1 <= spectre_amplitude.length && i * 2 <= spectre_amplitude.length) {
            double re = Math.pow(x_fourier[i * 2], 2);
            double im = Math.pow(x_fourier[i * 2 + 1], 2);
            spectre_amplitude[i] = Math.sqrt(re + im);
            //}
        }

        return spectre_amplitude;
    }

    public double[] spectrephase(double[] x_fourier, int fftorder) {
        double[] spectre_phase = new double[fftorder];
        for (int i = 0; i < spectre_phase.length; i++) {
            double re = x_fourier[i * 2];
            double im = x_fourier[i * 2 + 1];
            spectre_phase[i] = Math.atan2(im, re);
        }
        return spectre_phase;
    }

    public double[] spectrereconstruction(double[] spectre_amplitude, double[] spectre_phase, int fftorder) {
        // x[2*i] pour la partie réelle 
        // x[2*i+1] pour la partie imaginaire
        double[] res = new double[2 * fftorder];
        for (int i = 0; i < fftorder; i++) {
            res[i * 2] = spectre_amplitude[i] * Math.cos(spectre_phase[i]);
            res[i * 2 + 1] = spectre_amplitude[i] * Math.sin(spectre_phase[i]);
        }
        return res;
    }

    public double[] soustractionspetrale(double[] spectre_amplitude, double[] spectre_bruit, double alpha, double beta, double gamma) {
        double[] res = new double[spectre_amplitude.length];
        for (int i = 0; i < res.length; i++) {
            double soustraction = Math.pow(Math.pow(spectre_amplitude[i], alpha) - beta * Math.pow(spectre_bruit[i], alpha), 1 / alpha);
            if (soustraction > 0) {
                res[i] = soustraction;
            } else {
                res[i] = gamma * spectre_bruit[i];
            }
        }
        return res;
    }

    public void reconstructionHamming() throws IOException {
        short[] signal_modif = new short[ss.getSignalLength()];
        for (int i = 0; i < ss.getSignalLength(); i = i + start) {
            double[] f = this.fenetrageHamming(i, size);
            for (int k = 0; k < f.length; k++) {
                if (i + k < ss.getSignalLength()) {
                    signal_modif[k + i] = (short) ((f[k] + signal_modif[k + i]) / 2.16);
                }
            }
        }
        ss.setSignal(signal_modif, ss.getSamplingFrequency());
        ss.exportSignal("test_seg_hamming.wav", true);
    }

    private void debruitage() throws IOException {
        short[] signal_modif = new short[ss.getSignalLength()];
        int indiceFenetre = 0;
        for (int i = 0; i < ss.getSignalLength(); i = i + start) {
            double[] f = fft(this.fenetrageHamming(i, size), fftOrder, indiceFenetre);
            for (int k = 0; k < f.length; k++) {
                if (i + k < ss.getSignalLength() && 2 * k < 2 * fftOrder) {
                    signal_modif[k + i] = (short) ((f[k * 2] + signal_modif[k + i]));
                }
            }
            indiceFenetre++;
        }
        ss.setSignal(signal_modif, ss.getSamplingFrequency());
        ss.exportSignal(out, true);
    }
}

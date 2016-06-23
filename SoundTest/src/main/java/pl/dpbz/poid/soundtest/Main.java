/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dpbz.poid.soundtest;

import com.sun.media.sound.WaveFileReader;
import com.sun.media.sound.WaveFileWriter;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.spi.AudioFileWriter;

/**
 *
 * @author Daniel
 */
public class Main {

    public static void main(String args[]) throws UnsupportedAudioFileException, IOException {
        WaveFileReader wfr = new WaveFileReader();
        AudioInputStream ais = wfr.getAudioInputStream(new File("ExampleSounds/natural/flute/443Hz.wav"));
        AudioFormat af = ais.getFormat();
        System.out.println(af.getSampleRate());
        System.out.println(af.getSampleSizeInBits());
        System.out.println(af.getEncoding().toString());
        System.out.println(af.getChannels());
        System.out.println(ais.available());
        byte[] b = new byte[ais.available()];
        System.out.println("Przeczytano" + ais.read(b));
        
        for(int i = 0; i<b.length; i++){
            b[i]= (byte) (b[i]);
        }

        InputStream b_in = new ByteArrayInputStream(b);
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(
                "kutacz.wav"));
        dos.write(b);
        File file = new File("kutacz.wav");
        AudioFormat format = new AudioFormat(af.getSampleRate(), 16, 1, true, false);
        AudioInputStream stream = new AudioInputStream(b_in, format,
                b.length/2);
        AudioSystem.write(stream, AudioFileFormat.Type.WAVE, file);
    }

}

package Utils;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.ArrayList;

public class AudioPlayer implements Runnable{
    private Clip musicLoop;
    private Clip healSound;
    private Clip buttonSound;
    private Clip gameOverSound;
    private ArrayList<Clip> sfxClips;
    public boolean muteStatus = false;
    public AudioPlayer(){
        /*
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource("/Audio/Music.wav"));
            musicLoop = AudioSystem.getClip();
            musicLoop.open(audioInputStream);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        sfxClips = new ArrayList<>();
        insertClip("/Audio/laserfire01.wav");
        insertClip("/Audio/healSound.wav");
        insertClip("/Audio/button1.wav");
        */
    }
    public void insertClip(String s){
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource(s));
            Clip c = AudioSystem.getClip();
            c.open(audioInputStream);
            sfxClips.add(c);

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {

        musicLoop.start();
        musicLoop.loop(Clip.LOOP_CONTINUOUSLY);

    }
    public void pauseMusic(){
        if(!muteStatus){
            musicLoop.stop();
        }
    }
    public void resumeMusic(){
        if(!muteStatus){
            musicLoop.start();
            musicLoop.loop(Clip.LOOP_CONTINUOUSLY);
        }

    }
    public void healSound(){
        if(!muteStatus){
            sfxClips.get(1).setFramePosition(0);
            sfxClips.get(1).start();
        }

    }
    public void buttonSound(){
        if(!muteStatus){
            //sfxClips.get(2).setFramePosition(0);
            //sfxClips.get(2).start();
        }
    }
    public void muteAll(){
        //musicLoop.stop();
        //muteStatus = true;
    }
    public void unMuteAll(){
        muteStatus = false;
        //musicLoop.start();
        //musicLoop.loop(Clip.LOOP_CONTINUOUSLY);
    }
}

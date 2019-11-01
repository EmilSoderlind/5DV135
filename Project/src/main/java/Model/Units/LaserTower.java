package Model.Units;


import Utils.AudioPlayer;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class LaserTower extends Tower {
    private int ATTACK_RANGE = 10;
    private int damage;
    private Clip laserSound;
    private AudioPlayer audioPlayer;

    /**
     * Constructor for a laser tower. uses the superclass constructor when called.
     * @param tile tile that the tower will stand on
     * @param maxImageSize max image size
     * @param audioPlayer an audioplayer for the towers audio responses.
     */
    public LaserTower(Tile tile, Integer maxImageSize, AudioPlayer audioPlayer){
        super(tile,maxImageSize);
        attackRate = 1f;
        damage = 10;
        rechargeTime = attackRate;
        range = tile.getWidth() * ATTACK_RANGE;
        /*
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource("/Audio/laserfire01.wav"));
            laserSound = AudioSystem.getClip();
            laserSound.open(audioInputStream);
            laserSound.setFramePosition(0);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        this.audioPlayer = audioPlayer;
        */
    }

    /**
     * fire method for the laserTower
     * @param troops used for registering damage on troop or said troops
     */
    @Override
    public void fire(CopyOnWriteArrayList<Troop> troops) {
       /*
       if(!audioPlayer.muteStatus){
           laserSound();
       }
       */
        target.affectHP(-damage);
        hasTarget = false;
    }

    /**
     * laser towers draw function.
     * Will also draw the laser when firing at a target.
     * @param g graphics object
     */
    @Override
    public void draw(Graphics g) {
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D) g).setRenderingHints(renderingHints);
        g.setColor(Color.GREEN);
        if(rechargeTime < attackRate / 10){
            for(int i = 0; i< 10; i++){
                g.drawLine((int)rect.getCenterX()+i, (int)rect.getCenterY()+i,(int) target.getXPos(), (int) target.getYPos());
                g.drawLine((int)rect.getCenterX()-i, (int)rect.getCenterY()+i,(int) target.getXPos(), (int) target.getYPos());
            }
        }
        g.setColor(Color.GRAY);
        super.draw(g);


    }

    /**
     * triggers the laser sound effect
     */
    private void laserSound(){
        laserSound.setFramePosition(0);
        laserSound.start();
    }
}

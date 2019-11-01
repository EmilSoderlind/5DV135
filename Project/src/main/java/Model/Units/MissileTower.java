package Model.Units;


import Utils.AudioPlayer;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Missile tower that extends the Tower super class.
 */
public class MissileTower extends Tower {
    private final int ATTACK_RANGE = 10 ;
    private final float SPEED = 10;
    private int missileWidth = 10;

    private ArrayList<Projectile> pList;
    private Clip missileSound;
    private AudioPlayer audioPlayer;

    /**
     * Constructor for a missle tower
     * @param tile  Tile that the tower will stand on
     * @param pList reference to the sessions list of projectiles
     * @param maxImageSize  max image size of tower
     * @param audioPlayer an audioplayer for the tower.
     */
    public MissileTower(Tile tile, ArrayList<Projectile> pList, Integer maxImageSize, AudioPlayer audioPlayer){
        super(tile,maxImageSize);
        this.pList = pList;
        attackRate = 5f;
        rechargeTime = attackRate;
        range = tile.getWidth() * ATTACK_RANGE;
        /*
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource("/Audio/Missile.wav"));
            missileSound = AudioSystem.getClip();
            missileSound.open(audioInputStream);
            missileSound.setFramePosition(0);
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
     * Fire method for the missile tower
     * @param troops used for registering damage on troop or said troops
     */
    @Override
    public void fire(CopyOnWriteArrayList<Troop> troops) {
        /*
        if(!audioPlayer.muteStatus){
            missileSound();
        }
        */
        pList.add(new Projectile((int)rect.getCenterX() - missileWidth/2,(int)rect.getCenterY() - missileWidth/2 ,missileWidth,SPEED, target,maxImageSize));
    }

    /**
     * draw method for the missle tower
     * @param g graphics object
     */
    @Override
    public void draw(Graphics g) {
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D) g).setRenderingHints(renderingHints);
        g.setColor(Color.YELLOW);
        super.draw(g);
    }

    /**
     * trigger for missile sound
     */
    private void missileSound(){
        missileSound.setFramePosition(0);
        missileSound.start();
    }
}
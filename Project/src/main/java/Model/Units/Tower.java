package Model.Units;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Abstract superclass for all the towers in the anti-TD.
 * Implements drawable meaning that all towers that can
 * be instantiated are drawable.
 */
public abstract class Tower implements Drawable, TowerInterface {

    public Rectangle rect;
    protected boolean hasTarget = false;
    protected float attackRate;
    protected float rechargeTime = 0;
    protected int range;
    private Point shootOrigin;
    protected Integer maxImageSize;

    protected Troop target;

    /**
     * Constructor for a tower object
     * @param tile tile that the tower will rest upon
     * @param maxImageSize Maximum size of drawing
     */
    public Tower(Tile tile, Integer maxImageSize) {
        this.rect = tile.getFrame();
        attackRate = 2;
        shootOrigin = new Point((int)rect.getCenterX(),(int) rect.getCenterY());
        this.maxImageSize = maxImageSize;
    }

    /**
     * fires on the target
     * @param troops used for registering damage on troop or said troops
     */
    public abstract void fire(CopyOnWriteArrayList<Troop> troops);

    /**
     * draws a tower with the given graphics object
     * @param g graphics object
     */
    public void draw(Graphics g){

        g.fillOval(rect.x,rect.y,rect.width,rect.height);

    }

    /**
     * targets a troop
     * @param troop troop object to be targeted.
     */
    private void targetTroop(Troop troop){
        target = troop;
        hasTarget = true;
    }

    /**
     *
     * @param troops troop list to scan
     * @param timeStep timestep Used for determining rechargerate
     */
    public void scan(CopyOnWriteArrayList<Troop> troops, double timeStep){
        double targetDistance;
        double bestDistance = 100000000;

            for (Troop troop: troops) {
                targetDistance =  shootOrigin.distance(troop.getXPos(), troop.getYPos());
                if (range >= targetDistance) {
                    if(targetDistance < bestDistance){
                        bestDistance = targetDistance;
                        this.targetTroop(troop);
                        hasTarget = true;
                    }
                }
            }

        if(hasTarget && target.stillTargetable()){
            if(attackRate <= rechargeTime){

                fire(troops);
                rechargeTime = 0;
            }
        }
        rechargeTime += timeStep;
    }
}

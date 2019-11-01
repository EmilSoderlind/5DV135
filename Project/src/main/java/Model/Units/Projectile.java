package Model.Units;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Class for projectiles fired by MissileTower
 * Implements Drawable interface and can therefore be displayed.
 */
public class Projectile implements Drawable{
    private final int SPLITMAX = 3;
    private Rectangle rect;
    private double x;
    private double y;
    private float speed;
    private Troop target;
    private float SPLIT_DIA = 300;
    private float HEAVY_DMG = 20;
    private Point pos;
    private int splits = 0;

    private double dx, dy;

    /**
     * projectile Constructor
     * @param x x cordinate
     * @param y y cordinate
     * @param width projectile width
     * @param speed projectile speed
     * @param target    current target
     * @param maxImageSize  max size of image
     */
    public Projectile(int x, int y, int width, float speed, Troop target, Integer maxImageSize){
        rect = new Rectangle(x - width / 2, y - width/2, width, width);
        this.x = x;
        this.y = y;

        pos = new Point(x,y);
        this.speed = speed;
        this.target = target;
    }

    /**
     * Moves the projectile towards the target
     */
    public void moveToTarget(){
        //setX(getX() + (speed * Math.cos(direction)));
        //setY(getY() + (speed * Math.sin(direction)));
        double l;
        dx = target.getXPos() - rect.getCenterX();
        dy = target.getYPos() - rect.getCenterY();

        l = Math.sqrt(dx * dx + dy * dy);

        x += (speed * (dx / l));
        y += (speed * (dy / l));

        rect.x = (int)x - rect.width / 2;
        rect.y = (int)y - rect.width / 2;
    }

    /**
     * draws the projectile
     * @param g graphics object
     */
    @Override
    public void draw(Graphics g) {

            //g.setColor(Color.white);
            //g.drawOval((int)(x - (SPLIT_DIA / 2)),(int)(y - (SPLIT_DIA / 2)), (int) SPLIT_DIA,(int) SPLIT_DIA);
            g.setColor(Color.ORANGE);
            g.fillRect(rect.x, rect.y, rect.width, rect.width);

    }

    /**
     * splits the projectile if it can
     * @param troops list of troops in the session.
     * @return true if it can split false else
     */
    public boolean split(CopyOnWriteArrayList<Troop> troops){
        double targetDistance;
        target.affectHP(-HEAVY_DMG);
        pos.x = (int)x;
        pos.y = (int)y;

        for(Troop troop: troops){
            targetDistance = pos.distance(troop.getXPos(), troop.getYPos());

            if(targetDistance <= SPLIT_DIA / 2 && !troop.equals(target)){
                if(splits < SPLITMAX){
                    target = troop;
                    splits++;
                    return true;
                }
            }
        }
        return  false;
    }

    /**
     * checks if the projectile reached the target
     * @return true if projectile reached the target.
     */
    public boolean reachedTarget(){
        return rect.contains(target.getXPos(),target.getYPos());
    }
}

package Model.Effects;

import Model.Effect;
import Model.Units.Affectable;
import Model.Units.Drawable;
import Utils.EFFECTSTATUS;

import java.awt.*;

/**
 * TeleportEntrance is an effect that changes the position of
 * affectable objects.
 * @see TeleportExit
 */
public class TeleportEntrance extends Effect implements Drawable {

    private TeleportExit teleportExit;

    public TeleportEntrance(int dormantDuration, double xPosition, double yPosition, Integer maxImageSize) {
        super(dormantDuration, xPosition, yPosition, maxImageSize);
    }

    /**
    * If effect is active, change the position of Affectable object
    * to the position of created TeleportExit
    * @param unitToBeAffected Affectable unit.
    */
    @Override
    public void affect(Affectable unitToBeAffected) {
        if (effectStatus == EFFECTSTATUS.ACTIVE) {
            unitToBeAffected.affectPosition((int) teleportExit.getX(),(int) teleportExit.getY());
            duration--;
            teleportExit.reduceDuration();
        }
    }

    /**
     * Draws TeleportEntrance and TeleportExit onto given graphics object.
     * @param g graphics object to be drawn on.
     */
    @Override
    public void draw(Graphics g) {
        if (effectStatus == EFFECTSTATUS.ACTIVE) {
            if (maxImageSize != null) {
                RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                ((Graphics2D) g).setRenderingHints(renderingHints);
                g.setColor(Color.RED);
                g.fillOval((int) xPosition - (int) (0.8 * maxImageSize) / 2, (int) yPosition - (int) (0.8 * maxImageSize) / 2, (int) (0.8 * maxImageSize), (int) (0.8 * maxImageSize));
                g.setColor(Color.MAGENTA);
                g.fillOval((int) xPosition - (int) (0.6 * maxImageSize) / 2, (int) yPosition - (int) (0.6 * maxImageSize) / 2, (int) (0.6 * maxImageSize), (int) (0.6 * maxImageSize));
                ((Graphics2D)g).setStroke(new BasicStroke((int) (0.05 * maxImageSize)));
                g.setColor(Color.BLACK);
                g.drawOval((int) xPosition - (int) (0.8 * maxImageSize) / 2, (int) yPosition - (int) (0.8 * maxImageSize) / 2, (int) (0.8 * maxImageSize), (int) (0.8 * maxImageSize));
                g.drawOval((int) xPosition - (int) (0.6 * maxImageSize) / 2, (int) yPosition - (int) (0.6 * maxImageSize) / 2, (int) (0.6 * maxImageSize), (int) (0.6 * maxImageSize));
                g.setColor(Color.BLUE);
                g.fillOval((int) teleportExit.getX() - (int) (0.8 * maxImageSize) / 2, (int) teleportExit.getY() - (int) (0.8 * maxImageSize) / 2, (int) (0.8 * maxImageSize), (int) (0.8 * maxImageSize));
                g.setColor(Color.MAGENTA);
                g.fillOval((int) teleportExit.getX() - (int) (0.6 * maxImageSize) / 2, (int) teleportExit.getY() - (int) (0.6 * maxImageSize) / 2, (int) (0.6 * maxImageSize), (int) (0.6 * maxImageSize));
                g.setColor(Color.BLACK);
                g.drawOval((int) teleportExit.getX() - (int) (0.8 * maxImageSize) / 2, (int) teleportExit.getY() - (int) (0.8 * maxImageSize) / 2, (int) (0.8 * maxImageSize), (int) (0.8 * maxImageSize));
                g.drawOval((int) teleportExit.getX() - (int) (0.6 * maxImageSize) / 2, (int) teleportExit.getY() - (int) (0.6 * maxImageSize) / 2, (int) (0.6 * maxImageSize), (int) (0.6 * maxImageSize));
                g.setColor(Color.MAGENTA);
                ((Graphics2D) g).setStroke(new BasicStroke((int) (0.1 * maxImageSize)));
                g.drawLine((int) xPosition, (int) yPosition, (int) teleportExit.getX(), (int) teleportExit.getY());
            }
        }
    }

    /**
     * Creates an activated TeleportExit with given position.
     * @param exitXPosition x position of TeleportExit
     * @param exitYPosition y position of TeleportExit
     */
    public void setTeleportExit(int exitXPosition, int exitYPosition) {
        teleportExit = new TeleportExit(duration, exitXPosition, exitYPosition,maxImageSize);
        activate(duration);
        teleportExit.activate(duration);
    }

    /**
     * @return current x position
     */
    public double getX() {
        return xPosition;
    }

    /**
     * @return current y position
     */
    public double getY() {
        return yPosition;
    }
}

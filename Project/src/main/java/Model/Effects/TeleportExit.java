package Model.Effects;

import Model.Effect;
import Model.Units.Affectable;

import java.awt.*;


/**
 * TeleportExit is an effect to which
 * affectable objects are moved to
 */
public class TeleportExit extends Effect {

    public TeleportExit(int duration, double exitXPosition, double exitYPosition, Integer maxImageSize) {
        super(duration,exitXPosition,exitYPosition,maxImageSize);
    }

     /**
     * @return current x position
     */
    public double getX() {
        return xPosition;
    }

    /**
     * @return current y position.
     */
    public double getY() {
        return yPosition;
    }

    @Override
    public void affect(Affectable unitToBeAffected) {
    }

    @Override
    public void draw(Graphics g) {
    }

    /**
     * Reduces the remaining duration of this Effect
     */
    public void reduceDuration() {
        duration--;
    }
}

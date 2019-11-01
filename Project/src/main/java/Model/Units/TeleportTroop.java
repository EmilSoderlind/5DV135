package Model.Units;

import Model.Effect;
import Model.Effects.TeleportEntrance;
import Utils.Vector2D;

import java.awt.*;
import java.util.Map;

/**
 * Class of spawnable teleport troop in the game
 */
public class TeleportTroop extends Troop {

    private boolean readyToDropPortal;
    private Color color;
    private int startXPosition;
    private int startYPosition;

    public TeleportTroop(int xPos, int yPos, Integer maxImageSize) {
        super(xPos, yPos, maxImageSize);
        maxHP = 80;
        currentHP = maxHP;
        defaultSpeed = 0.5;
        currentSpeed = defaultSpeed;
        readyToDropPortal = true;
        startXPosition = xPos;
        startYPosition = yPos;
        color = Color.getHSBColor((float) Math.random(), (float) Math.random(), (float) Math.random());
    }

    @Override
    public void draw(Graphics graphic) {
        if (maxImageSize != null) {
            RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            ((Graphics2D) graphic).setRenderingHints(renderingHints);
            graphic.setColor(color);
            graphic.fillOval((int) position.x - (int) (0.7 * maxImageSize) / 2, (int) position.y - (int) (0.7 * maxImageSize) / 2, (int) (0.7 * maxImageSize), (int) (0.7 * maxImageSize));
            graphic.setColor(Color.BLACK);
            ((Graphics2D) graphic).setStroke(new BasicStroke(1));
            graphic.drawOval((int) position.x - (int) (0.7 * maxImageSize) / 2, (int) position.y - (int) (0.7 * maxImageSize) / 2, (int) (0.7 * maxImageSize), (int) (0.7 * maxImageSize));
            if (effectList.size() > 0) {
                for (Map.Entry<Class<? extends Effect>, Effect> classEffectEntry : effectList.entrySet()) {
                    Effect effect = classEffectEntry.getValue();
                    effect.draw(graphic);
                }
            }
            super.drawHealthBar(graphic);
        }
    }

    @Override
    public void move() {
        Vector2D steeringForce = currentDirection.getDirectionVel().subtract(velocity);
        acceleration = acceleration.add(steeringForce);
        this.velocity = velocity.add(acceleration);
        velocity.normalize();
        velocity.mult(currentSpeed);
        this.position = position.add(velocity);
        acceleration.mult(0);
    }


    public TeleportEntrance dropPortal(int xPosition, int yPosition) {
        if (readyToDropPortal) {
            TeleportEntrance entrancePortal = new TeleportEntrance(10, startXPosition, startYPosition, maxImageSize);
            entrancePortal.setTeleportExit(xPosition, yPosition);
            readyToDropPortal = false;
            return entrancePortal;
        }
        return null;
    }

    /**
     * Check if unit is ready to drop portal
     *
     * @return true if ready to drop, false otherwise.
     */
    public boolean readyToDropPortal() {
        return readyToDropPortal;
    }

    /**
     * Returns the start x position of the unit.
     *
     * @return x position as an int.
     */
    public int getStartXPosition() {
        return startXPosition;
    }


    /**
     * Returns the start y position of the unit.
     *
     * @return y position as an int.
     */
    public int getStartYPosition() {
        return startYPosition;
    }
}

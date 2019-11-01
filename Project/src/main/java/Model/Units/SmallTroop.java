package Model.Units;

import Model.Effect;
import Utils.Vector2D;

import java.awt.*;
import java.util.Map;

/**
 * Class of spawnable small troop in the game
 */
public class SmallTroop extends Troop {

    private final Color color;

    public SmallTroop(int xPos, int yPos, Integer maxImageSize) {
        super(xPos,yPos,maxImageSize);
        maxHP = 40;
        currentHP = maxHP;
        defaultSpeed = (int)(Math.random() * 4 - 2) + 2;
        currentSpeed =defaultSpeed;
        color = Color.getHSBColor((float)Math.random(), (float)Math.random(), (float)Math.random());
    }

    @Override
    public void draw(Graphics graphic) {
        if (maxImageSize != null) {
            RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            ((Graphics2D) graphic).setRenderingHints(renderingHints);
            graphic.setColor(color);
            graphic.fillOval((int)position.getX() - (int) (0.5 * maxImageSize) / 2, (int)position.getY() - (int) (0.5 * maxImageSize) / 2, (int) (0.5 * maxImageSize), (int) (0.5 * maxImageSize));
            graphic.setColor(Color.BLACK);
            ((Graphics2D) graphic).setStroke(new BasicStroke(1));
            graphic.drawOval((int)position.getX() - (int) (0.5 * maxImageSize) / 2, (int)position.getY() - (int) (0.5 * maxImageSize) / 2, (int) (0.5 * maxImageSize), (int) (0.5 * maxImageSize));
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
}

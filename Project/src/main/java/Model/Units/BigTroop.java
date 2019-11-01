package Model.Units;


import Model.Effect;
import Utils.Vector2D;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;

public class BigTroop extends Troop{

    public BigTroop(int xPos, int yPos, Integer maxImageSize ) {
        super(xPos,yPos,maxImageSize);
        maxHP = 200;
        currentHP = maxHP;
        defaultSpeed = 0.8;
        currentSpeed = defaultSpeed;
    }

    @Override
    public void draw(Graphics graphic) {
        if (maxImageSize != null) {
            RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            ((Graphics2D) graphic).setRenderingHints(renderingHints);
            graphic.setColor(Color.BLACK);
            graphic.fillRect((int) (position.x-(0.70 * maxImageSize / 2)), (int) (position.y-(0.70 * maxImageSize / 2)), (int) (0.70 * maxImageSize), (int) (0.70 * maxImageSize));
            graphic.setColor(Color.GRAY);
            ((Graphics2D) graphic).setStroke(new BasicStroke(1));
            graphic.drawRect((int) (position.x-(0.70 * maxImageSize / 2)), (int) (position.y-(0.70 * maxImageSize / 2)), (int) (0.70 * maxImageSize), (int) (0.70 * maxImageSize));
            if (effectList.size() > 0) {
                for (Iterator<HashMap.Entry<Class<? extends Effect>, Effect>> iterable = effectList.entrySet().iterator(); iterable.hasNext(); ) {
                    Effect effect = iterable.next().getValue();
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

package Model.Units;

import Model.Effect;
import Utils.TILESTATUS;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;

import static Utils.TILESTATUS.GOAL;
import static Utils.TILESTATUS.START;

//Credit "Kenney.nl" or "www.kenney.nl", this is not mandatory.

/**
 * Class for the walkable tiles in the game. Extends the
 * Tile super class and implements the LandOn interface
 */
public class WalkableTile extends Tile implements LandOn {

    private Rectangle centerRectangle;

    Image image;

    public WalkableTile(Rectangle frame, TILESTATUS status) {
        super(frame, status);

        int width = frame.width / 5;
        centerRectangle = new Rectangle(
                (int)frame.getX() + frame.width / 2 - width / 2,
                (int)frame.getY() + frame.height / 2 - width / 2,
                width ,width);
    }

    @Override
    public void landOn(Troop troop) {
        if (centerRectangle.contains(new Point((int) troop.getXPos(), (int) troop.getYPos()))) {
            if (getStatus() == GOAL) {
                troop.setHasReachedGoal(true);
            } else {
                troop.setDirection(this.direction);
            }
        }
        if (getNumberOfEffects() > 0) {
            for (Iterator<HashMap.Entry<Class<? extends Effect>, Effect>> iterable = effectList.entrySet().iterator(); iterable.hasNext(); ) {
                Effect effect = iterable.next().getValue();
                if (effect.getDuration() <= 0) {
                    iterable.remove();
                } else {
                    effect.affect(troop);
                }
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.darkGray);
        super.draw(g);
        if (rotatable) {
            g.setColor(Color.white);
            g.setFont(new Font("Helvetica", Font.PLAIN, 18));
            g.drawString(direction.toString(), getFrame().x + getFrame().width / 2 - 9, (int) centerRectangle.getMaxY());
            g.setColor(Color.BLUE);
        } else if (getStatus() == START) {
            g.setColor(Color.BLUE);
            super.draw(g);
        } else if (getStatus() == GOAL) {
            g.setColor(Color.GREEN);
            super.draw(g);
        }
    }

    @Override
    public void setFrame(Rectangle frame) {
        super.setFrame(frame);

        int width = frame.width / 5;
        centerRectangle = new Rectangle(
                (int)frame.getX() + frame.width / 2 - width / 2,
                (int)frame.getY() + frame.height / 2 - width / 2,
                width ,width);
    }

    @Override
    public WalkableTile copy() {
        return new WalkableTile(getFrame(), getStatus());
    }
}

package Model.WalkableTileTest;

import Model.Units.Troop;
import Utils.DIRECTION;

import java.awt.*;

public class StubTroop extends Troop {

    private boolean changedDirection = false;
    private boolean affected = false;

    public StubTroop(int xPosition, int yPosition) {
        super(xPosition, yPosition,null);
    }

    @Override
    public void draw(Graphics graphic) {
    }

    @Override
    public void move() {

    }

    @Override
    public void setDirection(DIRECTION direction) {
        changedDirection = true;
    }

    public boolean changedDirection() {
        return changedDirection;
    }

    public boolean affected() {
        return affected;
    }

    public void setAffected() {
        affected = true;
    }
}

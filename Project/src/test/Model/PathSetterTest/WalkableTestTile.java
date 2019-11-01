package Model.PathSetterTest;

import Model.Units.Tile;
import Utils.TILESTATUS;

import java.awt.*;

import static Utils.DIRECTION.*;

public class WalkableTestTile extends Tile {

    public WalkableTestTile(Rectangle rectangle, TILESTATUS tileStatus) {
        super(rectangle,tileStatus);
    }

    @Override
    public boolean isWalkable() {
        return true;
    }

    @Override
    public Tile copy() {
        return new WalkableTestTile(getFrame(), getStatus());
    }

    @Override
    public String toString() {
        if (direction == NORTH) {
            return "[^]";
        } else if (direction == EAST) {
            return "[>]";
        } else if (direction == SOUTH) {
            return "[v]";
        } else if (direction == WEST) {
            return "[<]";
        }
        return "[0]";
    }
}

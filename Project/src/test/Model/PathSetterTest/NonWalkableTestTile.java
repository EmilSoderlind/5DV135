package Model.PathSetterTest;

import Model.Units.Tile;
import Utils.TILESTATUS;

import java.awt.*;

public class NonWalkableTestTile extends Tile {

    public NonWalkableTestTile(Rectangle frame, TILESTATUS status) {
        super(frame, status);
    }

    @Override
    public boolean isWalkable() {
        return false;
    }

    @Override
    public Tile copy() {
        return new NonWalkableTestTile(getFrame(), getStatus());
    }

    @Override
    public String toString() {
        return "[ ]";
    }
}

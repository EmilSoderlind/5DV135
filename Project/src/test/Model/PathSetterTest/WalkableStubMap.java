package Model.PathSetterTest;

import Model.Map;
import Model.Units.Tile;
import Utils.GridIndex;
import Utils.TILESTATUS;

import java.awt.*;

public class WalkableStubMap extends Map {

    public WalkableStubMap(int rows, int cols, String name) {
        super(rows,cols,name);
        Tile[][] tiles = getTileGrid();
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                Rectangle dummyRectangle = new Rectangle(i, j, 10, 10);
                tiles[i][j] = new WalkableTestTile(dummyRectangle, TILESTATUS.WALKABLE);
            }
        }
        Rectangle dummyRectangle = new Rectangle(1, 1, 10, 10);
        tiles[1][1] = new WalkableTestTile(dummyRectangle, TILESTATUS.GOAL);
    }

    @Override
    public Tile getGoalTile() {
        return getTile(new GridIndex(1, 1));
    }
}

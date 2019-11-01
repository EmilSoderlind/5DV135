package Model.PathSetterTest;

import Model.Map;
import Model.Units.Tile;
import Utils.GridIndex;
import Utils.TILESTATUS;

import java.awt.*;

public class BlockadeStubMap extends Map {

    public BlockadeStubMap(int rows, int cols, String name) {
        super(rows, cols, name);
        Tile[][] tiles = getTileGrid();
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                Rectangle dummyRectangle = new Rectangle(i, j, 10, 10);
                tiles[i][j] = new WalkableTestTile(dummyRectangle, TILESTATUS.WALKABLE);
            }
        }
        Rectangle dummyRectangle1 = new Rectangle(0, 2, 10, 10);
        tiles[0][2] = new WalkableTestTile(dummyRectangle1, TILESTATUS.GOAL);

        Rectangle dummyRectangle2 = new Rectangle(1, 0, 10, 10);
        tiles[1][0] = new NonWalkableTestTile(dummyRectangle2,TILESTATUS.NONWALKABLE);

        Rectangle dummyRectangle3 = new Rectangle(2, 1, 10, 10);
        tiles[2][1] = new NonWalkableTestTile(dummyRectangle3,TILESTATUS.NONWALKABLE);

        Rectangle dummyRectangle4 = new Rectangle(1, 1, 10, 10);
        tiles[1][1] = new NonWalkableTestTile(dummyRectangle4,TILESTATUS.NONWALKABLE);
    }

    @Override
    public Tile getGoalTile() {
        return getTile(new GridIndex(0, 2));
    }
}

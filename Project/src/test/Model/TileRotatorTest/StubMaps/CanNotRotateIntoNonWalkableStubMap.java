package Model.TileRotatorTest.StubMaps;

import Model.Map;
import Model.PathSetterTest.NonWalkableTestTile;
import Model.PathSetterTest.WalkableTestTile;
import Model.Units.Tile;
import Utils.GridIndex;
import Utils.TILESTATUS;

import java.awt.*;

import static Utils.DIRECTION.NORTH;

public class CanNotRotateIntoNonWalkableStubMap extends Map {

    public CanNotRotateIntoNonWalkableStubMap(int rows, int cols, String name) {
        super(rows, cols, name);
        Tile[][] tiles = getTileGrid();
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                Rectangle dummyRectangle = new Rectangle(i, j, 10, 10);
                tiles[i][j] = new WalkableTestTile(dummyRectangle, TILESTATUS.WALKABLE);
            }
        }

        GridIndex centerIndex = new GridIndex(1, 1);
        getTile(centerIndex).setDirection(NORTH);

        Rectangle dummyRectangle1 = new Rectangle(2, 1, 10, 10);
        tiles[2][1] = new NonWalkableTestTile(dummyRectangle1,TILESTATUS.NONWALKABLE);

        Rectangle dummyRectangle2 = new Rectangle(1, 2, 10, 10);
        tiles[1][2] = new NonWalkableTestTile(dummyRectangle2,TILESTATUS.NONWALKABLE);

        Rectangle dummyRectangle3 = new Rectangle(0, 1, 10, 10);
        tiles[0][1] = new NonWalkableTestTile(dummyRectangle3,TILESTATUS.NONWALKABLE);
    }
}

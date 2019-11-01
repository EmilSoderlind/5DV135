package Model.TileRotatorTest.StubMaps;

import Model.Map;
import Model.PathSetterTest.WalkableTestTile;
import Model.Units.Tile;
import Utils.GridIndex;
import Utils.TILESTATUS;

import java.awt.*;

import static Utils.DIRECTION.*;

public class NorthToEastStubMap extends Map {

    public NorthToEastStubMap(int rows, int cols, String name) {
        super(rows, cols, name);
        Tile[][] tiles = getTileGrid();
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                Rectangle dummyRectangle = new Rectangle(i, j, 10, 10);
                tiles[i][j] = new WalkableTestTile(dummyRectangle, TILESTATUS.WALKABLE);
            }
        }

        GridIndex gridIndex1 = new GridIndex(1, 1);
        GridIndex gridIndex2 = new GridIndex(1,0);
        GridIndex gridIndex3 = new GridIndex(2,1);
        GridIndex gridIndex4 = new GridIndex(1,2);
        GridIndex gridIndex5 = new GridIndex(0,1);

        getTile(gridIndex1).setDirection(NORTH);
        getTile(gridIndex1).setRotatable(true);
        getTile(gridIndex2).setDirection(NORTH);
        getTile(gridIndex3).setDirection(EAST);
        getTile(gridIndex4).setDirection(SOUTH);
        getTile(gridIndex5).setDirection(WEST);

    }
}

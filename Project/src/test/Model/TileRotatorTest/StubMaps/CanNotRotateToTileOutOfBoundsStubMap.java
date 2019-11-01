package Model.TileRotatorTest.StubMaps;

import Model.Map;
import Model.PathSetterTest.WalkableTestTile;
import Model.Units.Tile;
import Utils.TILESTATUS;

import java.awt.*;

import static Utils.DIRECTION.NORTH;

public class CanNotRotateToTileOutOfBoundsStubMap extends Map {

    public CanNotRotateToTileOutOfBoundsStubMap(int rows, int cols, String name) {
        super(rows, cols, name);
        Tile[][] tiles = getTileGrid();
        Rectangle dummyRectangle = new Rectangle(0, 0, 10, 10);
        tiles[0][0] = new WalkableTestTile(dummyRectangle,TILESTATUS.WALKABLE);
        tiles[0][0].setDirection(NORTH);
        tiles[0][0].setRotatable(true);
    }
}

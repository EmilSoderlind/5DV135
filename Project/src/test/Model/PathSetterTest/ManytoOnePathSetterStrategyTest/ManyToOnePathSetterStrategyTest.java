package Model.PathSetterTest.ManytoOnePathSetterStrategyTest;

import Model.ManyToOnePathSetterStrategy;
import Model.Map;
import Model.PathSetter;
import Model.PathSetterTest.BlockadeStubMap;
import Model.PathSetterTest.WalkableStubMap;
import Utils.GridIndex;
import org.junit.jupiter.api.Test;

import static Utils.DIRECTION.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ManyToOnePathSetterStrategyTest {

    @Test
    public void testAllTilesShouldHaveDirectionIfReachableFromGoal() {
        GridIndex currentGridIndex = new GridIndex(0, 0);
        boolean allTilesHaveDirection = true;
        Map map = setUpWalkableMap();
        PathSetter pathSetter = new PathSetter(new ManyToOnePathSetterStrategy());
        pathSetter.setPaths(map);
        for (int i = 0; i < map.getCols(); i++) {
            for (int j = 0; j < map.getRows(); j++) {
                currentGridIndex.setX(i);
                currentGridIndex.setY(j);
                if (map.getTile(currentGridIndex).getDirection() == null
                        && !map.getTile(currentGridIndex).isGoal()) {
                    allTilesHaveDirection = false;
                }
            }
        }
        assertTrue(allTilesHaveDirection);
    }



    @Test
    public void testNorthTileShouldHaveDirectionSouth() {
        Map map = setUpWalkableMap();
        PathSetter pathSetter = new PathSetter(new ManyToOnePathSetterStrategy());
        pathSetter.setPaths(map);
        assertEquals(SOUTH,map.getTile(new GridIndex(1,0)).getDirection());
    }

    @Test
    public void testEastTileShouldHaveDirectionWest() {
        Map map = setUpWalkableMap();
        PathSetter pathSetter = new PathSetter(new ManyToOnePathSetterStrategy());
        pathSetter.setPaths(map);
        assertEquals(WEST, map.getTile(new GridIndex(2, 1)).getDirection());
    }

    @Test
    public void testSouthTileShouldHaveDirectionNorth() {
        Map map = setUpWalkableMap();
        PathSetter pathSetter = new PathSetter(new ManyToOnePathSetterStrategy());
        pathSetter.setPaths(map);
        assertEquals(NORTH, map.getTile(new GridIndex(1, 2)).getDirection());
    }

    @Test
    public void testWestTileShouldHaveDirectionEast() {
        Map map = setUpWalkableMap();
        PathSetter pathSetter = new PathSetter(new ManyToOnePathSetterStrategy());
        pathSetter.setPaths(map);
        assertEquals(EAST, map.getTile(new GridIndex(0, 1)).getDirection());
    }

    @Test
    public void testTilesNotReachableFromGoalShouldNotHaveDirection() {
        Map map = setUpMapWithBlockade();
        PathSetter pathSetter = new PathSetter(new ManyToOnePathSetterStrategy());
        pathSetter.setPaths(map);
        assertEquals(null, map.getTile(new GridIndex(2,0)).getDirection());
    }

    Map setUpWalkableMap() {
        Map map = new WalkableStubMap(3,3,"Walkable Mock map");
        return map;
    }

    Map setUpMapWithBlockade() {
        Map map = new BlockadeStubMap(3,3,"Blockade Mock map");
        return map;
    }
}
